package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
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
public class CurrencyDao implements ICurrencyDao {

    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    final private DaoHelper daoHelper;
    final private RowMapper<CurrencyEntity> currencyEntityRowMapper;

    @Autowired
    public CurrencyDao(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            DaoHelper daoHelper,
            RowMapper<CurrencyEntity> currencyEntityRowMapper) {
        super();
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.daoHelper = daoHelper;
        this.currencyEntityRowMapper = currencyEntityRowMapper;
    }

    @Override
    public List<CurrencyEntity> getEntityList() {
        String sql = "SELECT id, code, title, accuracy FROM currencies ORDER BY title";
        return namedParameterJdbcTemplate.query(sql, currencyEntityRowMapper);
    }

    @Override
    public List<CurrencyEntity> getEntityList(Set<Integer> ids) {
        String sql = "SELECT id, code, title, accuracy FROM currencies WHERE id IN (:ids) ORDER BY title";
        return daoHelper.getEntityList(sql, ids, currencyEntityRowMapper);
    }

    @Override
    public CurrencyEntity getEntity(Integer id) {
        String sql = "SELECT id, code, title, accuracy FROM currencies WHERE id = :id";
        return daoHelper.getEntity(sql, id, currencyEntityRowMapper);
    }

    @Override
    public int insertEntity(CurrencyEntity entity) {
        String sql = "INSERT INTO currencies(code, title, accuracy) VALUES(:code, :title, :accuracy)";
        Map<String, Object> params = new HashMap<>();
        params.put("code", entity.getCode());
        params.put("title", entity.getTitle());
        params.put("accuracy", entity.getAccuracy());
        Integer id = daoHelper.insertEntity(sql, params);
        entity.setId(id);
        return 1;
    }

    @Override
    public int updateEntity(CurrencyEntity entity) {
        String sql = "UPDATE currencies SET code = :code, title = :title, accuracy = :accuracy WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", entity.getId());
        params.put("code", entity.getCode());
        params.put("title", entity.getTitle());
        params.put("accuracy", entity.getAccuracy());
        return namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public int deleteEntity(Integer id) {
        String sql = "DELETE FROM currencies WHERE id = :id";
        return namedParameterJdbcTemplate.update(sql, Collections.singletonMap("id", id));
    }

}
