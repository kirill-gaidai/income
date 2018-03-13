package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.kirillgaidai.income.dao.intf.IOperationDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.BalanceDto;
import org.kirillgaidai.income.service.exception.IncomeServiceDuplicateException;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.kirillgaidai.income.service.intf.IBalanceService;
import org.kirillgaidai.income.service.util.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BalanceService extends GenericService<BalanceDto, BalanceEntity> implements IBalanceService {

    final private static Logger LOGGER = LoggerFactory.getLogger(BalanceService.class);

    final private IAccountDao accountDao;
    final private IOperationDao operationDao;

    @Autowired
    public BalanceService(
            IBalanceDao balanceDao,
            IAccountDao accountDao,
            IOperationDao operationDao,
            ServiceHelper serviceHelper,
            IGenericConverter<BalanceEntity, BalanceDto> converter) {
        super(balanceDao, converter, serviceHelper);
        this.accountDao = accountDao;
        this.operationDao = operationDao;
    }

    private IBalanceDao getDao() {
        return (IBalanceDao) dao;
    }

    @Override
    @Transactional
    public List<BalanceDto> getList() {
        LOGGER.debug("Entering method");
        return super.getList();
    }

    @Override
    @Transactional
    public BalanceDto get(Integer accountId, LocalDate day) {
        LOGGER.debug("Entering method");
        validateAccountIdAndDay(accountId, day);

        // if balance exists for specified account and day, then we will return it
        BalanceEntity balanceEntity = getDao().get(accountId, day);
        if (balanceEntity != null) {
            return populateAdditionalFields(converter.convertToDto(balanceEntity));
        }

        // otherwise, if balance for specified day does not exist, but it exists before specified day,
        // then we will return it
        balanceEntity = getDao().getBefore(accountId, day);
        if (balanceEntity != null) {
            return populateAdditionalFields(new BalanceDto(accountId, null, day, balanceEntity.getAmount(), false));
        }
        // if balance exists only after specified day, then we will return it
        balanceEntity = getDao().getAfter(accountId, day);
        if (balanceEntity != null) {
            return populateAdditionalFields(new BalanceDto(accountId, null, day, balanceEntity.getAmount(), false));
        }
        // otherwise, returning zero
        return populateAdditionalFields(new BalanceDto(accountId, null, day, BigDecimal.ZERO, false));
    }

    @Override
    @Transactional
    public BalanceDto create(BalanceDto dto) {
        LOGGER.debug("Entering method");
        return createOrUpdateBalance(dto, 'C');
    }

    @Override
    @Transactional
    public BalanceDto update(BalanceDto dto) {
        LOGGER.debug("Entering method");
        return createOrUpdateBalance(dto, 'U');
    }

    @Override
    @Transactional
    public BalanceDto save(BalanceDto dto) {
        LOGGER.debug("Entering method");
        return createOrUpdateBalance(dto, 'S');
    }

    private BalanceDto createOrUpdateBalance(BalanceDto dto, char mode) {
        LOGGER.debug("Entering method");
        validateDto(dto);
        Integer accountId = dto.getAccountId();
        LocalDate day = dto.getDay();
        validateAccountIdAndDay(accountId, day);
        AccountEntity accountEntity = serviceHelper.getAccountEntity(accountId);
        BalanceEntity newBalanceEntity = converter.convertToEntity(dto);
        BalanceEntity oldBalanceEntity = getDao().get(accountId, day);

        if (oldBalanceEntity == null) {
            // If balance does not exist
            if (mode == 'U') {
                // Error if trying to update non-existing balance
                String message = String.format("Balance for account with id %d on %s not found", accountId, day);
                LOGGER.error(message);
                throw new IncomeServiceNotFoundException(message);
            }
            // Always setting fixed flag when creating manually
            serviceHelper.createBalanceEntity(new BalanceEntity(accountId, day, newBalanceEntity.getAmount(), true));
            return new BalanceDto(accountId, accountEntity.getTitle(), day, newBalanceEntity.getAmount(), true);
        }

        // After this point old balance exists

        if (mode == 'C') {
            // Error if trying to create balance over existing balance
            String message = String.format("Balance for account with id %d on %s already exists", accountId, day);
            LOGGER.error(message);
            throw new IncomeServiceDuplicateException(message);
        }

        if (newBalanceEntity.getManual() || operationDao.getCountByAccountIdAndDay(accountId, day) != 0) {
            // Updating balance if:
            // trying to update old balance with new one with fixed flag
            // or operations for account on day exist
            serviceHelper.updateBalanceEntity(newBalanceEntity, oldBalanceEntity);
            BalanceDto result = converter.convertToDto(newBalanceEntity);
            result.setAccountTitle(accountEntity.getTitle());
            return result;
        }

        BalanceEntity beforeBalanceEntity = getDao().getBefore(accountId, day);
        if (beforeBalanceEntity != null) {
            // Deleting balance if balance before exists
            serviceHelper.deleteBalanceEntity(oldBalanceEntity);
            return new BalanceDto(accountId, accountEntity.getTitle(), day, beforeBalanceEntity.getAmount(), false);
        }

        BalanceEntity afterBalanceEntity = getDao().getAfter(accountId, day);
        if (afterBalanceEntity != null && operationDao.getCountByAccountIdAndDay(
                afterBalanceEntity.getAccountId(), afterBalanceEntity.getDay()) != 0) {
            // Updating balance if no balance before and balance after with operations exist
            serviceHelper.updateBalanceEntity(new BalanceEntity(accountId, day, newBalanceEntity.getAmount(), true),
                    oldBalanceEntity);
            return new BalanceDto(accountId, accountEntity.getTitle(), day, newBalanceEntity.getAmount(), true);
        }

        // Deleting balance if no balance before and no balance after or balance after without operations exist
        serviceHelper.deleteBalanceEntity(oldBalanceEntity);
        return new BalanceDto(accountId, accountEntity.getTitle(), day, BigDecimal.ZERO, false);
    }

    @Override
    public void delete(Integer accountId, LocalDate day) {
        LOGGER.debug("Entering method");
        validateAccountIdAndDay(accountId, day);
        BalanceEntity oldBalanceEntity = getDao().get(accountId, day);
        // Simply exit if no balance for account on day was found
        if (oldBalanceEntity == null) {
            return;
        }
        // Error if operations for account on current day exist
        serviceHelper.checkAccountAndDayDependentOperations(accountId, day);
        // Can delete if no operations for account on current day exist and balance before exists
        if (getDao().getBefore(accountId, day) != null) {
            serviceHelper.deleteBalanceEntity(oldBalanceEntity);
            return;
        }
        BalanceEntity afterBalanceEntity = getDao().getAfter(accountId, day);
        // Error if balance before doesn't exist, and balance after exists, and operations for the next day exist
        if (afterBalanceEntity != null) {
            serviceHelper.checkAccountAndDayDependentOperations(
                    afterBalanceEntity.getAccountId(), afterBalanceEntity.getDay());
        }
        serviceHelper.deleteBalanceEntity(oldBalanceEntity);
    }

    @Override
    protected List<BalanceDto> populateAdditionalFields(List<BalanceDto> dtoList) {
        LOGGER.debug("Entering method");
        if (dtoList.isEmpty()) {
            return Collections.emptyList();
        }
        Set<Integer> accountIds = dtoList.stream().map(BalanceDto::getAccountId).collect(Collectors.toSet());
        Map<Integer, String> accountEntityMap = accountDao.getList(accountIds).stream()
                .collect(Collectors.toMap(AccountEntity::getId, AccountEntity::getTitle));
        dtoList.forEach(dto -> dto.setAccountTitle(accountEntityMap.get(dto.getAccountId())));
        return dtoList;
    }

    @Override
    protected BalanceDto populateAdditionalFields(BalanceDto dto) {
        LOGGER.debug("Entering method");
        dto.setAccountTitle(accountDao.get(dto.getAccountId()).getTitle());
        return dto;
    }

    private void validateAccountIdAndDay(Integer accountId, LocalDate day) {
        LOGGER.debug("Entering method");
        if (accountId == null) {
            LOGGER.error("Account id is null");
            throw new IllegalArgumentException("Account id is null");
        }
        if (day == null) {
            LOGGER.error("Day is null");
            throw new IllegalArgumentException("Day is null");
        }
    }

}
