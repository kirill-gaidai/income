package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.AccountDao;
import org.kirillgaidai.income.dao.CurrencyDao;
import org.kirillgaidai.income.dto.AccountDto;
import org.kirillgaidai.income.entity.AccountEntity;
import org.kirillgaidai.income.entity.CurrencyEntity;
import org.kirillgaidai.income.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountDao accountDao;
    private CurrencyDao currencyDao;

    @Autowired
    public AccountServiceImpl(final AccountDao accountDao, final CurrencyDao currencyDao) {
        this.accountDao = accountDao;
        this.currencyDao = currencyDao;
    }

    @Override
    public List<AccountDto> getAccountList() {
        final List<CurrencyEntity> currencyEntities = currencyDao.getCurrencyList();
        return accountDao
                .getAccountList()
                .stream()
                .map(accountEntity -> convertToAccountDto(accountEntity, convertToCurrencyEntityMap(currencyEntities)))
                .collect(Collectors.toList());
    }

    @Override
    public AccountDto getAccountById(final Integer id) {
        if (id == null) {
            return null;
        }

        final List<CurrencyEntity> currencyEntities = currencyDao.getCurrencyList();
        return convertToAccountDto(accountDao.getAccountById(id),  convertToCurrencyEntityMap(currencyEntities));
    }

    @Override
    public void saveAccount(final AccountDto accountDto) {
        final AccountEntity accountEntity = convertToAccountEntity(accountDto);
        if (accountDto.getId() != null) {
            accountDao.updateAccount(accountEntity);
            return;
        }
        accountDao.insertAccount(accountEntity);
        accountDto.setId(accountEntity.getId());
    }

    @Override
    public void deleteAccount(final Integer id) {
        accountDao.deleteAccount(id);
    }

    private AccountDto convertToAccountDto(
            final AccountEntity accountEntity, final Map<Integer, CurrencyEntity> currencyEntities) {
        if (accountEntity == null) {
            return null;
        }

        final AccountDto accountDto = new AccountDto();
        accountDto.setId(accountEntity.getId());
        accountDto.setTitle(accountEntity.getTitle());

        final Integer currencyId = accountEntity.getCurrencyId();
        final CurrencyEntity currencyEntity = currencyEntities.get(currencyId);
        accountDto.setCurrencyId(currencyId);
        accountDto.setCurrencyCode(currencyEntity.getCode());
        accountDto.setCurrencyTitle(currencyEntity.getTitle());

        return accountDto;
    }

    private AccountEntity convertToAccountEntity(final AccountDto accountDto) {
        final AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(accountDto.getId());
        accountEntity.setCurrencyId(accountDto.getCurrencyId());
        accountEntity.setTitle(accountDto.getTitle());
        return accountEntity;
    }

    private Map<Integer, CurrencyEntity> convertToCurrencyEntityMap(final List<CurrencyEntity> currencyEntities) {
        final Map<Integer, CurrencyEntity> currencyEntityMap = new HashMap<>();
        currencyEntities.forEach(currencyEntity -> currencyEntityMap.put(currencyEntity.getId(), currencyEntity));
        return currencyEntityMap;
    }

}
