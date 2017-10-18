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
public class BalanceDao extends GenericDao<BalanceEntity> implements IBalanceDao {

    @Autowired
    public BalanceDao(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            RowMapper<BalanceEntity> rowMapper) {
        super(namedParameterJdbcTemplate, rowMapper);
    }

    @Override
    public List<BalanceEntity> getList(Set<Integer> accountIds, LocalDate lastDay) {
        if (accountIds.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = "SELECT b2.account_id, b2.day, b2.amount, b2.manual FROM balances AS b2 INNER JOIN (" +
                "SELECT account_id, MAX(day) AS day FROM balances " +
                "WHERE (account_id IN (:account_ids)) AND (day <= :last_day) GROUP BY account_id) AS b1 " +
                "ON (b2.account_id = b1.account_id) AND (b2.day = b1.day) ORDER BY b2.account_id";
        Map<String, Object> params = new HashMap<>();
        params.put("account_ids", accountIds);
        params.put("last_day", Date.valueOf(lastDay));
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    @Override
    public List<BalanceEntity> getList(Set<Integer> accountIds, LocalDate firstDay, LocalDate lastDay) {
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
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    @Override
    public BalanceEntity get(Integer accountId, LocalDate day) {
        String sql = "SELECT account_id, day, amount, MANUAL FROM balances " +
                "WHERE (account_id = :account_id) AND (day = :day)";
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);
        params.put("day", Date.valueOf(day));
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params, rowMapper);
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
            return namedParameterJdbcTemplate.queryForObject(sql, params, rowMapper);
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
            return namedParameterJdbcTemplate.queryForObject(sql, params, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int delete(Integer accountId, LocalDate day) {
        String sql = "DELETE FROM balances WHERE (account_id = :account_id) AND (day = :day)";
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);
        params.put("day", Date.valueOf(day));
        return namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    protected String getGetListSql() {
        return "SELECT account_id, day, amount, manual FROM balances ORDER BY account_id, day DESC";
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO balances(account_id, day, amount, manual) " +
                "VALUES(:account_id, :day, :amount, :manual)";
    }

    @Override
    protected Map<String, Object> getInsertParamsMap(BalanceEntity entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", entity.getAccountId());
        params.put("day", Date.valueOf(entity.getDay()));
        params.put("amount", entity.getAmount());
        params.put("manual", entity.getManual());
        return params;
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE balances SET amount = :amount, manual = :manual " +
                "WHERE (account_id = :account_id) AND (day = :day)";
    }

    @Override
    protected Map<String, Object> getUpdateParamsMap(BalanceEntity entity) {
        return getInsertParamsMap(entity);
    }

}
