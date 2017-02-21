package org.kirillgaidai.income.dao;

import org.kirillgaidai.income.entity.CurrencyEntity;

import java.util.List;

public interface CurrencyDao {

    List<CurrencyEntity> getCurrencyList();

    CurrencyEntity getCurrencyById(final Integer id);

    int insertCurrency(final CurrencyEntity currencyEntity);

    int updateCurrency(final CurrencyEntity currencyEntity);

    int deleteCurrency(final Integer id);

}
