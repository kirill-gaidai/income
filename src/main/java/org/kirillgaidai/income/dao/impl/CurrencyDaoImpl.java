package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.CurrencyDao;
import org.kirillgaidai.income.entity.CurrencyEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

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
        final String QUERY = "SELECT id, code, title FROM currencies ORDER BY id";
        return namedParameterJdbcTemplate.query(QUERY, new HashMap<>(), getRowMapper());
    }

    @Override
    public CurrencyEntity getCurrencyById(final Integer id) {
        final String QUERY = "SELECT id, code, title FROM currencies WHERE :id = id";
        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        try {
            return namedParameterJdbcTemplate.queryForObject(QUERY, params, getRowMapper());
        } catch (final EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int insertCurrency(final CurrencyEntity currencyEntity) {
        final String QUERY_KEY = "SELECT nextval('currencies_id_seq')";
        final Integer id = namedParameterJdbcTemplate.queryForObject(
                QUERY_KEY, new HashMap<>(), (resultSet, rowNum) -> resultSet.getInt(1));
        currencyEntity.setId(id);

        final String QUERY = "INSERT INTO currencies(id, code, title) VALUES(:id, :code, :title)";
        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("code", currencyEntity.getCode());
        params.put("title", currencyEntity.getTitle());
        return namedParameterJdbcTemplate.update(QUERY, params);
    }

    @Override
    public int updateCurrency(final CurrencyEntity currencyEntity) {
        final String QUERY = "UPDATE currencies SET code = :code, title = :title WHERE id = :id";
        final Map<String, Object> params = new HashMap<>();
        params.put("id", currencyEntity.getId());
        params.put("code", currencyEntity.getCode());
        params.put("title", currencyEntity.getTitle());
        return namedParameterJdbcTemplate.update(QUERY, params);
    }

    @Override
    public int deleteCurrency(final Integer id) {
        final String QUERY = "DELETE FROM currencies WHERE id = :id";
        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return namedParameterJdbcTemplate.update(QUERY, params);
    }

    private RowMapper<CurrencyEntity> getRowMapper() {
        return (resultSet, rowNum) -> {
            CurrencyEntity currencyEntity = new CurrencyEntity();
            currencyEntity.setId(resultSet.getInt("id"));
            currencyEntity.setCode(resultSet.getString("code"));
            currencyEntity.setTitle(resultSet.getString("title"));
            return currencyEntity;
        };
    }

}
