package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.dao.intf.IOperationDao;
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

    @Autowired
    public OperationDao(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            RowMapper<OperationEntity> rowMapper) {
        super(namedParameterJdbcTemplate, rowMapper);
    }

    @Override
    public List<OperationEntity> getList(Set<Integer> accountIds, LocalDate firstDay, LocalDate lastDay) {
        if (accountIds.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = "SELECT id, account_id, category_id, day, amount, note FROM operations " +
                "WHERE (account_id IN (:account_ids)) AND (day BETWEEN :first_day AND :last_day) ORDER BY id";
        Map<String, Object> params = new HashMap<>();
        params.put("account_ids", accountIds);
        params.put("first_day", Date.valueOf(firstDay));
        params.put("last_day", Date.valueOf(lastDay));
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    @Override
    public List<OperationEntity> getList(Set<Integer> accountIds, LocalDate day) {
        if (accountIds.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = "SELECT id, account_id, category_id, day, amount, note FROM operations " +
                "WHERE (account_id IN (:account_ids)) AND (day = :day) ORDER BY id";
        Map<String, Object> params = new HashMap<>();
        params.put("account_ids", accountIds);
        params.put("day", Date.valueOf(day));
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    @Override
    public List<OperationEntity> getList(Set<Integer> accountIds, LocalDate day, Integer categoryId) {
        if (accountIds.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = "SELECT id, account_id, category_id, day, amount, note FROM operations " +
                "WHERE (account_id IN (:account_ids)) AND (day = :day) AND (category_id = :category_id) ORDER BY id";
        Map<String, Object> params = new HashMap<>();
        params.put("account_ids", accountIds);
        params.put("day", Date.valueOf(day));
        params.put("category_id", categoryId);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    @Override
    protected String getGetListSql() {
        return "SELECT id, account_id, category_id, day, amount, note FROM operations ORDER BY id";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE operations SET amount = :amount, note = :note WHERE id = :id";
    }

    @Override
    protected Map<String, Object> getUpdateParamsMap(OperationEntity entity) {
        Map<String, Object> params = new HashMap<>();
        params.put(ID_FIELD, entity.getId());
        params.put("amount", entity.getAmount());
        params.put("note", entity.getNote());
        return params;
    }

    @Override
    protected String getUpdateOptimisticSql() {
        return getUpdateSql() + " AND (amount = :old_amount) AND (note = :old_note)";
    }

    @Override
    protected Map<String, Object> getUpdateOptimisticParamsMap(OperationEntity newEntity, OperationEntity oldEntity) {
        Map<String, Object> params = getUpdateParamsMap(newEntity);
        params.put("old_amount", oldEntity.getAmount());
        params.put("old_note", oldEntity.getNote());
        return params;
    }

    @Override
    protected String getGetListByIdsSql() {
        return "SELECT id, account_id, category_id, day, amount, note " +
                "FROM operations WHERE id IN (:ids) ORDER BY id";
    }

    @Override
    protected String getGetByIdSql() {
        return "SELECT id, account_id, category_id, day, amount, note FROM operations WHERE id = :id";
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO operations(account_id, category_id, day, amount, note) " +
                "VALUES(:account_id, :category_id, :day, :amount, :note)";
    }

    @Override
    protected Map<String, Object> getInsertParamsMap(OperationEntity entity) {
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
        return "DELETE FROM operations WHERE id = :id";
    }

    @Override
    protected String getDeleteOptimisticSql() {
        return "DELETE FROM operations WHERE (id = :id) AND " +
                "(account_id = :account_id) AND (category_id = :category_id) AND " +
                "(amount = :amount) AND (note = :note)";
    }

    @Override
    protected Map<String, Object> getDeleteOptimisticParamsMap(OperationEntity entity) {
        return getUpdateParamsMap(entity);
    }
}
