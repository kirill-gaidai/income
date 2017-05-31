package org.kirillgaidai.income.dao.util;

import org.kirillgaidai.income.dao.exception.IncomeDaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class DaoHelper {

    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public DaoHelper(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public <T> T getEntity(String sql, Integer id, RowMapper<T> rowMapper) {
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, Collections.singletonMap("id", id), rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public <T> List<T> getEntityList(String sql, Set<Integer> ids, RowMapper<T> rowMapper) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        return namedParameterJdbcTemplate.query(sql, Collections.singletonMap("ids", ids), rowMapper);
    }

    public Integer insertEntity(String sql, Map<String, Object> params) {
        SqlParameterSource paramSource = new MapSqlParameterSource(params);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String[] keyColumnNames = new String[]{"id"};

        int affectedRows = namedParameterJdbcTemplate.update(sql, paramSource, keyHolder, keyColumnNames);
        if (affectedRows != 1) {
            throw new IncomeDaoException("Insert operation did not affect one row");
        }

        return keyHolder.getKey().intValue();
    }

}
