package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.CategoryDao;
import org.kirillgaidai.income.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

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
    public CategoryEntity getCategoryById(final Integer id) {
        final String QUERY = "SELECT id, title FROM categories WHERE :id = id";
        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        try {
            return namedParameterJdbcTemplate.queryForObject(QUERY, params, getRowMapper());
        } catch (final EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int insertCategory(final CategoryEntity categoryEntity) {
        final String QUERY_KEY = "SELECT nextval('categories_id_seq')";
        final Integer id = namedParameterJdbcTemplate.queryForObject(
                QUERY_KEY, new HashMap<>(), (resultSet, rowNum) -> resultSet.getInt(1));
        categoryEntity.setId(id);

        final String QUERY = "INSERT INTO categories(id, title) VALUES(:id, :title)";
        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("title", categoryEntity.getTitle());
        return namedParameterJdbcTemplate.update(QUERY, params);
    }

    @Override
    public int updateCategory(final CategoryEntity categoryEntity) {
        final String QUERY = "UPDATE categories SET title = :title WHERE id = :id";
        final Map<String, Object> params = new HashMap<>();
        params.put("id", categoryEntity.getId());
        params.put("title", categoryEntity.getTitle());
        return namedParameterJdbcTemplate.update(QUERY, params);
    }

    @Override
    public int deleteCategory(final Integer id) {
        final String QUERY = "DELETE FROM categories WHERE id = :id";
        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return namedParameterJdbcTemplate.update(QUERY, params);
    }

    private RowMapper<CategoryEntity> getRowMapper() {
        return (resultSet, rowNum) -> {
            final CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setId(resultSet.getInt("id"));
            categoryEntity.setTitle(resultSet.getString("title"));
            return categoryEntity;
        };
    }

}
