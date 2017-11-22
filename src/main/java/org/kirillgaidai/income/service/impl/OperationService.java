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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OperationService extends SerialService<OperationDto, OperationEntity> implements IOperationService {

    final private static Logger LOGGER = LoggerFactory.getLogger(OperationService.class);

    final private IAccountDao accountDao;
    final private IBalanceDao balanceDao;
    final private ICategoryDao categoryDao;

    @Autowired
    public OperationService(
            IAccountDao accountDao,
            IOperationDao operationDao,
            IBalanceDao balanceDao,
            ICategoryDao categoryDao,
            IGenericConverter<OperationEntity, OperationDto> converter) {
        super(operationDao, converter);
        this.accountDao = accountDao;
        this.balanceDao = balanceDao;
        this.categoryDao = categoryDao;
    }

    protected IOperationDao getDao() {
        return (IOperationDao) dao;
    }

    @Override
    public List<OperationDto> getDtoList(Set<Integer> accountIds, LocalDate day) {
        return populateAdditionalFields(getDao().getList(accountIds, day).stream()
                .map(converter::convertToDto).collect(Collectors.toList()));
    }

    @Override
    public List<OperationDto> getDtoList(Set<Integer> accountIds, LocalDate day, Integer categoryId) {
        return populateAdditionalFields(getDao().getList(accountIds, day, categoryId).stream()
                .map(converter::convertToDto).collect(Collectors.toList()));
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
            AccountEntity accountEntity = accountDao.get(accountIds.iterator().next());
            result.setAccountId(accountEntity.getId());
            result.setAccountTitle(accountEntity.getTitle());
        }

        if (categoryId != null) {
            CategoryEntity categoryEntity = categoryDao.get(categoryId);
            result.setCategoryId(categoryEntity.getId());
            result.setCategoryTitle(categoryEntity.getTitle());
        }

        return result;
    }

    @Override
    public OperationDto create(OperationDto dto) {
        LOGGER.trace("Entering method");
        Integer accountId = dto.getAccountId();
        Integer categoryId = dto.getCategoryId();
        LocalDate thisDay = dto.getDay();

        AccountEntity accountEntity = getAccount(accountId);
        CategoryEntity categoryEntity = getCategory(categoryId);

        BalanceEntity thisBalanceEntity = balanceDao.get(accountId, thisDay);
        BalanceEntity prevBalanceEntity = balanceDao.getBefore(accountId, thisDay);
        if (thisBalanceEntity == null && prevBalanceEntity == null) {
            // If no operations for account at this day or before, then error
            LOGGER.error("Balance for account {} at or before day {} not found", accountId, thisDay);
            throw new IncomeServiceBalanceNotFoundException(accountId, thisDay, 0);
        }

        BigDecimal amount = dto.getAmount();

        if (thisBalanceEntity == null) {
            // If balance before this day exists, but no balance for this day - calculating balance for this day
            BigDecimal newAmount = prevBalanceEntity.getAmount().subtract(amount);
            BalanceEntity newBalanceEntity = new BalanceEntity(accountId, thisDay, newAmount, false);
            LOGGER.debug("Inserting balance for account id {} day {} equal to {}", accountId, thisDay, newAmount);
            balanceDao.insert(newBalanceEntity);
            return insertOperation(dto, accountEntity, categoryEntity);
        }

        if (prevBalanceEntity == null) {
            // If balance for this day exists, but no balance before - calculating balance for previous day
            BigDecimal newAmount = thisBalanceEntity.getAmount().add(amount);
            LocalDate prevDay = thisDay.minusDays(1L);
            BalanceEntity newBalanceEntity = new BalanceEntity(accountId, prevDay, newAmount, false);
            LOGGER.debug("Inserting balance for account id {} day {} equal to {}", accountId, prevDay, newAmount);
            balanceDao.insert(newBalanceEntity);
            return insertOperation(dto, accountEntity, categoryEntity);
        }

        if (thisBalanceEntity.getManual() && prevBalanceEntity.getManual()) {
            // If both balances exist, but fixed - no balance recalculation
            LOGGER.debug("No balance changed");
            return insertOperation(dto, accountEntity, categoryEntity);
        }

        // After that point both balances (for this day and previous balance) exist
        BigDecimal thisAmount = thisBalanceEntity.getAmount();
        BigDecimal prevAmount = prevBalanceEntity.getAmount();
        LocalDate prevDay = prevBalanceEntity.getDay();

        if (prevBalanceEntity.getManual()) {
            // If previous balance fixed, only balance for this day may be recalculated
            if (balanceDao.getAfter(accountId, thisDay) == null) {
                // Balance for this day may be recalculated (operation amount is subtracted from it) only
                // if no balance after this day
                BigDecimal newAmount = thisAmount.subtract(amount);
                BalanceEntity newBalanceEntity = new BalanceEntity(accountId, thisDay, newAmount, false);
                updateBalance(newBalanceEntity, thisBalanceEntity);
            }
            return insertOperation(dto, accountEntity, categoryEntity);
        }

        if (thisBalanceEntity.getManual()) {
            // If this balance fixed, only balance for previous day may be recalculated
            if (balanceDao.getBefore(accountId, prevDay) == null) {
                // Balance for previous day may be recalculated (operation amount is added to it) only
                // if no balance before previous day
                BigDecimal newAmount = prevAmount.add(amount);
                BalanceEntity newBalanceEntity = new BalanceEntity(accountId, prevDay, newAmount, false);
                updateBalance(newBalanceEntity, prevBalanceEntity);
            }
            return insertOperation(dto, accountEntity, categoryEntity);
        }

        // After that point no fixed balances at this or previous days
        if (balanceDao.getAfter(accountId, thisDay) == null) {
            // If no balance after this day then then recalculating balance for this day (subtracting)
            BigDecimal newAmount = thisAmount.subtract(amount);
            BalanceEntity newBalanceEntity = new BalanceEntity(accountId, thisDay, newAmount, false);
            updateBalance(newBalanceEntity, thisBalanceEntity);
        } else if (balanceDao.getBefore(accountId, prevDay) == null) {
            // If no balance before previous day then recalculating balance for previous day (adding)
            BigDecimal newAmount = prevAmount.add(amount);
            BalanceEntity newBalanceEntity = new BalanceEntity(accountId, prevDay, newAmount, false);
            updateBalance(newBalanceEntity, prevBalanceEntity);
        }

        return insertOperation(dto, accountEntity, categoryEntity);
    }

    @Override
    public OperationDto update(OperationDto dto) {
        LOGGER.trace("Entering method");

        Integer accountId = dto.getAccountId();
        AccountEntity accountEntity = getAccount(accountId);

        Integer categoryId = dto.getCategoryId();
        CategoryEntity categoryEntity = getCategory(categoryId);

        LocalDate thisDay = dto.getDay();
        BalanceEntity thisBalanceEntity = balanceDao.get(accountId, thisDay);
        if (thisBalanceEntity == null) {
            // If no operations for account at this day, then error
            LOGGER.error("Balance for account {} at day {} not found", accountId, thisDay);
            throw new IncomeServiceBalanceNotFoundException(accountId, thisDay, 0);
        }

        BalanceEntity prevBalanceEntity = balanceDao.getBefore(accountId, thisDay);
        if (prevBalanceEntity == null) {
            // If no operations for account before day, then error
            LOGGER.error("Balance for account {} before day {} not found", accountId, thisDay);
            throw new IncomeServiceBalanceNotFoundException(accountId, thisDay, -1);
        }

        LocalDate prevDay = prevBalanceEntity.getDay();
        OperationEntity oldOperationEntity = getDao().get(dto.getId());
        if (oldOperationEntity == null) {
            throwNotFoundException(dto.getId());
        }

        if (prevBalanceEntity.getManual() && thisBalanceEntity.getManual()) {
            return updateOperation(dto, oldOperationEntity, accountEntity, categoryEntity);
        }

        if (prevBalanceEntity.getManual()) {
            if (balanceDao.getAfter(accountId, thisDay) == null) {
                updateBalance(new BalanceEntity(accountId, thisDay,
                        prevBalanceEntity.getAmount().subtract(dto.getAmount()), false), thisBalanceEntity);
            }
            return updateOperation(dto, oldOperationEntity, accountEntity, categoryEntity);
        }

        if (thisBalanceEntity.getManual()) {
            if (balanceDao.getBefore(accountId, prevDay) == null) {
                updateBalance(new BalanceEntity(accountId, prevDay,
                        thisBalanceEntity.getAmount().add(dto.getAmount()), false), prevBalanceEntity);
            }
            return updateOperation(dto, oldOperationEntity, accountEntity, categoryEntity);
        }

        if (balanceDao.getAfter(accountId, thisDay) == null) {
            updateBalance(new BalanceEntity(accountId, thisDay,
                    prevBalanceEntity.getAmount().subtract(dto.getAmount()), false), thisBalanceEntity);
        } else if (balanceDao.getBefore(accountId, prevDay) == null) {
            updateBalance(new BalanceEntity(accountId, prevDay,
                    thisBalanceEntity.getAmount().add(dto.getAmount()), false), prevBalanceEntity);
        }
        return updateOperation(dto, oldOperationEntity, accountEntity, categoryEntity);
    }

    /**
     * Deletes operation and updates account balance if needed
     *
     * @param id - operation id
     */
    @Override
    public void delete(Integer id) {
        LOGGER.debug("Entering method");

        // Error in case operation with specified id is not found
        OperationEntity operationEntity = getDao().get(id);
        if (operationEntity == null) {
            LOGGER.error("Operation with id {} not found", id);
            throw new IncomeServiceOperationNotFoundException(id);
        }
        Integer accountId = operationEntity.getAccountId();
        LocalDate thisDay = operationEntity.getDay();
        BigDecimal amount = operationEntity.getAmount();

        // Retrieving balance for operation account and date
        BalanceEntity thisBalanceEntity = balanceDao.get(accountId, thisDay);
        if (thisBalanceEntity == null) {
            throw new IncomeServiceBalanceNotFoundException(accountId, thisDay, 0);
        }

        // Retrieving balance for operation account before operation date
        BalanceEntity prevBalanceEntity = balanceDao.getBefore(accountId, thisDay);
        if (prevBalanceEntity == null) {
            throw new IncomeServiceBalanceNotFoundException(accountId, thisDay, -1);
        }
        LocalDate prevDay = prevBalanceEntity.getDay();

        // At this point this and previous balances exist

        // If this and previous balances fixed, then simply deleting operation
        if (thisBalanceEntity.getManual() && prevBalanceEntity.getManual()) {
            deleteOperation(operationEntity);
            return;
        }

        // At this point only one of two balances is fixed

        // If previous balance is fixed then updating this balance if no balances after this
        if (prevBalanceEntity.getManual()) {
            if (balanceDao.getAfter(accountId, thisDay) == null) {
                BigDecimal newAmount = thisBalanceEntity.getAmount().add(amount);
                BalanceEntity newBalanceEntity = new BalanceEntity(accountId, thisDay, newAmount, false);
                updateBalance(newBalanceEntity, thisBalanceEntity);
            }
            deleteOperation(operationEntity);
            return;
        }

        // If this balance is fixed then updating previous one if no balances before it
        if (thisBalanceEntity.getManual()) {
            if (balanceDao.getBefore(accountId, prevDay) == null) {
                BigDecimal newAmount = prevBalanceEntity.getAmount().subtract(amount);
                BalanceEntity newBalanceEntity = new BalanceEntity(accountId, prevDay, newAmount, false);
                updateBalance(newBalanceEntity, prevBalanceEntity);
            }
            deleteOperation(operationEntity);
            return;
        }

        // At this point no balances are fixed

        if (balanceDao.getAfter(accountId, thisDay) == null) {
            // If no balances after this, then updating this balance
            BigDecimal newAmount = thisBalanceEntity.getAmount().add(amount);
            BalanceEntity newBalanceEntity = new BalanceEntity(accountId, thisDay, newAmount, false);
            updateBalance(newBalanceEntity, thisBalanceEntity);
        } else if (balanceDao.getBefore(accountId, prevDay) == null) {
            // If no balances before previous then updating previous balance
            BigDecimal newAmount = prevBalanceEntity.getAmount().subtract(amount);
            BalanceEntity newBalanceEntity = new BalanceEntity(accountId, prevDay, newAmount, false);
            updateBalance(newBalanceEntity, prevBalanceEntity);
        }

        // Otherwise, simply deleting operation
        deleteOperation(operationEntity);
    }

    /**
     * Inserts operation and returns inserted operation dto
     *
     * @param dto            operation dto
     * @param accountEntity  operation account
     * @param categoryEntity operation category
     * @return operation dto
     */
    private OperationDto insertOperation(OperationDto dto, AccountEntity accountEntity, CategoryEntity categoryEntity) {
        LOGGER.debug("Inserting operation");
        OperationEntity entity = converter.convertToEntity(dto);
        getDao().insert(entity);
        OperationDto result = converter.convertToDto(entity);
        result.setAccountTitle(accountEntity.getTitle());
        result.setCategoryTitle(categoryEntity.getTitle());
        return result;
    }

    /**
     * Updates operation and returns updated operation dto.
     * Throws {@link IncomeServiceOperationNotFoundException} if operation was modified or deleted
     *
     * @param dto                operation dto
     * @param oldOperationEntity old operation entity
     * @param accountEntity      operation account
     * @param categoryEntity     category account
     * @return operation dto
     */
    private OperationDto updateOperation(
            OperationDto dto, OperationEntity oldOperationEntity,
            AccountEntity accountEntity, CategoryEntity categoryEntity) {
        LOGGER.debug("Updating operation");
        OperationEntity entity = converter.convertToEntity(dto);
        int affectedRows = getDao().update(entity, oldOperationEntity);
        if (affectedRows != 1) {
            throwNotFoundException(entity.getId());
        }
        OperationDto result = converter.convertToDto(entity);
        result.setAccountTitle(accountEntity.getTitle());
        result.setCategoryTitle(categoryEntity.getTitle());
        return result;
    }

    /**
     * Deletes operation.
     * Throws {@link IncomeServiceOperationNotFoundException} if operation was modified or deleted
     *
     * @param entity operation entity
     */
    private void deleteOperation(OperationEntity entity) {
        LOGGER.debug("Deleting operation");
        int affectedRows = getDao().delete(entity);
        if (affectedRows != 1) {
            throwNotFoundException(entity.getId());
        }
    }

    private void updateBalance(BalanceEntity newEntity, BalanceEntity oldEntity) {
        Integer accountId = newEntity.getAccountId();
        LocalDate day = newEntity.getDay();
        LOGGER.debug("Updating balance for account id {} day {} equal to {}", accountId, day, newEntity.getAmount());
        int affectedRows = balanceDao.update(newEntity, oldEntity);
        if (affectedRows != 1) {
            throw new IncomeServiceBalanceNotFoundException(accountId, day, 0);
        }
    }

    private AccountEntity getAccount(Integer id) {
        AccountEntity result = accountDao.get(id);
        if (result == null) {
            LOGGER.error("Account with id {} not found", id);
            throw new IncomeServiceAccountNotFoundException(id);
        }
        return result;
    }

    private CategoryEntity getCategory(Integer id) {
        CategoryEntity result = categoryDao.get(id);
        if (result == null) {
            LOGGER.error("Category with id {} not found", id);
            throw new IncomeServiceCategoryNotFoundException(id);
        }
        return result;
    }

    @Override
    protected List<OperationDto> populateAdditionalFields(List<OperationDto> dtoList) {
        if (dtoList.isEmpty()) {
            return dtoList;
        }

        Set<Integer> accountIds = new HashSet<>();
        Set<Integer> categoryIds = new HashSet<>();
        for (OperationDto operationDto : dtoList) {
            accountIds.add(operationDto.getAccountId());
            categoryIds.add(operationDto.getCategoryId());
        }

        Map<Integer, AccountEntity> accountEntityMap = accountDao.getList(accountIds).stream()
                .collect(Collectors.toMap(AccountEntity::getId, accountEntity -> accountEntity));
        Map<Integer, CategoryEntity> categoryEntityMap = categoryDao.getList(categoryIds).stream()
                .collect(Collectors.toMap(CategoryEntity::getId, categoryEntity -> categoryEntity));

        for (OperationDto operationDto : dtoList) {
            operationDto.setAccountTitle(accountEntityMap.get(operationDto.getAccountId()).getTitle());
            operationDto.setCategoryTitle(categoryEntityMap.get(operationDto.getCategoryId()).getTitle());
        }

        return dtoList;
    }

    @Override
    protected OperationDto populateAdditionalFields(OperationDto dto) {
        AccountEntity accountEntity = accountDao.get(dto.getAccountId());
        CategoryEntity categoryEntity = categoryDao.get(dto.getCategoryId());
        dto.setAccountTitle(accountEntity.getTitle());
        dto.setCategoryTitle(categoryEntity.getTitle());
        return dto;
    }

    @Override
    protected void throwNotFoundException(Integer id) {
        LOGGER.error("Operation with id {} not found", id);
        throw new IncomeServiceOperationNotFoundException(id);
    }

}
