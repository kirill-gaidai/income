package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.kirillgaidai.income.service.intf.IAccountService;
import org.kirillgaidai.income.service.util.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService extends SerialService<AccountDto, AccountEntity> implements IAccountService {

    final private static Logger LOGGER = LoggerFactory.getLogger(AccountService.class);

    final private ICurrencyDao currencyDao;

    @Autowired
    public AccountService(
            IAccountDao accountDao,
            ICurrencyDao currencyDao,
            ServiceHelper serviceHelper,
            IGenericConverter<AccountEntity, AccountDto> converter) {
        super(accountDao, converter, serviceHelper);
        this.currencyDao = currencyDao;
    }

    @Override
    protected IAccountDao getDao() {
        return (IAccountDao) dao;
    }

    @Override
    @Transactional
    public List<AccountDto> getList() {
        LOGGER.debug("Entering method");
        return super.getList();
    }

    @Override
    @Transactional
    public List<AccountDto> getList(Set<Integer> ids) {
        LOGGER.debug("Entering method");
        return super.getList(ids);
    }

    @Override
    @Transactional
    public List<AccountDto> getList(Integer currencyId) {
        LOGGER.debug("Entering method");
        if (currencyId == null) {
            String message = "Currency id is null";
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
        CurrencyEntity currencyEntity = serviceHelper.getCurrencyEntity(currencyId);
        return getDao().getList(currencyId).stream().map(converter::convertToDto).peek(dto -> {
            dto.setCurrencyCode(currencyEntity.getCode());
            dto.setCurrencyTitle(currencyEntity.getTitle());
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AccountDto get(Integer id) {
        LOGGER.debug("Entering method");
        validateId(id);
        return populateAdditionalFields(converter.convertToDto(serviceHelper.getAccountEntity(id)));
    }

    @Override
    @Transactional
    public AccountDto create(AccountDto dto) {
        LOGGER.debug("Entering method");
        validateDto(dto);
        CurrencyEntity currencyEntity = serviceHelper.getCurrencyEntity(dto.getCurrencyId());
        AccountEntity entity = converter.convertToEntity(dto);
        serviceHelper.createAccountEntity(entity);
        AccountDto result = converter.convertToDto(entity);
        result.setCurrencyCode(currencyEntity.getCode());
        result.setCurrencyTitle(currencyEntity.getTitle());
        return result;
    }

    @Override
    @Transactional
    public AccountDto update(AccountDto dto) {
        LOGGER.debug("Entering method");
        validateDto(dto);
        Integer id = dto.getId();
        validateId(id);
        CurrencyEntity currencyEntity = serviceHelper.getCurrencyEntity(dto.getCurrencyId());
        AccountEntity oldEntity = serviceHelper.getAccountEntity(id);
        AccountEntity newEntity = converter.convertToEntity(dto);
        serviceHelper.updateAccountEntity(newEntity, oldEntity);
        AccountDto result = converter.convertToDto(newEntity);
        result.setCurrencyCode(currencyEntity.getCode());
        result.setCurrencyTitle(currencyEntity.getTitle());
        return result;
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        LOGGER.debug("Entering method");
        validateId(id);
        AccountEntity entity = serviceHelper.getAccountEntity(id);
        serviceHelper.checkAccountDependentOperations(id);
        serviceHelper.checkAccountDependentBalances(id);
        serviceHelper.deleteAccountEntity(entity);
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
            Integer currencyId = dto.getCurrencyId();
            if (currencyId == null) {
                String message = "Currency id is null";
                LOGGER.error(message);
                throw new IllegalStateException(message);
            }
            if (!entityMap.containsKey(currencyId)) {
                String message = String.format("Currency with id %d not found", currencyId);
                LOGGER.error(message);
                throw new IncomeServiceNotFoundException(message);
            }
            CurrencyEntity entity = entityMap.get(currencyId);
            dto.setCurrencyCode(entity.getCode());
            dto.setCurrencyTitle(entity.getTitle());
        }
        return dtoList;
    }

    @Override
    protected AccountDto populateAdditionalFields(AccountDto dto) {
        LOGGER.debug("Entering method");
        Integer currencyId = dto.getCurrencyId();
        if (currencyId == null) {
            String message = "Currency id is null";
            LOGGER.error(message);
            throw new IllegalStateException(message);
        }
        CurrencyEntity currencyEntity = serviceHelper.getCurrencyEntity(currencyId);
        dto.setCurrencyCode(currencyEntity.getCode());
        dto.setCurrencyTitle(currencyEntity.getTitle());
        return dto;
    }

    @Override
    protected void validateDto(AccountDto dto) {
        LOGGER.debug("Entering method");
        super.validateDto(dto);
        if (dto.getCurrencyId() == null) {
            String message = "Currency id is null";
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
    }

}
