package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.BalanceDao;
import org.kirillgaidai.income.entity.BalanceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Repository
public class BalanceDaoImpl implements BalanceDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public BalanceDaoImpl(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public BalanceEntity getEntityByIds(final Integer accountId, final Date day) {
        final String query = "SELECT account_id, day, amount, manual FROM balances " +
                "WHERE (account_id = :account_id) AND (day = :day)";
        final Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);
        params.put("day", day);
        try {
            return namedParameterJdbcTemplate.queryForObject(query, params, getRowMapper());
        } catch (final EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public BalanceEntity getPreviousEntityByIds(final Integer accountId, final Date day) {
        final String query = "SELECT account_id, day, amount, manual FROM balances " +
                "WHERE (account_id = :account_id) AND (day < :day)";
        final Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);
        params.put("day", day);
        try {
            return namedParameterJdbcTemplate.queryForObject(query, params, getRowMapper());
        } catch (final EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int insertEntity(final BalanceEntity balanceEntity) {
        final String QUERY = "INSERT INTO balances(account_id, day, amount, manual) " +
                "VALUES(:account_id, :day, :amount, :manual)";
        final Map<String, Object> params = new HashMap<>();
        params.put("account_id", balanceEntity.getAccountId());
        params.put("day", balanceEntity.getDay());
        params.put("amount", balanceEntity.getAmount());
        params.put("manual", balanceEntity.getManual());
        return namedParameterJdbcTemplate.update(QUERY, params);
    }

    @Override
    public int updateEntity(final BalanceEntity balanceEntity) {
        final String QUERY = "UPDATE balances SET amount = :amount, manual = :manual " +
                "WHERE (account_id = :account_id) AND (day = :day)";
        final Map<String, Object> params = new HashMap<>();
        params.put("account_id", balanceEntity.getAccountId());
        params.put("day", balanceEntity.getDay());
        params.put("amount", balanceEntity.getAmount());
        params.put("manual", balanceEntity.getManual());
        return namedParameterJdbcTemplate.update(QUERY, params);
    }

    @Override
    public int deleteEntity(final Integer accountId, final Date day) {
        final String QUERY = "DELETE FROM balances WHERE (account_id = :account_id) AND (day = :day)";
        final Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);
        params.put("day", day);
        return namedParameterJdbcTemplate.update(QUERY, params);
    }

    private RowMapper<BalanceEntity> getRowMapper() {
        return (resultSet, rowNum) -> {
            final BalanceEntity entity = new BalanceEntity();
            entity.setAccountId(resultSet.getInt("account_id"));
            entity.setDay(resultSet.getDate("day"));
            entity.setAmount(resultSet.getBigDecimal("amount"));
            entity.setManual(resultSet.getBoolean("manual"));
            return entity;
        };
    }

}
