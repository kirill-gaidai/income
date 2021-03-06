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
    public BalanceEntity getBefore(Integer accountId, LocalDate day) {
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
    public BalanceEntity getAfter(Integer accountId, LocalDate day) {
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

    @Override
    protected String getUpdateOptimisticSql() {
        return getUpdateSql() + " AND (account_id = :old_account_id) AND (day = :old_day)" +
                " AND (amount = :old_amount) AND (manual = :old_manual)";
    }

    @Override
    protected Map<String, Object> getUpdateOptimisticParamsMap(BalanceEntity newEntity, BalanceEntity oldEntity) {
        Map<String, Object> params = getUpdateParamsMap(newEntity);
        params.put("old_account_id", oldEntity.getAccountId());
        params.put("old_day", Date.valueOf(oldEntity.getDay()));
        params.put("old_amount", oldEntity.getAmount());
        params.put("old_manual", oldEntity.getManual());
        return params;
    }

    @Override
    protected String getDeleteOptimisticSql() {
        return "DELETE FROM balances " +
                "WHERE (account_id = :account_id) AND (day = :day) AND (amount = :amount) AND (manual = :manual)" +
                " AND ((SELECT COUNT(*) FROM operations WHERE (account_id = :account_id) AND (day = :day)) = 0) " +
                " AND (((SELECT COUNT(*) FROM balances WHERE (account_id = :account_id) AND (day < :day)) <> 0) OR" +
                " ((SELECT COUNT(*) FROM (SELECT account_id, day FROM balances WHERE (account_id = :account_id)" +
                " AND (day > :day) ORDER BY day LIMIT 1) AS b" +
                " INNER JOIN operations AS o ON (b.account_id = o.account_id) AND (b.day = o.day)) = 0))";
    }

    @Override
    public int getCountByAccountId(Integer accountId) {
        String sql = "SELECT COUNT(*) FROM balances WHERE account_id = :account_id";
        Map<String, Object> params = Collections.singletonMap("account_id", accountId);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

}
