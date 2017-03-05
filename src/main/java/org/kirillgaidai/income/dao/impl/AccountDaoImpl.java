package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.AccountDao;
import org.kirillgaidai.income.entity.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AccountDaoImpl implements AccountDao {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public AccountDaoImpl(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<AccountEntity> getAccountList() {
        final String QUERY = "SELECT id, currency_id, title FROM accounts ORDER BY id";
        return namedParameterJdbcTemplate.query(QUERY, new HashMap<>(), getRowMapper());
    }

    @Override
    public AccountEntity getAccountById(final Integer id) {
        final String QUERY = "SELECT id, currency_id, title FROM accounts WHERE id = :id";
        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        try {
            return namedParameterJdbcTemplate.queryForObject(QUERY, params, getRowMapper());
        } catch (final EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int insertAccount(final AccountEntity accountEntity) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final String QUERY = "INSERT INTO accounts(currency_id, title) VALUES(:currency_id, :title)";
        
        final Map<String, Object> params = new HashMap<>();
        params.put("currency_id", accountEntity.getCurrencyId());
        params.put("title", accountEntity.getTitle());
        final SqlParameterSource sqlParameterSource = new MapSqlParameterSource(params);
        
        final int affectedRows = namedParameterJdbcTemplate.update(
                QUERY, sqlParameterSource, keyHolder, new String[] {"id"});
        accountEntity.setId(keyHolder.getKey().intValue());
        return affectedRows;
    }

    @Override
    public int updateAccount(AccountEntity accountEntity) {
        final String QUERY = "UPDATE accounts SET currency_id = :currency_id, title = :title WHERE id = :id";
        final Map<String, Object> params = new HashMap<>();
        params.put("id", accountEntity.getId());
        params.put("currency_id", accountEntity.getCurrencyId());
        params.put("title", accountEntity.getTitle());
        return namedParameterJdbcTemplate.update(QUERY, params);
    }

    @Override
    public int deleteAccount(final Integer id) {
        final String QUERY = "DELETE FROM accounts WHERE id = :id";
        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return namedParameterJdbcTemplate.update(QUERY, params);
    }

    private RowMapper<AccountEntity> getRowMapper() {
        return (resultSet, rowNum) -> {
            final AccountEntity accountEntity = new AccountEntity();
            accountEntity.setId(resultSet.getInt("id"));
            accountEntity.setCurrencyId(resultSet.getInt("currency_id"));
            accountEntity.setTitle(resultSet.getString("title"));
            return accountEntity;
        };
    }

}
