package org.kirillgaidai.income.dao;

import org.kirillgaidai.income.entity.CurrencyEntity;

import java.util.List;

public interface CurrencyDao {

    List<CurrencyEntity> getCurrencyList();

    CurrencyEntity getCurrencyById(final Long id);

    void createCurrency(final CurrencyEntity currencyEntity);

}
