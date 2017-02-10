package org.kirillgaidai.income.service;

import org.kirillgaidai.income.dto.CurrencyDto;
import org.kirillgaidai.income.dto.CurrencyListDto;

import java.util.List;

public interface CurrencyService {

    CurrencyListDto getCurrencyList();

    CurrencyDto getCurrencyById(final Long id);

    void createCurrency(final CurrencyDto currencyDto);

}
