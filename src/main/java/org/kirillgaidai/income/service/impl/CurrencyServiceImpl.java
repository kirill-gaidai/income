package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dto.CurrencyDto;
import org.kirillgaidai.income.service.CurrencyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Override
    public List<CurrencyDto> getCurrencyList() {
        final List<CurrencyDto> result = new ArrayList<>();

        final CurrencyDto currencyDto1 = new CurrencyDto();
        currencyDto1.setId(1L);
        currencyDto1.setCode("USD");
        currencyDto1.setTitle("United states dollar");
        result.add(currencyDto1);

        final CurrencyDto currencyDto2 = new CurrencyDto();
        currencyDto2.setId(2L);
        currencyDto2.setCode("EUR");
        currencyDto2.setTitle("United states dollar");
        result.add(currencyDto2);

        return result;
    }

    @Override
    public CurrencyDto getCurrencyById(Long id) {
        final CurrencyDto result = new CurrencyDto();
        result.setId(1L);
        result.setCode("USD");
        result.setTitle("United states dollar");
        return result;
    }

}
