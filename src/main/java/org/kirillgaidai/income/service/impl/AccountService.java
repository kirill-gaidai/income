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

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService extends SerialService<AccountDto, AccountEntity> implements IAccountService {

    final private ICurrencyDao currencyDao;

    @Autowired
    public AccountService(
            IAccountDao accountDao,
            ICurrencyDao currencyDao,
            IGenericConverter<AccountEntity, AccountDto> converter) {
        super(accountDao, converter);
        this.currencyDao = currencyDao;
    }

    @Override
    protected List<AccountDto> populateAdditionalFields(List<AccountDto> dtoList) {
        if (dtoList.isEmpty()) {
            return dtoList;
        }

        Set<Integer> currencyIds = dtoList.stream().map(AccountDto::getCurrencyId).collect(Collectors.toSet());
        Map<Integer, CurrencyEntity> currencyEntityMap = currencyDao.getList(currencyIds).stream()
                .collect(Collectors.toMap(CurrencyEntity::getId, currencyEntity -> currencyEntity));
        dtoList.forEach(accountDto -> {
            Integer currencyId = accountDto.getCurrencyId();
            if (!currencyEntityMap.containsKey(currencyId)) {
                throw new IncomeServiceCurrencyNotFoundException(currencyId);
            }
            CurrencyEntity currencyEntity = currencyEntityMap.get(currencyId);
            accountDto.setCurrencyCode(currencyEntity.getCode());
            accountDto.setCurrencyTitle(currencyEntity.getTitle());
        });
        return dtoList;
    }

    @Override
    protected AccountDto populateAdditionalFields(AccountDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("null");
        }

        CurrencyEntity currencyEntity = currencyDao.get(dto.getCurrencyId());
        dto.setCurrencyCode(currencyEntity.getCode());
        dto.setCurrencyTitle(currencyEntity.getTitle());
        return dto;
    }

    @Override
    protected void throwNotFoundException(Integer id) {
        throw new IncomeServiceAccountNotFoundException(id);
    }

}
