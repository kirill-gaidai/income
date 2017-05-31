package org.kirillgaidai.income.dao.rowmappers;

import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class OperationEntityRowMapper implements RowMapper<OperationEntity> {

    @Override
    public OperationEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new OperationEntity(resultSet.getInt("id"), resultSet.getInt("account_id"),
                resultSet.getInt("category_id"), resultSet.getDate("day").toLocalDate(),
                resultSet.getBigDecimal("amount"), resultSet.getString("note"));
    }

}
