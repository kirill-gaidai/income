package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.CurrencyDao;
import org.kirillgaidai.income.dto.CurrencyDto;
import org.kirillgaidai.income.dto.CurrencyListDto;
import org.kirillgaidai.income.entity.CurrencyEntity;
import org.kirillgaidai.income.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Autowired
    private CurrencyDao currencyDao;

    @Override
    public CurrencyListDto getCurrencyList() {
        List<CurrencyDto> currencyDtoList = currencyDao
                .getCurrencyList()
                .stream()
                .map(this::convertToCurrencyDto)
                .collect(Collectors.toList());

        CurrencyListDto currencyListDto = new CurrencyListDto();
        currencyListDto.setCurrencyDtoList(currencyDtoList);
        return currencyListDto;
    }

    @Override
    public CurrencyDto getCurrencyById(final Long id) {
        return convertToCurrencyDto(currencyDao.getCurrencyById(id));
    }

    @Override
    public void createCurrency(final CurrencyDto currencyDto) {

    }

    private CurrencyDto convertToCurrencyDto(final CurrencyEntity currencyEntity) {
        final CurrencyDto currencyDto = new CurrencyDto();
        currencyDto.setId(currencyEntity.getId());
        currencyDto.setCode(currencyEntity.getCode());
        currencyDto.setTitle(currencyEntity.getTitle());
        return currencyDto;
    }

    private CurrencyEntity convertToCurrencyentity(final CurrencyDto currencyDto) {
        final CurrencyEntity currencyEntity = new CurrencyEntity();
        return currencyEntity;
    }

}
