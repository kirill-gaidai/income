package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class BalanceDao implements IBalanceDao {

    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    final private RowMapper<BalanceEntity> balanceEntityRowMapper;

    @Autowired
    public BalanceDao(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            RowMapper<BalanceEntity> balanceEntityRowMapper) {
        super();
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.balanceEntityRowMapper = balanceEntityRowMapper;
    }

    @Override
    public List<BalanceEntity> getEntityList() {
        String sql = "SELECT account_id, day, amount, manual FROM balances ORDER BY account_id, day DESC";
        return namedParameterJdbcTemplate.query(sql, Collections.emptyMap(), balanceEntityRowMapper);
    }

    @Override
    public List<BalanceEntity> getEntityList(Set<Integer> accountIds, LocalDate lastDay) {
        if (accountIds.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = "SELECT b2.account_id, b2.day, b2.amount, b2.manual FROM balances AS b2 INNER JOIN (" +
                "SELECT account_id, MAX(day) AS day FROM balances " +
                "WHERE (account_id IN (:account_ids)) AND (day <= :last_day) GROUP BY account_id) AS b1 " +
                "ON (b2.account_id = b1.account_id) AND (b2.day = b1.day) ORDER BY b2.account_id";
        Map<String, Object> params = new HashMap<>();
        params.put("account_ids", accountIds);
        params.put("last_day", lastDay);
        return namedParameterJdbcTemplate.query(sql, params, balanceEntityRowMapper);
    }

    @Override
    public List<BalanceEntity> getEntityList(Set<Integer> accountIds, LocalDate firstDay, LocalDate lastDay) {
        if (accountIds.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = "SELECT account_id, day, amount, manual FROM balances " +
                "WHERE (account_id IN (:account_ids)) AND (day BETWEEN :first_day AND :last_day) " +
                "ORDER BY account_id, day";
        Map<String, Object> params = new HashMap<>();
        params.put("account_ids", accountIds);
        params.put("first_day", Date.valueOf(firstDay));
        params.put("last_day", Date.valueOf(lastDay));
        return namedParameterJdbcTemplate.query(sql, params, balanceEntityRowMapper);
    }

    @Override
    public BalanceEntity getEntity(Integer accountId, LocalDate day) {
        String sql = "SELECT account_id, day, amount, MANUAL FROM balances " +
                "WHERE (account_id = :account_id) AND (day = :day)";
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);
        params.put("day", Date.valueOf(day));
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params, balanceEntityRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public BalanceEntity getEntityBefore(Integer accountId, LocalDate day) {
        String sql = "SELECT account_id, day, amount, MANUAL FROM balances " +
                "WHERE (account_id = :account_id) AND (day < :day) ORDER BY day DESC LIMIT 1";
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);
        params.put("day", Date.valueOf(day));
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params, balanceEntityRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public BalanceEntity getEntityAfter(Integer accountId, LocalDate day) {
        String sql = "SELECT account_id, day, amount, MANUAL FROM balances " +
                "WHERE (account_id = :account_id) AND (day > :day) ORDER BY day LIMIT 1";
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);
        params.put("day", Date.valueOf(day));
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params, balanceEntityRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int insertEntity(BalanceEntity entity) {
        String sql = "INSERT INTO balances(account_id, day, amount, manual) " +
                "VALUES(:account_id, :day, :amount, :manual)";
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", entity.getAccountId());
        params.put("day", Date.valueOf(entity.getDay()));
        params.put("amount", entity.getAmount());
        params.put("manual", entity.getManual());
        return namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public int updateEntity(BalanceEntity entity) {
        String sql = "UPDATE balances SET amount = :amount, manual = :manual " +
                "WHERE (account_id = :account_id) AND (day = :day)";
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", entity.getAccountId());
        params.put("day", Date.valueOf(entity.getDay()));
        params.put("amount", entity.getAmount());
        params.put("manual", entity.getManual());
        return namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public int deleteEntity(Integer accountId, LocalDate day) {
        String sql = "DELETE FROM balances WHERE (account_id = :account_id) AND (day = :day)";
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);
        params.put("day", Date.valueOf(day));
        return namedParameterJdbcTemplate.update(sql, params);
    }

}
