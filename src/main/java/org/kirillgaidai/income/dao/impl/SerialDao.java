package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.entity.ISerialEntity;
import org.kirillgaidai.income.dao.intf.ISerialDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class SerialDao<T extends ISerialEntity> extends GenericDao<T> implements ISerialDao<T> {

    final protected String ID_FIELD = "id";
    final protected String ID_FIELDS = "ids";

    public SerialDao(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            RowMapper<T> rowMapper) {
        super(namedParameterJdbcTemplate, rowMapper);
    }

    @Override
    public List<T> getList(Set<Integer> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        return namedParameterJdbcTemplate.query(
                getGetListByIdsSql(),
                Collections.singletonMap(ID_FIELDS, ids),
                rowMapper);
    }

    @Override
    public T get(Integer id) {
        try {
            return namedParameterJdbcTemplate.queryForObject(
                    getGetByIdSql(),
                    Collections.singletonMap(ID_FIELD, id),
                    rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int insert(T entity) {
        SqlParameterSource paramSource = new MapSqlParameterSource(getInsertParamsMap(entity));
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String[] keyColumnNames = new String[]{ID_FIELD};
        int affectedRows = namedParameterJdbcTemplate.update(getInsertSql(), paramSource, keyHolder, keyColumnNames);
        entity.setId(keyHolder.getKey().intValue());
        return affectedRows;
    }

    @Override
    public int delete(Integer id) {
        return namedParameterJdbcTemplate.update(getDeleteByIdSql(), Collections.singletonMap(ID_FIELD, id));
    }

    @Override
    protected Map<String, Object> getUpdateParamsMap(T entity) {
        Map<String, Object> params = getInsertParamsMap(entity);
        params.put(ID_FIELD, entity.getId());
        return params;
    }

    protected abstract String getGetListByIdsSql();

    protected abstract String getGetByIdSql();

    protected abstract String getInsertSql();

    protected abstract Map<String, Object> getInsertParamsMap(T entity);

    protected abstract String getDeleteByIdSql();

}
