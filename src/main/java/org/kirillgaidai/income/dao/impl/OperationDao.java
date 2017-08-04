package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.dao.intf.IOperationDao;
import org.kirillgaidai.income.dao.util.DaoHelper;
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
public class OperationDao implements IOperationDao {

    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    final private DaoHelper daoHelper;
    final private RowMapper<OperationEntity> operationEntityRowMapper;

    @Autowired
    public OperationDao(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            DaoHelper daoHelper,
            RowMapper<OperationEntity> operationEntityRowMapper) {
        super();
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.daoHelper = daoHelper;
        this.operationEntityRowMapper = operationEntityRowMapper;
    }

    @Override
    public List<OperationEntity> getEntityList() {
        String sql = "SELECT id, account_id, category_id, day, amount, note FROM operations ORDER BY id";
        return namedParameterJdbcTemplate.query(sql, operationEntityRowMapper);
    }

    @Override
    public List<OperationEntity> getEntityList(Set<Integer> ids) {
        String sql = "SELECT id, account_id, category_id, day, amount, note " +
                "FROM operations WHERE id IN (:ids) ORDER BY id";
        return daoHelper.getEntityList(sql, ids, operationEntityRowMapper);
    }

    @Override
    public OperationEntity getEntity(Integer id) {
        String sql = "SELECT id, account_id, category_id, day, amount, note FROM operations WHERE id = :id";
        return daoHelper.getEntity(sql, id, operationEntityRowMapper);
    }

    @Override
    public int insertEntity(OperationEntity entity) {
        String sql = "INSERT INTO operations(account_id, category_id, day, amount, note) " +
                "VALUES(:account_id, :category_id, :day, :amount, :note)";
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", entity.getAccountId());
        params.put("category_id", entity.getCategoryId());
        params.put("day", Date.valueOf(entity.getDay()));
        params.put("amount", entity.getAmount());
        params.put("note", entity.getNote());
        Integer id = daoHelper.insertEntity(sql, params);
        entity.setId(id);
        return 1;
    }

    @Override
    public int updateEntity(OperationEntity entity) {
        String sql = "UPDATE operations SET amount = :amount, note = :note WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", entity.getId());
        params.put("amount", entity.getAmount());
        params.put("note", entity.getNote());
        return namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public int deleteEntity(Integer id) {
        String sql = "DELETE FROM operations WHERE id = :id";
        return namedParameterJdbcTemplate.update(sql, Collections.singletonMap("id", id));
    }

    @Override
    public List<OperationEntity> getEntityList(Set<Integer> accountIds, LocalDate firstDay, LocalDate lastDay) {
        if (accountIds.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = "SELECT id, account_id, category_id, day, amount, note FROM operations " +
                "WHERE (account_id IN (:account_ids)) AND (day BETWEEN :first_day AND :last_day) ORDER BY id";
        Map<String, Object> params = new HashMap<>();
        params.put("account_ids", accountIds);
        params.put("first_day", Date.valueOf(firstDay));
        params.put("last_day", Date.valueOf(lastDay));
        return namedParameterJdbcTemplate.query(sql, params, operationEntityRowMapper);
    }

    @Override
    public List<OperationEntity> getEntityList(Set<Integer> accountIds, LocalDate day) {
        if (accountIds.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = "SELECT id, account_id, category_id, day, amount, note FROM operations " +
                "WHERE (account_id IN (:account_ids)) AND (day = :day) ORDER BY id";
        Map<String, Object> params = new HashMap<>();
        params.put("account_ids", accountIds);
        params.put("day", Date.valueOf(day));
        return namedParameterJdbcTemplate.query(sql, params, operationEntityRowMapper);
    }

    @Override
    public List<OperationEntity> getEntityList(Set<Integer> accountIds, LocalDate day, Integer categoryId) {
        if (accountIds.isEmpty()) {
            return Collections.emptyList();
        }

        String sql = "SELECT id, account_id, category_id, day, amount, note FROM operations " +
                "WHERE (account_id IN (:account_ids)) AND (day = :day) AND (category_id = :category_id) ORDER BY id";
        Map<String, Object> params = new HashMap<>();
        params.put("account_ids", accountIds);
        params.put("day", Date.valueOf(day));
        params.put("category_id", categoryId);
        return namedParameterJdbcTemplate.query(sql, params, operationEntityRowMapper);
    }

}
