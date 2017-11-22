package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.BalanceDto;
import org.kirillgaidai.income.service.exception.IncomeServiceAccountNotFoundException;
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
    final private ServiceHelper serviceHelper;

    @Autowired
    public BalanceService(
            IBalanceDao balanceDao,
            IAccountDao accountDao,
            ServiceHelper serviceHelper,
            IGenericConverter<BalanceEntity, BalanceDto> converter) {
        super(balanceDao, converter);
        this.accountDao = accountDao;
    }

    private IBalanceDao getDao() {
        return (IBalanceDao) dao;
    }

    @Override
    public BalanceDto get(Integer accountId, LocalDate day) {
        if (accountId == null || day == null) {
            throw new IllegalArgumentException("null");
        }

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
    public BalanceDto update(BalanceDto dto) {
        LOGGER.debug("Entering method");
        return super.update(dto);
    }

    @Override
    public BalanceDto save(BalanceDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("null");
        }

        Integer accountId = dto.getAccountId();
        LocalDate day = dto.getDay();
        if (day == null || accountId == null) {
            throw new IllegalArgumentException("null");
        }

        AccountEntity accountEntity = accountDao.get(accountId);
        if (accountEntity == null) {
            throw new IncomeServiceAccountNotFoundException(accountId);
        }

        BalanceEntity entity = converter.convertToEntity(dto);
        int affectedRows = dao.update(entity);
        if (affectedRows == 0) {
            dao.insert(entity);
        }

        dto.setAccountTitle(accountEntity.getTitle());
        return dto;
    }

    @Override
    protected List<BalanceDto> populateAdditionalFields(List<BalanceDto> dtoList) {
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
        dto.setAccountTitle(accountDao.get(dto.getAccountId()).getTitle());
        return dto;
    }

}
