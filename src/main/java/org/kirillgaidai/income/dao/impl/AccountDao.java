package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.util.DaoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class AccountDao implements IAccountDao {

    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    final private DaoHelper daoHelper;
    final private RowMapper<AccountEntity> accountEntityRowMapper;

    @Autowired
    public AccountDao(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            DaoHelper daoHelper,
            RowMapper<AccountEntity> accountEntityRowMapper) {
        super();
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.daoHelper = daoHelper;
        this.accountEntityRowMapper = accountEntityRowMapper;
    }

    @Override
    public List<AccountEntity> getEntityList() {
        String sql = "SELECT id, currency_id, sort, title FROM accounts ORDER BY sort";
        return namedParameterJdbcTemplate.query(sql, accountEntityRowMapper);
    }

    @Override
    public List<AccountEntity> getEntityList(Set<Integer> ids) {
        String sql = "SELECT id, currency_id, sort, title FROM accounts WHERE id IN (:ids) ORDER BY sort";
        return daoHelper.getEntityList(sql, ids, accountEntityRowMapper);
    }

    @Override
    public AccountEntity getEntity(Integer id) {
        String sql = "SELECT id, currency_id, sort, title FROM accounts WHERE id = :id";
        return daoHelper.getEntity(sql, id, accountEntityRowMapper);
    }

    @Override
    public int insertEntity(AccountEntity entity) {
        String sql = "INSERT INTO accounts(currency_id, sort, title) VALUES(:currency_id, :sort, :title)";
        Map<String, Object> params = new HashMap<>();
        params.put("currency_id", entity.getCurrencyId());
        params.put("sort", entity.getSort());
        params.put("title", entity.getTitle());
        Integer id = daoHelper.insertEntity(sql, params);
        entity.setId(id);
        return 1;
    }

    @Override
    public int updateEntity(AccountEntity entity) {
        String sql = "UPDATE accounts SET currency_id = :currency_id, sort = :sort, title = :title WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", entity.getId());
        params.put("currency_id", entity.getCurrencyId());
        params.put("sort", entity.getSort());
        params.put("title", entity.getTitle());
        return namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public int deleteEntity(Integer id) {
        String sql = "DELETE FROM accounts WHERE id = :id";
        return namedParameterJdbcTemplate.update(sql, Collections.singletonMap("id", id));
    }

}
