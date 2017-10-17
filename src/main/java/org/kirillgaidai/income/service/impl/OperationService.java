package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.dao.intf.IOperationDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.OperationDto;
import org.kirillgaidai.income.service.exception.IncomeServiceAccountNotFoundException;
import org.kirillgaidai.income.service.exception.IncomeServiceBalanceNotFoundException;
import org.kirillgaidai.income.service.exception.IncomeServiceCategoryNotFoundException;
import org.kirillgaidai.income.service.exception.IncomeServiceOperationNotFoundException;
import org.kirillgaidai.income.service.intf.IOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OperationService implements IOperationService {

    final private IAccountDao accountDao;
    final private IOperationDao operationDao;
    final private IBalanceDao balanceDao;
    final private ICategoryDao categoryDao;
    final private IGenericConverter<OperationEntity, OperationDto> operationConverter;

    @Autowired
    public OperationService(
            IAccountDao accountDao,
            IOperationDao operationDao,
            IBalanceDao balanceDao,
            ICategoryDao categoryDao,
            IGenericConverter<OperationEntity, OperationDto> operationConverter) {
        super();
        this.accountDao = accountDao;
        this.operationDao = operationDao;
        this.balanceDao = balanceDao;
        this.categoryDao = categoryDao;
        this.operationConverter = operationConverter;
    }

    @Override
    public List<OperationDto> getList() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<OperationDto> getList(Set<Integer> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OperationDto get(Integer id) {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates new operation. Recalculates balance for account if necessary.
     *
     * @param operationDto - operation dto
     */
    @Override
    public OperationDto save(OperationDto operationDto) {
        Integer accountId = operationDto.getAccountId();
        AccountEntity accountEntity = accountDao.getEntity(accountId);
        if (accountEntity == null) {
            throw new IncomeServiceAccountNotFoundException(accountId);
        }

        LocalDate thisDay = operationDto.getDay();

        BalanceEntity thisBalanceEntity = balanceDao.getEntity(accountId, thisDay);
        BalanceEntity prevBalanceEntity = balanceDao.getEntityBefore(accountId, thisDay);

        // if no operations for account, then error
        if (thisBalanceEntity == null && prevBalanceEntity == null) {
            throw new IncomeServiceBalanceNotFoundException(accountEntity.getTitle(), thisDay);
        }

        BigDecimal amount = operationDto.getAmount();
        OperationEntity operationEntity = operationConverter.convertToEntity(operationDto);

        // if balance before this day exists, but no balance for this day - calculating balance for this day
        if (thisBalanceEntity == null) {
            BalanceEntity newBalanceEntity =
                    new BalanceEntity(accountId, thisDay, prevBalanceEntity.getAmount().subtract(amount), false);
            balanceDao.insertEntity(newBalanceEntity);
            operationDao.insertEntity(operationEntity);
            return null;
        }

        // if balance for this day exists, but no balance before - calculating balance for previous day
        if (prevBalanceEntity == null) {
            BalanceEntity newBalanceEntity = new BalanceEntity(accountId, thisDay.minusDays(1L),
                    thisBalanceEntity.getAmount().add(amount), false);
            balanceDao.insertEntity(newBalanceEntity);
            operationDao.insertEntity(operationEntity);
            return null;
        }

        // if both balances exist, but fixed - no balance recalculation
        if (prevBalanceEntity.getManual() && thisBalanceEntity.getManual()) {
            operationDao.insertEntity(operationEntity);
            return null;
        }

        // After that point both balances (for this day and previous balance) exist
        BigDecimal prevAmount = prevBalanceEntity.getAmount();
        BigDecimal thisAmount = thisBalanceEntity.getAmount();
        LocalDate prevDay = prevBalanceEntity.getDay();

        // If previous balance fixed, only balance for this day may be recalculated
        if (prevBalanceEntity.getManual()) {
            // Balance for this day may be recalculated (operation amount is subtracted from it) only
            // if no balance after this day
            if (balanceDao.getEntityAfter(accountId, thisDay) == null) {
                balanceDao.updateEntity(new BalanceEntity(accountId, thisDay, thisAmount.subtract(amount), false));
            }
            operationDao.insertEntity(operationEntity);
            return null;
        }

        // If this balance fixed, only balance for previous day may be recalculated
        if (thisBalanceEntity.getManual()) {
            // Balance for previous day may be recalculated (operation amount is added to it) only
            // if no balance before previous day
            if (balanceDao.getEntityBefore(accountId, prevDay) == null) {
                balanceDao.updateEntity(new BalanceEntity(accountId, prevDay, prevAmount.add(amount), false));
            }
            operationDao.insertEntity(operationEntity);
            return null;
        }

        // After that point no fixed balances at this or previous days

        // If no balance after this day then then recalculating balance for this day (subtracting)
        if (balanceDao.getEntityAfter(accountId, thisDay) == null) {
            balanceDao.updateEntity(new BalanceEntity(accountId, thisDay, thisAmount.subtract(amount), false));
            operationDao.insertEntity(operationEntity);
            return null;
        }

        // If no balance before previous day then recalculating balance for previous day (adding)
        if (balanceDao.getEntityBefore(accountId, prevDay) == null) {
            balanceDao.updateEntity(new BalanceEntity(accountId, prevDay, prevAmount.add(amount), false));
            operationDao.insertEntity(operationEntity);
            return null;
        }

        // Balance recalculation in the middle is forbidden
        operationDao.insertEntity(operationEntity);
        return null;
    }

    /**
     * Deletes operation and updates account balance if needed
     *
     * @param id - operation id
     */
    @Override
    public void delete(Integer id) {
        // Error in case operation with specified id is not found
        OperationEntity operationEntity = operationDao.getEntity(id);
        if (operationEntity == null) {
            throw new IncomeServiceOperationNotFoundException(id);
        }

        // Retrieving balance for operation account and date
        Integer accountId = operationEntity.getAccountId();
        LocalDate thisDay = operationEntity.getDay();
        BalanceEntity thisBalanceEntity = balanceDao.getEntity(accountId, thisDay);
        if (thisBalanceEntity == null) {
            throw new IncomeServiceBalanceNotFoundException(accountId, thisDay);
        }

        // Retrieving balance for operation account before operation date
        BalanceEntity prevBalanceEntity = balanceDao.getEntityBefore(accountId, thisDay);
        if (prevBalanceEntity == null) {
            throw new IncomeServiceBalanceNotFoundException(accountId, thisDay);
        }

        // At this point this and previous balances exist
        LocalDate prevDay = prevBalanceEntity.getDay();

        // If this and previous balances fixed, then simply deleting operation
        if (thisBalanceEntity.getManual() && prevBalanceEntity.getManual()) {
            operationDao.deleteEntity(id);
            return;
        }

        // At this point only one of two balances is fixed
        BigDecimal amount = operationEntity.getAmount();
        BigDecimal thisBalanceAmount = thisBalanceEntity.getAmount();
        BigDecimal prevBalanceAmount = prevBalanceEntity.getAmount();

        // If previous balance is fixed then updating this balance if no balances after this
        if (prevBalanceEntity.getManual()) {
            if (balanceDao.getEntityAfter(accountId, thisDay) == null) {
                balanceDao.updateEntity(new BalanceEntity(accountId, thisDay, thisBalanceAmount.add(amount), false));
            }
            operationDao.deleteEntity(id);
            return;
        }

        // If this balance is fixed then updating previous one if no balances before it
        if (thisBalanceEntity.getManual()) {
            if (balanceDao.getEntityBefore(accountId, prevDay) == null) {
                balanceDao.updateEntity(
                        new BalanceEntity(accountId, prevDay, prevBalanceAmount.subtract(amount), false));
            }
            operationDao.deleteEntity(id);
            return;
        }

        // At this point no balances are fixed

        // If no balances after this, then updating this balance
        if (balanceDao.getEntityAfter(accountId, thisDay) == null) {
            balanceDao.updateEntity(new BalanceEntity(accountId, thisDay, thisBalanceAmount.add(amount), false));
            operationDao.deleteEntity(id);
            return;
        }

        // If no balances before previous then updating previous balance
        if (balanceDao.getEntityBefore(accountId, prevDay) == null) {
            balanceDao.updateEntity(new BalanceEntity(accountId, prevDay, prevBalanceAmount.subtract(amount), false));
        }

        // Otherwise, simply deleting operation
        operationDao.deleteEntity(id);
    }

    @Override
    public List<OperationDto> getDtoList(Set<Integer> accountIds, LocalDate day) {
        return convertToDtoList(operationDao.getEntityList(accountIds, day));
    }

    @Override
    public List<OperationDto> getDtoList(Set<Integer> accountIds, LocalDate day, Integer categoryId) {
        return convertToDtoList(operationDao.getEntityList(accountIds, day, categoryId));
    }

    private List<OperationDto> convertToDtoList(List<OperationEntity> operationEntityList) {
        if (operationEntityList.isEmpty()) {
            return Collections.emptyList();
        }

        List<OperationDto> operationDtoList = operationEntityList.stream().map(operationConverter::convertToDto)
                .collect(Collectors.toList());

        Set<Integer> accountIds = new HashSet<>();
        Set<Integer> categoryIds = new HashSet<>();
        for (OperationDto operationDto : operationDtoList) {
            accountIds.add(operationDto.getAccountId());
            categoryIds.add(operationDto.getCategoryId());
        }

        Map<Integer, AccountEntity> accountEntityMap = accountDao.getEntityList(accountIds).stream()
                .collect(Collectors.toMap(AccountEntity::getId, accountEntity -> accountEntity));
        Map<Integer, CategoryEntity> categoryEntityMap = categoryDao.getEntityList(categoryIds).stream()
                .collect(Collectors.toMap(CategoryEntity::getId, categoryEntity -> categoryEntity));

        operationDtoList.forEach(operationDto -> {
            Integer accountId = operationDto.getAccountId();
            if (!accountEntityMap.containsKey(accountId)) {
                throw new IncomeServiceAccountNotFoundException(accountId);
            }
            operationDto.setAccountTitle(accountEntityMap.get(accountId).getTitle());

            Integer categoryId = operationDto.getCategoryId();
            if (!categoryEntityMap.containsKey(categoryId)) {
                throw new IncomeServiceCategoryNotFoundException(categoryId);
            }
            operationDto.setCategoryTitle(categoryEntityMap.get(categoryId).getTitle());
        });

        return operationDtoList;
    }

    /**
     * Prepares operation dto for new operation input.
     * If account ids set contains single id, fills account id and title. Otherwise sets them to null.
     * If category id is not null fills category id and title. Otherwise sets them to null.
     *
     * @param accountIds - account ids set
     * @param categoryId - category id
     * @return prepared operation dto
     */
    @Override
    public OperationDto getDto(Set<Integer> accountIds, Integer categoryId, LocalDate day) {
        OperationDto result = new OperationDto(null, null, null, null, null, day, BigDecimal.ZERO, null);

        if (accountIds.size() == 1) {
            AccountEntity accountEntity = accountDao.getEntity(accountIds.iterator().next());
            result.setAccountId(accountEntity.getId());
            result.setAccountTitle(accountEntity.getTitle());
        }

        if (categoryId != null) {
            CategoryEntity categoryEntity = categoryDao.getEntity(categoryId);
            result.setCategoryId(categoryEntity.getId());
            result.setCategoryTitle(categoryEntity.getTitle());
        }

        return result;
    }

}
