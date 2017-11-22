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
        LOGGER.debug("Entering method");
        return "SELECT id, code, title, accuracy FROM currencies ORDER BY title";
    }

    @Override
    protected String getUpdateSql() {
        LOGGER.debug("Entering method");
        return "UPDATE currencies SET code = :code, title = :title, accuracy = :accuracy WHERE id = :id";
    }

    @Override
    protected String getUpdateOptimisticSql() {
        LOGGER.debug("Entering method");
        return getUpdateSql() + " AND (code = :old_code) AND (title = :old_title) AND (accuracy = :old_accuracy)";
    }

    @Override
    protected Map<String, Object> getUpdateOptimisticParamsMap(CurrencyEntity newEntity, CurrencyEntity oldEntity) {
        LOGGER.debug("Entering method");
        Map<String, Object> params = getUpdateParamsMap(newEntity);
        params.put("old_code", oldEntity.getCode());
        params.put("old_title", oldEntity.getTitle());
        params.put("old_accuracy", oldEntity.getAccuracy());
        return params;
    }

    @Override
    protected String getGetListByIdsSql() {
        LOGGER.debug("Entering method");
        return "SELECT id, code, title, accuracy FROM currencies WHERE id IN (:ids) ORDER BY title";
    }

    @Override
    protected String getGetByIdSql() {
        LOGGER.debug("Entering method");
        return "SELECT id, code, title, accuracy FROM currencies WHERE id = :id";
    }

    @Override
    protected String getInsertSql() {
        LOGGER.debug("Entering method");
        return "INSERT INTO currencies(code, title, accuracy) VALUES(:code, :title, :accuracy)";
    }

    @Override
    protected Map<String, Object> getInsertParamsMap(CurrencyEntity entity) {
        LOGGER.debug("Entering method");
        Map<String, Object> params = new HashMap<>();
        params.put("code", entity.getCode());
        params.put("title", entity.getTitle());
        params.put("accuracy", entity.getAccuracy());
        return params;
    }

    @Override
    protected String getDeleteByIdSql() {
        LOGGER.debug("Entering method");
        return "DELETE FROM currencies WHERE id = :id";
    }

    @Override
    protected String getDeleteOptimisticSql() {
        LOGGER.debug("Entering method");
        return "DELETE FROM currencies " +
                "WHERE (id = :id) AND (code = :code) AND (title = :title) AND (accuracy = :accuracy) " +
                "AND (0 = (SELECT COUNT(*) FROM accounts WHERE currency_id = :id))";
    }

    @Override
    protected Map<String, Object> getDeleteOptimisticParamsMap(CurrencyEntity entity) {
        LOGGER.debug("Entering method");
        return getUpdateParamsMap(entity);
    }

}
