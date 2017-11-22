package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.entity.IGenericEntity;
import org.kirillgaidai.income.dao.intf.IGenericDao;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

/**
 * Generic DAO
 *
 * @param <T> entity
 * @author Kirill Gaidai
 */
public abstract class GenericDao<T extends IGenericEntity> implements IGenericDao<T> {

    final protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    final protected RowMapper<T> rowMapper;

    public GenericDao(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            RowMapper<T> rowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public List<T> getList() {
        return namedParameterJdbcTemplate.query(getGetListSql(), rowMapper);
    }

    @Override
    public int insert(T entity) {
        return namedParameterJdbcTemplate.update(getInsertSql(), getInsertParamsMap(entity));
    }

    @Override
    public int update(T entity) {
        return namedParameterJdbcTemplate.update(getUpdateSql(), getUpdateParamsMap(entity));
    }

    @Override
    public int update(T newEntity, T oldEntity) {
        return namedParameterJdbcTemplate.update(
                getUpdateOptimisticSql(), getUpdateOptimisticParamsMap(newEntity, oldEntity));
    }

    @Override
    public int delete(T entity) {
        return namedParameterJdbcTemplate.update(
                getDeleteOptimisticSql(), getDeleteOptimisticParamsMap(entity));
    }

    /**
     * Returns SQL used by {@link IGenericDao#getList()}
     *
     * @return getting list SQL
     */
    protected abstract String getGetListSql();

    protected abstract String getInsertSql();

    protected abstract Map<String, Object> getInsertParamsMap(T entity);

    protected abstract String getUpdateSql();

    protected abstract Map<String, Object> getUpdateParamsMap(T entity);

    protected abstract String getUpdateOptimisticSql();

    protected abstract Map<String, Object> getUpdateOptimisticParamsMap(T newEntity, T oldEntity);

    protected abstract String getDeleteOptimisticSql();

    protected abstract Map<String, Object> getDeleteOptimisticParamsMap(T entity);

}
