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
    public List<OperationDto> getDtoList() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<OperationDto> getDtoList(Set<Integer> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public OperationDto getDto(Integer id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void saveDto(OperationDto operationDto) {
        Integer accountId = operationDto.getAccountId();
        AccountEntity accountEntity = accountDao.getEntity(accountId);
        if (accountEntity == null) {
            throw new IncomeServiceAccountNotFoundException(accountId);
        }

        LocalDate thisDay = operationDto.getDay();
        LocalDate prevDay = thisDay.minusDays(1L);

        BalanceEntity thisBalanceEntity = balanceDao.getEntity(accountId, thisDay);
        BalanceEntity prevBalanceEntity = balanceDao.getEntity(accountId, prevDay);
        if (thisBalanceEntity == null && prevBalanceEntity == null) {
            throw new IncomeServiceBalanceNotFoundException(accountEntity.getTitle(), prevDay);
        }

        BigDecimal amount = operationDto.getAmount();
        OperationEntity operationEntity = operationConverter.convertToEntity(operationDto);

        if (thisBalanceEntity == null) {
            BalanceEntity newBalanceEntity = new BalanceEntity(accountId, thisDay,
                    prevBalanceEntity.getAmount().subtract(amount), false);
            balanceDao.insertEntity(newBalanceEntity);
            operationDao.insertEntity(operationEntity);
            return;
        }

        if (prevBalanceEntity == null) {
            BalanceEntity newBalanceEntity = new BalanceEntity(accountId, prevDay,
                    thisBalanceEntity.getAmount().add(amount), false);
            balanceDao.insertEntity(newBalanceEntity);
            operationDao.insertEntity(operationEntity);
            return;
        }

        if (prevBalanceEntity.getManual() && thisBalanceEntity.getManual()) {
            operationDao.insertEntity(operationEntity);
            return;
        }

        LocalDate afterDay = thisDay.plusDays(1L);
        LocalDate beforeDay = prevDay.minusDays(1L);
        BigDecimal prevAmount = prevBalanceEntity.getAmount();
        BigDecimal thisAmount = thisBalanceEntity.getAmount();

        if (prevBalanceEntity.getManual()) {
            if (balanceDao.getEntity(accountId, afterDay) == null) {
                balanceDao.updateEntity(new BalanceEntity(accountId, thisDay, thisAmount.subtract(amount), false));
            }
            operationDao.insertEntity(operationEntity);
            return;
        }

        if (thisBalanceEntity.getManual()) {
            if (balanceDao.getEntity(accountId, beforeDay) == null) {
                balanceDao.updateEntity(new BalanceEntity(accountId, prevDay, prevAmount.add(amount), false));
            }
            operationDao.insertEntity(operationEntity);
            return;
        }

        if (balanceDao.getEntity(accountId, afterDay) == null) {
            balanceDao.updateEntity(new BalanceEntity(accountId, thisDay, thisAmount.subtract(amount), false));
            operationDao.insertEntity(operationEntity);
            return;
        }

        if (balanceDao.getEntity(accountId, beforeDay) == null) {
            balanceDao.updateEntity(new BalanceEntity(accountId, prevDay, prevAmount.add(amount), false));
            operationDao.insertEntity(operationEntity);
            return;
        }

        operationDao.insertEntity(operationEntity);
    }

    @Override
    public void deleteDto(Integer id) {
        OperationEntity operationEntity = operationDao.getEntity(id);
        if (operationEntity == null) {
            throw new IncomeServiceOperationNotFoundException(id);
        }

        Integer accountId = operationEntity.getAccountId();
        LocalDate thisDay = operationEntity.getDay();
        LocalDate afterDay = thisDay.plusDays(1L);
        LocalDate prevDay = thisDay.minusDays(1L);
        LocalDate beforeDay = prevDay.minusDays(1L);

        BalanceEntity thisBalanceEntity = balanceDao.getEntity(accountId, thisDay);
        if (thisBalanceEntity == null) {
            throw new IncomeServiceBalanceNotFoundException(accountId, thisDay);
        }

        BalanceEntity prevBalanceEntity = balanceDao.getEntity(accountId, prevDay);
        if (prevBalanceEntity == null) {
            throw new IncomeServiceBalanceNotFoundException(accountId, prevDay);
        }

        if (thisBalanceEntity.getManual() && prevBalanceEntity.getManual()) {
            operationDao.deleteEntity(id);
            return;
        }

        BigDecimal amount = operationEntity.getAmount();
        BigDecimal thisBalanceAmount = thisBalanceEntity.getAmount();
        BigDecimal prevBalanceAmount = prevBalanceEntity.getAmount();

        if (prevBalanceEntity.getManual()) {
            if (balanceDao.getEntity(accountId, afterDay) == null) {
                balanceDao.updateEntity(new BalanceEntity(accountId, thisDay, thisBalanceAmount.add(amount), false));
            }
            operationDao.deleteEntity(id);
            return;
        }

        if (thisBalanceEntity.getManual()) {
            if (balanceDao.getEntity(accountId, beforeDay) == null) {
                balanceDao.updateEntity(new BalanceEntity(accountId, prevDay, prevBalanceAmount.subtract(amount), false));
            }
            operationDao.deleteEntity(id);
            return;
        }

        if (balanceDao.getEntity(accountId, afterDay) == null) {
            balanceDao.updateEntity(new BalanceEntity(accountId, thisDay, thisBalanceAmount.add(amount), false));
            operationDao.deleteEntity(id);
            return;
        }

        if (balanceDao.getEntity(accountId, beforeDay) == null) {
            balanceDao.updateEntity(new BalanceEntity(accountId, prevDay, prevBalanceAmount.subtract(amount), false));
        }

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
