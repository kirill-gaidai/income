package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.exception.IncomeServiceAccountNotFoundException;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.kirillgaidai.income.service.intf.IAccountService;
import org.kirillgaidai.income.service.util.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class AccountService extends SerialService<AccountDto, AccountEntity> implements IAccountService {

    final private static Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    final private ICurrencyDao currencyDao;
    final private ServiceHelper serviceHelper;

    @Autowired
    public AccountService(
            IAccountDao accountDao,
            ICurrencyDao currencyDao,
            ServiceHelper serviceHelper,
            IGenericConverter<AccountEntity, AccountDto> converter) {
        super(accountDao, converter);
        this.currencyDao = currencyDao;
        this.serviceHelper = serviceHelper;
    }

    @Override
    public AccountDto create(AccountDto dto) {
        LOGGER.debug("Entering method");
        validateDto(dto);
        CurrencyEntity currencyEntity = serviceHelper.getCurrencyEntity(dto.getCurrencyId());
        AccountDto result = super.create(dto);
        result.setCurrencyCode(currencyEntity.getCode());
        result.setCurrencyTitle(currencyEntity.getTitle());
        return result;
    }

    @Override
    public AccountDto update(AccountDto dto) {
        LOGGER.debug("Entering method");
        validateDtoWithId(dto);
        CurrencyEntity currencyEntity = serviceHelper.getCurrencyEntity(dto.getCurrencyId());
        AccountDto result = super.update(dto);
        result.setCurrencyCode(currencyEntity.getCode());
        result.setCurrencyTitle(currencyEntity.getTitle());
        return result;
    }

    @Override
    public AccountDto save(AccountDto dto) {
        LOGGER.debug("Entering method");
        validateDto(dto);
        CurrencyEntity currencyEntity = serviceHelper.getCurrencyEntity(dto.getCurrencyId());
        AccountDto result = super.save(dto);
        result.setCurrencyCode(currencyEntity.getCode());
        result.setCurrencyTitle(currencyEntity.getTitle());
        return result;
    }

    @Override
    public void delete(Integer id) {
        LOGGER.debug("Entering method");
        validateId(id);
        serviceHelper.checkAccountDependentOperations(id);
        serviceHelper.checkAccountDependentBalances(id);
        super.delete(id);
    }

    @Override
    protected List<AccountDto> populateAdditionalFields(List<AccountDto> dtoList) {
        LOGGER.debug("Entering method");
        if (dtoList.isEmpty()) {
            return dtoList;
        }
        Set<Integer> ids = dtoList.stream().map(AccountDto::getCurrencyId).collect(Collectors.toSet());
        Map<Integer, CurrencyEntity> entityMap = currencyDao.getList(ids).stream()
                .collect(Collectors.toMap(CurrencyEntity::getId, currencyEntity -> currencyEntity));
        for (AccountDto dto : dtoList) {
            CurrencyEntity entity = entityMap.get(dto.getCurrencyId());
            dto.setCurrencyCode(entity.getCode());
            dto.setCurrencyTitle(entity.getTitle());
        }
        return dtoList;
    }

    @Override
    protected AccountDto populateAdditionalFields(AccountDto dto) {
        LOGGER.debug("Entering method");
        CurrencyEntity currencyEntity = currencyDao.get(dto.getCurrencyId());
        dto.setCurrencyCode(currencyEntity.getCode());
        dto.setCurrencyTitle(currencyEntity.getTitle());
        return dto;
    }

    @Override
    protected void throwNotFoundException(Integer id) {
        LOGGER.debug("Entering method");
        throw new IncomeServiceAccountNotFoundException(id);
    }

    @Override
    protected void validateDto(AccountDto dto) {
        LOGGER.debug("Entering method");
        super.validateDto(dto);
        if (dto.getCurrencyId() == null) {
            LOGGER.error("Currency id is null");
            throw new IllegalArgumentException("Currency id is null");
        }
    }

    @Override
    protected Supplier<IncomeServiceNotFoundException> getNotFoundException() {
        return null;
    }

}
