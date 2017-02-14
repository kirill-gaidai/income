package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.CategoryDao;
import org.kirillgaidai.income.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CategoryDaoImpl implements CategoryDao {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public CategoryDaoImpl(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super();
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List<CategoryEntity> getCategoryList() {
        final String QUERY = "SELECT id, title FROM categories ORDER BY id";
        return namedParameterJdbcTemplate.query(QUERY, new HashMap<>(), getRowMapper());
    }

    @Override
    public CategoryEntity getCategoryById(final Long id) {
        final String QUERY = "SELECT id, title FROM categories WHERE :id = id";
        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        try {
            return namedParameterJdbcTemplate.queryForObject(QUERY, params, getRowMapper());
        } catch (final EmptyResultDataAccessException e) {
            return null;
        }
    }

    private RowMapper<CategoryEntity> getRowMapper() {
        return (resultSet, rowNum) -> {
            final CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId(resultSet.getLong("id"));
            categoryEntity.setTitle(resultSet.getString("title"));
            return categoryEntity;
        };
    }

}
