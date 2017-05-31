package org.kirillgaidai.income.dao.rowmappers;

import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CategoryEntityRowMapper implements RowMapper<CategoryEntity> {

    @Override
    public CategoryEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new CategoryEntity(resultSet.getInt("id"), resultSet.getString("sort"), resultSet.getString("title"));
    }

}
