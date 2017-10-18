package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
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

}
