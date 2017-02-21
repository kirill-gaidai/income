package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.CurrencyDao;
import org.kirillgaidai.income.dto.CurrencyDto;
import org.kirillgaidai.income.entity.CurrencyEntity;
import org.kirillgaidai.income.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private CurrencyDao currencyDao;

    @Autowired
    public CurrencyServiceImpl(final CurrencyDao currencyDao) {
        this.currencyDao = currencyDao;
    }

    @Override
    public List<CurrencyDto> getCurrencyList() {
        return currencyDao.getCurrencyList().stream().map(this::convertToCurrencyDto).collect(Collectors.toList());
    }

    @Override
    public CurrencyDto getCurrencyById(final Integer id) {
        if (id == null) {
            return null;
        }
        return convertToCurrencyDto(currencyDao.getCurrencyById(id));
    }

    @Override
    public void saveCurrency(final CurrencyDto currencyDto) {
        final CurrencyEntity currencyEntity = convertToCurrencyEntity(currencyDto);
        if (currencyEntity.getId() != null) {
            currencyDao.updateCurrency(currencyEntity);
            return;
        }
        currencyDao.insertCurrency(currencyEntity);
        currencyDto.setId(currencyEntity.getId());
    }

    @Override
    public void deleteCurrency(final Integer id) {
        currencyDao.deleteCurrency(id);
    }

    private CurrencyDto convertToCurrencyDto(final CurrencyEntity currencyEntity) {
        if (currencyEntity == null) {
            return null;
        }
        final CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setId(currencyEntity.getId());
        currencyDto.setCode(currencyEntity.getCode());
        currencyDto.setTitle(currencyEntity.getTitle());
        return currencyDto;
    }

    private CurrencyEntity convertToCurrencyEntity(final CurrencyDto currencyDto) {
        if (currencyDao == null) {
            return null;
        }
        final CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setId(currencyDto.getId());
        currencyEntity.setCode(currencyDto.getCode());
        currencyEntity.setTitle(currencyDto.getTitle());
        return currencyEntity;
    }

}
