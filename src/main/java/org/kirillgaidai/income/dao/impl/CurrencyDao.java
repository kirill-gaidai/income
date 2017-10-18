package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CurrencyDao extends SerialDao<CurrencyEntity> implements ICurrencyDao {

    final private static Logger LOGGER = LoggerFactory.getLogger(CurrencyDao.class);

    @Autowired
    public CurrencyDao(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            RowMapper<CurrencyEntity> rowMapper) {
        super(namedParameterJdbcTemplate, rowMapper);
    }

    @Override
    protected String getGetListSql() {
        return "SELECT id, code, title, accuracy FROM currencies ORDER BY title";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE currencies SET code = :code, title = :title, accuracy = :accuracy WHERE id = :id";
    }

    @Override
    protected String getGetListByIdsSql() {
        return "SELECT id, code, title, accuracy FROM currencies WHERE id IN (:ids) ORDER BY title";
    }

    @Override
    protected String getGetByIdSql() {
        return "SELECT id, code, title, accuracy FROM currencies WHERE id = :id";
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO currencies(code, title, accuracy) VALUES(:code, :title, :accuracy)";
    }

    @Override
    protected Map<String, Object> getInsertParamsMap(CurrencyEntity entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("code", entity.getCode());
        params.put("title", entity.getTitle());
        params.put("accuracy", entity.getAccuracy());
        return params;
    }

    @Override
    protected String getDeleteByIdSql() {
        return "DELETE FROM currencies WHERE id = :id";
    }

}
