package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.CurrencyDao;
import org.kirillgaidai.income.entity.CurrencyEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CurrencyDaoImpl implements CurrencyDao {

    @Override
    public List<CurrencyEntity> getCurrencyList() {
        final List<CurrencyEntity> result = new ArrayList<>();

        final CurrencyEntity currencyEntity1 = new CurrencyEntity();
        currencyEntity1.setId(1L);
        currencyEntity1.setCode("USD");
        currencyEntity1.setTitle("United states dollar");
        result.add(currencyEntity1);

        final CurrencyEntity currencyEntity2 = new CurrencyEntity();
        currencyEntity2.setId(2L);
        currencyEntity2.setCode("EUR");
        currencyEntity2.setTitle("United states dollar");
        result.add(currencyEntity2);

        return result;
    }

    @Override
    public CurrencyEntity getCurrencyById(final Long id) {
        final CurrencyEntity result = new CurrencyEntity();
        result.setId(1L);
        result.setCode("USD");
        result.setTitle("United states dollar");
        return result;
    }

}
