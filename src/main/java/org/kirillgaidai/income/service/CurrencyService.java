package org.kirillgaidai.income.service;

import org.kirillgaidai.income.dto.CurrencyDto;
import org.kirillgaidai.income.dto.rest.CurrencyListDto;

import java.util.List;

public interface CurrencyService {

    List<CurrencyDto> getCurrencyList();

    CurrencyDto getCurrencyById(final Long id);

}
