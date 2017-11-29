package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.BalanceDto;
import org.kirillgaidai.income.service.exception.IncomeServiceDuplicateException;
import org.kirillgaidai.income.service.intf.IBalanceService;
import org.kirillgaidai.income.service.util.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    public BalanceService(
            IBalanceDao balanceDao,
            IAccountDao accountDao,
            ServiceHelper serviceHelper,
            IGenericConverter<BalanceEntity, BalanceDto> converter) {
        super(balanceDao, converter, serviceHelper);
        this.accountDao = accountDao;
    }

    private IBalanceDao getDao() {
        return (IBalanceDao) dao;
    }

    @Override
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
    public BalanceDto create(BalanceDto dto) {
        LOGGER.debug("Entering method");

        validateDto(dto);

        Integer accountId = dto.getAccountId();
        LocalDate day = dto.getDay();

        validateAccountIdAndDay(accountId, day);

        AccountEntity accountEntity = serviceHelper.getAccountEntity(dto.getAccountId());

        if (getDao().get(accountId, day) != null) {
            String message = String.format("Balance for account with id %d on %s already exists", accountId, day);
            LOGGER.error(message);
            throw new IncomeServiceDuplicateException(message);
        }

        BalanceEntity entity = converter.convertToEntity(dto);
        serviceHelper.createBalanceEntity(entity);
        BalanceDto result = converter.convertToDto(entity);
        result.setAccountTitle(accountEntity.getTitle());
        return result;
    }

    @Override
    public BalanceDto update(BalanceDto dto) {
        LOGGER.debug("Entering method");

        validateDto(dto);
        validateAccountIdAndDay(dto.getAccountId(), dto.getDay());

        BalanceEntity oldBalanceEntity = serviceHelper.getBalanceEntity(dto.getAccountId(), dto.getDay(), 0);
        AccountEntity accountEntity = serviceHelper.getAccountEntity(dto.getAccountId());

        BalanceEntity entity = converter.convertToEntity(dto);
        serviceHelper.updateBalanceEntity(entity, oldBalanceEntity);
        BalanceDto result = converter.convertToDto(entity);

        result.setAccountTitle(accountEntity.getTitle());
        return result;
    }

    @Override
    public BalanceDto save(BalanceDto dto) {
        LOGGER.debug("Entering method");

        validateDto(dto);

        Integer accountId = dto.getAccountId();
        LocalDate day = dto.getDay();

        validateAccountIdAndDay(accountId, day);

        AccountEntity accountEntity = serviceHelper.getAccountEntity(accountId);
        BalanceEntity newBalanceEntity = converter.convertToEntity(dto);

        BalanceEntity oldBalanceEntity = getDao().get(accountId, day);
        if (oldBalanceEntity == null) {
            serviceHelper.createBalanceEntity(newBalanceEntity);
        } else {
            serviceHelper.updateBalanceEntity(newBalanceEntity, oldBalanceEntity);
        }

        BalanceDto result = converter.convertToDto(newBalanceEntity);
        result.setAccountTitle(accountEntity.getTitle());
        return result;
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
