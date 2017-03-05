package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.OperationDao;
import org.kirillgaidai.income.entity.OperationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class OperationDaoImpl implements OperationDao {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public OperationDaoImpl(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public int insertEntity(final OperationEntity operationEntity) {
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final String QUERY = "INSERT INTO operations(account_id, category_id, day, amount, note) " +
                "VALUES(:account_id, :category_id, :day, :amount, :note)";

        final Map<String, Object> params = new HashMap<>();
        params.put("account_id", operationEntity.getAccountId());
        params.put("category_id", operationEntity.getCategoryId());
        params.put("day", operationEntity.getDay());
        params.put("amount", operationEntity.getAmount());
        params.put("note", operationEntity.getNote());
        final SqlParameterSource sqlParameterSource = new MapSqlParameterSource(params);

        final int affectedRows = namedParameterJdbcTemplate.update(
                QUERY, sqlParameterSource, keyHolder, new String[] {"id"});
        operationEntity.setId(keyHolder.getKey().intValue());
        return affectedRows;
    }

}
