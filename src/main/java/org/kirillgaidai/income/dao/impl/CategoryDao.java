package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.dao.util.DaoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public class CategoryDao implements ICategoryDao {

    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    final private DaoHelper daoHelper;
    final private RowMapper<CategoryEntity> categoryEntityRowMapper;

    @Autowired
    public CategoryDao(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            DaoHelper daoHelper,
            RowMapper<CategoryEntity> categoryEntityRowMapper) {
        super();
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.daoHelper = daoHelper;
        this.categoryEntityRowMapper = categoryEntityRowMapper;
    }

    @Override
    public List<CategoryEntity> getEntityList() {
        String sql = "SELECT id, sort, title FROM categories ORDER BY sort";
        return namedParameterJdbcTemplate.query(sql, categoryEntityRowMapper);
    }

    @Override
    public List<CategoryEntity> getEntityList(final Set<Integer> ids) {
        String sql = "SELECT id, sort, title FROM categories WHERE id IN (:ids) ORDER BY sort";
        return daoHelper.getEntityList(sql, ids, categoryEntityRowMapper);
    }

    @Override
    public CategoryEntity getEntity(final Integer id) {
        String sql = "SELECT id, sort, title FROM categories WHERE :id = id";
        return daoHelper.getEntity(sql, id, categoryEntityRowMapper);
    }

    @Override
    public int insertEntity(CategoryEntity entity) {
        String sql = "INSERT INTO categories(sort, title) VALUES(:sort, :title)";
        Map<String, Object> params = new HashMap<>();
        params.put("sort", entity.getSort());
        params.put("title", entity.getTitle());
        Integer id = daoHelper.insertEntity(sql, params);
        entity.setId(id);
        return 1;
    }

    @Override
    public int updateEntity(CategoryEntity entity) {
        String sql = "UPDATE categories SET sort = :sort, title = :title WHERE id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", entity.getId());
        params.put("sort", entity.getSort());
        params.put("title", entity.getTitle());
        return namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public int deleteEntity(Integer id) {
        String sql = "DELETE FROM categories WHERE id = :id";
        return namedParameterJdbcTemplate.update(sql, Collections.singletonMap("id", id));
    }

}
