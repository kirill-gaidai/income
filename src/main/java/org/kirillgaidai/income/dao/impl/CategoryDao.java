package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class CategoryDao extends SerialDao<CategoryEntity> implements ICategoryDao {

    @Autowired
    public CategoryDao(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            RowMapper<CategoryEntity> rowMapper) {
        super(namedParameterJdbcTemplate, rowMapper);
    }

    @Override
    protected String getGetListSql() {
        return "SELECT id, sort, title FROM categories ORDER BY sort";
    }

    @Override
    protected String getUpdateSql() {
        return "UPDATE categories SET sort = :sort, title = :title WHERE id = :id";
    }

    @Override
    protected String getUpdateOptimisticSql() {
        return getUpdateSql() + " AND (id = :old_id) AND (sort = :old_sort) AND (title = :old_title)";
    }

    @Override
    protected Map<String, Object> getUpdateOptimisticParamsMap(CategoryEntity newEntity, CategoryEntity oldEntity) {
        Map<String, Object> params = getUpdateParamsMap(newEntity);
        params.put("old_id", oldEntity.getSort());
        params.put("old_sort", oldEntity.getSort());
        params.put("old_title", oldEntity.getTitle());
        return params;
    }

    @Override
    protected String getGetListByIdsSql() {
        return "SELECT id, sort, title FROM categories WHERE id IN (:ids) ORDER BY sort";
    }

    @Override
    protected String getGetByIdSql() {
        return "SELECT id, sort, title FROM categories WHERE :id = id";
    }

    @Override
    protected String getInsertSql() {
        return "INSERT INTO categories(sort, title) VALUES(:sort, :title)";
    }

    @Override
    protected Map<String, Object> getInsertParamsMap(CategoryEntity entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("sort", entity.getSort());
        params.put("title", entity.getTitle());
        return params;
    }

    @Override
    protected String getDeleteByIdSql() {
        return "DELETE FROM categories WHERE id = :id";
    }

    @Override
    protected String getDeleteOptimisticSql() {
        return "DELETE FROM categories WHERE (id = :id) AND (sort = :sort) AND (title = :title) " +
                "AND (0 = (SELECT COUNT(*) FROM operations WHERE category_id = :id))";
    }

    @Override
    protected Map<String, Object> getDeleteOptimisticParamsMap(CategoryEntity entity) {
        return getUpdateParamsMap(entity);
    }

}
