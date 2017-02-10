package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.CurrencyDao;
import org.kirillgaidai.income.entity.CurrencyEntity;
import org.kirillgaidai.income.exception.IncomeNotFoundDaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CurrencyDaoImpl implements CurrencyDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private List<CurrencyEntity> storage;
    private long nextId;

    @Autowired
    public CurrencyDaoImpl() {
        super();
        storage = new ArrayList<>();

        final CurrencyEntity currencyEntity1 = new CurrencyEntity();
        currencyEntity1.setId(1L);
        currencyEntity1.setCode("USD");
        currencyEntity1.setTitle("United states dollar");
        storage.add(currencyEntity1);

        final CurrencyEntity currencyEntity2 = new CurrencyEntity();
        currencyEntity2.setId(2L);
        currencyEntity2.setCode("EUR");
        currencyEntity2.setTitle("United states dollar");
        storage.add(currencyEntity2);

        nextId = 3L;
    }

    @Override
    public List<CurrencyEntity> getCurrencyList() {
        return storage;
    }

    @Override
    public CurrencyEntity getCurrencyById(final Long id) {
        final String QUERY = "SELECT id, code, title FROM currencies WHERE :id = id";

        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        return namedParameterJdbcTemplate.queryForObject(QUERY, params, (resultSet, rowNum) -> {
            CurrencyEntity currencyEntity = new CurrencyEntity();
            currencyEntity.setId(resultSet.getLong("id"));
            currencyEntity.setCode(resultSet.getString("code"));
            currencyEntity.setTitle(resultSet.getString("title"));
            return currencyEntity;
        });
    }

    @Override
    public void createCurrency(final CurrencyEntity currencyEntity) {
        currencyEntity.setId(nextId);
        nextId++;
        storage.add(currencyEntity);
    }

}
