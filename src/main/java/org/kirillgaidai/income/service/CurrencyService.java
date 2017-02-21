package org.kirillgaidai.income.service;

import org.kirillgaidai.income.dto.CurrencyDto;

import java.util.List;

public interface CurrencyService {

    List<CurrencyDto> getCurrencyList();

    CurrencyDto getCurrencyById(final Integer id);

    void saveCurrency(final CurrencyDto categoryDto);

    void deleteCurrency(final Integer id);

}
