package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AccountDao extends SerialDao<AccountEntity> implements IAccountDao {

    @Autowired
    public AccountDao(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            RowMapper<AccountEntity> rowMapper) {
        super(namedParameterJdbcTemplate, rowMapper);
    }

    @Override
    protected String getGetListSql() {
        return "SELECT id, currency_id, sort, title FROM accounts ORDER BY sort";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE accounts SET currency_id = :currency_id, sort = :sort, title = :title WHERE id = :id";
    }

    @Override
    protected String getUpdateOptimisticSql() {
        return getUpdateSql() + " AND (currency_id = :old_currency_id) AND (sort = :old_sort) AND (title = :old_title)";
    }

    @Override
    protected Map<String, Object> getUpdateOptimisticParamsMap(AccountEntity newEntity, AccountEntity oldEntity) {
        Map<String, Object> params = getUpdateParamsMap(newEntity);
        params.put("old_currency_id", oldEntity.getCurrencyId());
        params.put("old_sort", oldEntity.getSort());
        params.put("old_title", oldEntity.getTitle());
        return params;
    }

    @Override
    protected String getGetListByIdsSql() {
        return "SELECT id, currency_id, sort, title FROM accounts WHERE id IN (:ids) ORDER BY sort";
    }

    @Override
    protected String getGetByIdSql() {
        return "SELECT id, currency_id, sort, title FROM accounts WHERE id = :id";
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO accounts(currency_id, sort, title) VALUES(:currency_id, :sort, :title)";
    }

    @Override
    protected Map<String, Object> getInsertParamsMap(AccountEntity entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("currency_id", entity.getCurrencyId());
        params.put("sort", entity.getSort());
        params.put("title", entity.getTitle());
        return params;
    }

    @Override
    protected String getDeleteByIdSql() {
        return "DELETE FROM accounts WHERE id = :id";
    }

    @Override
    protected String getDeleteOptimisticSql() {
        return "DELETE FROM accounts " +
                "WHERE (id = :id) AND (currency_id = :currency_id) AND (sort = :sort) AND (title = :title) " +
                "AND (0 = (SELECT COUNT(*) FROM operations WHERE account_id = :id)) " +
                "AND (0 = (SELECT COUNT(*) FROM balances WHERE account_id = :id))";
    }

    @Override
    protected Map<String, Object> getDeleteOptimisticParamsMap(AccountEntity entity) {
        return getUpdateParamsMap(entity);
    }

    @Override
    public int getCountByCurrencyId(Integer currencyId) {
        String sql = "SELECT COUNT(*) FROM accounts WHERE currency_id = :currency_id";
        Map<String, Object> params = Collections.singletonMap("currency_id", currencyId);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    @Override
    public List<AccountEntity> getList(Integer currencyId) {
        String sql = "SELECT id, currency_id, sort, title FROM accounts WHERE currency_id = :currency_id ORDER BY sort";
        Map<String, Object> params = Collections.singletonMap("currency_id", currencyId);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

}
