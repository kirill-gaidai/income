package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.exception.IncomeServiceAccountNotFoundException;
import org.kirillgaidai.income.service.exception.IncomeServiceCurrencyNotFoundException;
import org.kirillgaidai.income.service.intf.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService implements IAccountService {

    final private IAccountDao accountDao;
    final private ICurrencyDao currencyDao;
    final private IGenericConverter<AccountEntity, AccountDto> accountConverter;

    @Autowired
    public AccountService(
            IAccountDao accountDao,
            ICurrencyDao currencyDao,
            IGenericConverter<AccountEntity, AccountDto> accountConverter) {
        this.accountDao = accountDao;
        this.currencyDao = currencyDao;
        this.accountConverter = accountConverter;
    }

    @Override
    public List<AccountDto> getDtoList() {
        return convertToDtoList(accountDao.getEntityList());
    }

    @Override
    public List<AccountDto> getDtoList(Set<Integer> ids) {
        return convertToDtoList(accountDao.getEntityList(ids));
    }

    @Override
    public AccountDto getDto(Integer id) {
        if (id == null) {
            throw new IncomeServiceAccountNotFoundException();
        }

        AccountEntity accountEntity = accountDao.getEntity(id);
        if (accountEntity == null) {
            throw new IncomeServiceAccountNotFoundException(id);
        }
        if (accountEntity.getCurrencyId() == null) {
            throw new IncomeServiceCurrencyNotFoundException();
        }

        CurrencyEntity currencyEntity = currencyDao.getEntity(accountEntity.getCurrencyId());
        if (currencyEntity == null) {
            throw new IncomeServiceCurrencyNotFoundException(accountEntity.getCurrencyId());
        }

        AccountDto dto = accountConverter.convertToDto(accountEntity);
        dto.setCurrencyCode(currencyEntity.getCode());
        dto.setCurrencyTitle(currencyEntity.getTitle());
        return dto;
    }

    @Override
    public void saveDto(AccountDto accountDto) {
        if (accountDto == null) {
            throw new IncomeServiceAccountNotFoundException();
        }

        AccountEntity accountEntity = accountConverter.convertToEntity(accountDto);
        if (accountDto.getId() == null) {
            accountDao.insertEntity(accountEntity);
            accountDto.setId(accountEntity.getId());
            return;
        }

        int affectedRows = accountDao.updateEntity(accountEntity);
        if (affectedRows != 1) {
            throw new IncomeServiceAccountNotFoundException(accountDto.getId());
        }
    }

    @Override
    public void deleteDto(Integer id) {
        if (id == null) {
            throw new IncomeServiceAccountNotFoundException();
        }

        int affectedRows = accountDao.deleteEntity(id);
        if (affectedRows != 1) {
            throw new IncomeServiceAccountNotFoundException(id);
        }
    }

    private List<AccountDto> convertToDtoList(List<AccountEntity> accountEntityList) {
        if (accountEntityList.isEmpty()) {
            return Collections.emptyList();
        }

        List<AccountDto> accountDtoList = accountEntityList.stream().map(accountConverter::convertToDto)
                .collect(Collectors.toList());
        Set<Integer> currencyIds = accountEntityList.stream().map(AccountEntity::getCurrencyId)
                .collect(Collectors.toSet());
        Map<Integer, CurrencyEntity> currencyEntityMap = currencyDao.getEntityList(currencyIds).stream()
                .collect(Collectors.toMap(CurrencyEntity::getId, currencyEntity -> currencyEntity));
        accountDtoList.forEach(accountDto -> {
            Integer currencyId = accountDto.getCurrencyId();
            if (!currencyEntityMap.containsKey(currencyId)) {
                throw new IncomeServiceCurrencyNotFoundException(currencyId);
            }
            CurrencyEntity currencyEntity = currencyEntityMap.get(currencyId);
            accountDto.setCurrencyCode(currencyEntity.getCode());
            accountDto.setCurrencyTitle(currencyEntity.getTitle());
        });
        return accountDtoList;
    }

}
