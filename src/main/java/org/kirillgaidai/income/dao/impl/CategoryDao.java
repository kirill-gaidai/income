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

}
