package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.OperationDao;
import org.kirillgaidai.income.entity.OperationEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
        final String QUERY_KEY = "SELECT nextval('currencies_id_seq')";
        final Integer id = namedParameterJdbcTemplate.queryForObject(
                QUERY_KEY, new HashMap<>(), (resultSet, rowNum) -> resultSet.getInt(1));
        operationEntity.setId(id);

        final String QUERY = "INSERT INTO operations(id, account_id, category_id, day, amount, note) " +
                "VALUES(:id, :account_id, :category_id, :day, :amount, :note)";
        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("account_id", operationEntity.getAccountId());
        params.put("category_id", operationEntity.getCategoryId());
        params.put("day", operationEntity.getDay());
        params.put("amount", operationEntity.getAmount());
        params.put("note", operationEntity.getNote());
        return namedParameterJdbcTemplate.update(QUERY, params);
    }

}
