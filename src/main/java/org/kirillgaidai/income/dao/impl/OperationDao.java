package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.dao.intf.IOperationDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class OperationDao extends SerialDao<OperationEntity> implements IOperationDao {

    final private static Logger LOGGER = LoggerFactory.getLogger(OperationDao.class);

    @Autowired
    public OperationDao(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            RowMapper<OperationEntity> rowMapper) {
        super(namedParameterJdbcTemplate, rowMapper);
    }

    @Override
    public List<OperationEntity> getList(
            Set<Integer> accountIds, Set<Integer> categoryIds, LocalDate firstDay, LocalDate lastDay) {
        LOGGER.debug("Entering method");
        if (accountIds.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = "SELECT id, account_id, category_id, day, amount, note FROM operations " +
                "WHERE (account_id IN (:account_ids)) ";
        Map<String, Object> params = new HashMap<>();
        params.put("account_ids", accountIds);
        if (!categoryIds.isEmpty()) {
            sql += "AND (category_id IN (:category_ids)) ";
            params.put("category_ids", categoryIds);
        }
        sql += "AND (day BETWEEN :first_day AND :last_day) ORDER BY id";
        params.put("first_day", Date.valueOf(firstDay));
        params.put("last_day", Date.valueOf(lastDay));
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    @Override
    protected String getGetListSql() {
        LOGGER.debug("Entering method");
        return "SELECT id, account_id, category_id, day, amount, note FROM operations ORDER BY id";
    }

    @Override
    protected String getUpdateSql() {
        LOGGER.debug("Entering method");
        return "UPDATE operations SET category_id = :category_id, amount = :amount, note = :note WHERE id = :id";
    }

    @Override
    protected String getUpdateOptimisticSql() {
        LOGGER.debug("Entering method");
        return getUpdateSql() + " AND (account_id = :account_id) AND (day = :day) AND (id = :old_id)" +
                " AND (account_id = :old_account_id) AND (category_id = :old_category_id) AND (day = :old_day)" +
                " AND (amount = :old_amount) AND (note = :old_note)";
    }

    @Override
    protected Map<String, Object> getUpdateOptimisticParamsMap(OperationEntity newEntity, OperationEntity oldEntity) {
        LOGGER.debug("Entering method");
        Map<String, Object> params = getUpdateParamsMap(newEntity);
        params.put("old_id", oldEntity.getId());
        params.put("old_account_id", oldEntity.getAccountId());
        params.put("old_category_id", oldEntity.getCategoryId());
        params.put("old_day", Date.valueOf(oldEntity.getDay()));
        params.put("old_amount", oldEntity.getAmount());
        params.put("old_note", oldEntity.getNote());
        return params;
    }

    @Override
    protected String getGetListByIdsSql() {
        LOGGER.debug("Entering method");
        return "SELECT id, account_id, category_id, day, amount, note " +
                "FROM operations WHERE id IN (:ids) ORDER BY id";
    }

    @Override
    protected String getGetByIdSql() {
        LOGGER.debug("Entering method");
        return "SELECT id, account_id, category_id, day, amount, note FROM operations WHERE id = :id";
    }

    @Override
    protected String getInsertSql() {
        LOGGER.debug("Entering method");
        return "INSERT INTO operations(account_id, category_id, day, amount, note) " +
                "VALUES(:account_id, :category_id, :day, :amount, :note)";
    }

    @Override
    protected Map<String, Object> getInsertParamsMap(OperationEntity entity) {
        LOGGER.debug("Entering method");
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", entity.getAccountId());
        params.put("category_id", entity.getCategoryId());
        params.put("day", Date.valueOf(entity.getDay()));
        params.put("amount", entity.getAmount());
        params.put("note", entity.getNote());
        return params;
    }

    @Override
    protected String getDeleteByIdSql() {
        LOGGER.debug("Entering method");
        return "DELETE FROM operations WHERE id = :id";
    }

    @Override
    protected String getDeleteOptimisticSql() {
        LOGGER.debug("Entering method");
        return "DELETE FROM operations WHERE (id = :id) AND " +
                "(account_id = :account_id) AND (category_id = :category_id) AND (day = :day) AND " +
                "(amount = :amount) AND (note = :note)";
    }

    @Override
    public int getCountByAccountId(Integer accountId) {
        LOGGER.debug("Entering method");
        String sql = "SELECT COUNT(*) FROM operations WHERE account_id = :account_id";
        Map<String, Object> params = Collections.singletonMap("account_id", accountId);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    @Override
    public int getCountByCategoryId(Integer categoryId) {
        LOGGER.debug("Entering method");
        String sql = "SELECT COUNT(*) FROM operations WHERE category_id = :category_id";
        Map<String, Object> params = Collections.singletonMap("category_id", categoryId);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    @Override
    public int getCountByAccountIdAndDay(Integer accountId, LocalDate day) {
        LOGGER.debug("Entering method");
        String sql = "SELECT COUNT(*) FROM operations WHERE (account_id = :account_id) AND (day = :day)";
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", accountId);
        params.put("day", Date.valueOf(day));
        return namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
    }

}
