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
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.kirillgaidai.income.service.intf.IOperationService;
import org.kirillgaidai.income.service.util.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
            ServiceHelper serviceHelper,
            IGenericConverter<OperationEntity, OperationDto> converter) {
        super(operationDao, converter, serviceHelper);
        this.accountDao = accountDao;
        this.balanceDao = balanceDao;
        this.categoryDao = categoryDao;
    }

    protected IOperationDao getDao() {
        return (IOperationDao) dao;
    }

    @Override
    @Transactional
    public List<OperationDto> getList(Set<Integer> ids) {
        LOGGER.debug("Entering method");
        return super.getList(ids);
    }

    @Override
    @Transactional
    public List<OperationDto> getList() {
        LOGGER.debug("Entering method");
        return super.getList();
    }

    @Override
    @Transactional
    public List<OperationDto> getList(
            Set<Integer> accountIds, Set<Integer> categoryIds, LocalDate firstDay, LocalDate lastDay) {
        LOGGER.debug("Entering method");
        return populateAdditionalFields(getDao().getList(accountIds, categoryIds, firstDay, lastDay).stream()
                .map(converter::convertToDto).collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public OperationDto get(Integer id) {
        LOGGER.debug("Entering method");
        validateId(id);
        return populateAdditionalFields(converter.convertToDto(serviceHelper.getOperationEntity(id)));
    }

    @Override
    @Transactional
    public OperationDto create(OperationDto dto) {
        LOGGER.debug("Entering method");
        validateDto(dto);

        Integer accountId = dto.getAccountId();
        Integer categoryId = dto.getCategoryId();
        LocalDate thisDay = dto.getDay();
        BigDecimal amount = dto.getAmount();

        AccountEntity accountEntity = serviceHelper.getAccountEntity(accountId);
        CategoryEntity categoryEntity = serviceHelper.getCategoryEntity(categoryId);

        BalanceEntity thisBalanceEntity = balanceDao.get(accountId, thisDay);
        BalanceEntity prevBalanceEntity = balanceDao.getBefore(accountId, thisDay);
        if (thisBalanceEntity == null && prevBalanceEntity == null) {
            // If no operations for account at this day or before, then error
            String message =
                    String.format("Balance for account with id %d at or before %s not found", accountId, thisDay);
            LOGGER.error(message);
            throw new IncomeServiceNotFoundException(message);
        }

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

        if (thisBalanceEntity.getManual()) {
            // If this balance fixed, only balance for previous day may be recalculated
            BigDecimal newAmount = prevAmount.add(amount);
            BalanceEntity newBalanceEntity = new BalanceEntity(accountId, prevDay, newAmount, false);
            serviceHelper.updateBalanceEntity(newBalanceEntity, prevBalanceEntity);
            return insertOperation(dto, accountEntity, categoryEntity);
        }

        BigDecimal newAmount = thisAmount.subtract(amount);
        BalanceEntity newBalanceEntity = new BalanceEntity(accountId, thisDay, newAmount, false);
        serviceHelper.updateBalanceEntity(newBalanceEntity, thisBalanceEntity);
        return insertOperation(dto, accountEntity, categoryEntity);
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
        serviceHelper.createOperationEntity(entity);
        OperationDto result = converter.convertToDto(entity);

        result.setAccountTitle(accountEntity.getTitle());
        result.setCategoryTitle(categoryEntity.getTitle());
        return result;
    }

    @Override
    @Transactional
    public OperationDto update(OperationDto dto) {
        LOGGER.trace("Entering method");
        validateDto(dto);

        Integer id = dto.getId();
        Integer categoryId = dto.getCategoryId();
        BigDecimal newAmount = dto.getAmount();

        validateId(id);

        OperationEntity oldOperationEntity = serviceHelper.getOperationEntity(id);

        Integer accountId = oldOperationEntity.getAccountId();
        LocalDate thisDay = oldOperationEntity.getDay();

        AccountEntity accountEntity = serviceHelper.getAccountEntity(accountId);
        CategoryEntity categoryEntity = serviceHelper.getCategoryEntity(categoryId);
        BalanceEntity thisBalanceEntity = serviceHelper.getBalanceEntity(accountId, thisDay, 0);
        BalanceEntity prevBalanceEntity = serviceHelper.getBalanceEntity(accountId, thisDay, -1);

        LocalDate prevDay = prevBalanceEntity.getDay();
        BigDecimal oldAmount = oldOperationEntity.getAmount();

        if (prevBalanceEntity.getManual() && thisBalanceEntity.getManual()) {
            return updateOperation(dto, oldOperationEntity, accountEntity, categoryEntity);
        }

        if (thisBalanceEntity.getManual()) {
            BigDecimal newBalanceAmount = prevBalanceEntity.getAmount().subtract(oldAmount).add(newAmount);
            BalanceEntity newBalanceEntity = new BalanceEntity(accountId, prevDay, newBalanceAmount, false);
            serviceHelper.updateBalanceEntity(newBalanceEntity, prevBalanceEntity);
            return updateOperation(dto, oldOperationEntity, accountEntity, categoryEntity);
        }

        BigDecimal newBalanceAmount = thisBalanceEntity.getAmount().add(oldAmount).subtract(newAmount);
        BalanceEntity newBalanceEntity = new BalanceEntity(accountId, thisDay, newBalanceAmount, false);
        serviceHelper.updateBalanceEntity(newBalanceEntity, thisBalanceEntity);
        return updateOperation(dto, oldOperationEntity, accountEntity, categoryEntity);
    }

    /**
     * Updates operation and returns updated operation dto.
     *
     * @param operationDto       operation dto
     * @param oldOperationEntity old operation entity
     * @param accountEntity      operation account
     * @param categoryEntity     category account
     * @return operation dto
     */
    private OperationDto updateOperation(
            OperationDto operationDto, OperationEntity oldOperationEntity,
            AccountEntity accountEntity, CategoryEntity categoryEntity) {
        LOGGER.debug("Entering method");

        OperationEntity newOperationEntity = converter.convertToEntity(operationDto);
        newOperationEntity.setAccountId(oldOperationEntity.getAccountId());
        newOperationEntity.setDay(oldOperationEntity.getDay());
        serviceHelper.updateOperationEntity(newOperationEntity, oldOperationEntity);
        OperationDto result = converter.convertToDto(newOperationEntity);

        result.setAccountTitle(accountEntity.getTitle());
        result.setCategoryTitle(categoryEntity.getTitle());
        return result;
    }

    /**
     * Deletes operation and updates account balance if needed
     *
     * @param id - operation id
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        LOGGER.debug("Entering method");
        validateId(id);

        OperationEntity operationEntity = serviceHelper.getOperationEntity(id);

        Integer accountId = operationEntity.getAccountId();
        LocalDate thisDay = operationEntity.getDay();
        BigDecimal amount = operationEntity.getAmount();

        BalanceEntity thisBalanceEntity = serviceHelper.getBalanceEntity(accountId, thisDay, 0);
        BalanceEntity prevBalanceEntity = serviceHelper.getBalanceEntity(accountId, thisDay, -1);

        LocalDate prevDay = prevBalanceEntity.getDay();

        // At this point this and previous balances exist

        if (thisBalanceEntity.getManual() && prevBalanceEntity.getManual()) {
            // If this and previous balances fixed, then simply deleting operation
            serviceHelper.deleteOperationEntity(operationEntity);
            return;
        }

        // At this point only one of two balances may be fixed

        if (thisBalanceEntity.getManual()) {
            // If this balance is fixed then updating previous one
            BigDecimal newAmount = prevBalanceEntity.getAmount().subtract(amount);
            BalanceEntity newBalanceEntity = new BalanceEntity(accountId, prevDay, newAmount, false);
            serviceHelper.updateBalanceEntity(newBalanceEntity, prevBalanceEntity);
            serviceHelper.deleteOperationEntity(operationEntity);
            return;
        }

        int operationCount = getDao().getCountByAccountIdAndDay(accountId, thisDay);
        if (operationCount != 1) {
            // Updating this balance if it is not last operation for account on day
            BigDecimal newAmount = thisBalanceEntity.getAmount().add(amount);
            BalanceEntity newBalanceEntity = new BalanceEntity(accountId, thisDay, newAmount, false);
            serviceHelper.updateBalanceEntity(newBalanceEntity, thisBalanceEntity);
        }
        serviceHelper.deleteOperationEntity(operationEntity);
        if (operationCount == 1) {
            // Deleting balance if it is last operation for account on day
            serviceHelper.deleteBalanceEntity(thisBalanceEntity);
        }
    }

    @Override
    protected List<OperationDto> populateAdditionalFields(List<OperationDto> dtoList) {
        LOGGER.debug("Entering method");
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
        LOGGER.debug("Entering method");
        Integer accountId = dto.getAccountId();
        Integer categoryId = dto.getCategoryId();
        if (accountId == null) {
            String message = "Account id is null";
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
        if (categoryId == null) {
            String message = "Category id is null";
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
        AccountEntity accountEntity = serviceHelper.getAccountEntity(accountId);
        CategoryEntity categoryEntity = serviceHelper.getCategoryEntity(categoryId);
        dto.setAccountTitle(accountEntity.getTitle());
        dto.setCategoryTitle(categoryEntity.getTitle());
        return dto;
    }

}
