package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.CurrencyDao;
import org.kirillgaidai.income.entity.CurrencyEntity;
import org.kirillgaidai.income.exception.IncomeNotFoundDaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public CurrencyDaoImpl(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super();
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<CurrencyEntity> getCurrencyList() {
        final String QUERY = "SELECT id, code, title FROM currencies";
        return namedParameterJdbcTemplate.query(QUERY, new HashMap<>(), getRowMapper());
    }

    @Override
    public CurrencyEntity getCurrencyById(final Long id) {
        final String QUERY = "SELECT id, code, title FROM currencies WHERE :id = id";
        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        try {
            return namedParameterJdbcTemplate.queryForObject(QUERY, params, getRowMapper());
        } catch (final EmptyResultDataAccessException e) {
            return null;
        }
    }

    private RowMapper<CurrencyEntity> getRowMapper() {
        return (resultSet, rowNum) -> {
            CurrencyEntity currencyEntity = new CurrencyEntity();
            currencyEntity.setId(resultSet.getLong("id"));
            currencyEntity.setCode(resultSet.getString("code"));
            currencyEntity.setTitle(resultSet.getString("title"));
            return currencyEntity;
        };
    }

}
