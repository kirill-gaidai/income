package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.AccountDao;
import org.kirillgaidai.income.entity.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
        final String QUERY_KEY = "SELECT nextval('accounts_id_seq')";
        final Integer id = namedParameterJdbcTemplate.queryForObject(
                QUERY_KEY, new HashMap<>(), (resultSet, rowNum) -> resultSet.getInt(1));
        accountEntity.setId(id);

        final String QUERY = "INSERT INTO accounts(id, currency_id, title) VALUES(:id, :currency_id, :title)";
        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("currency_id", accountEntity.getCurrencyId());
        params.put("title", accountEntity.getTitle());
        return namedParameterJdbcTemplate.update(QUERY, params);
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
