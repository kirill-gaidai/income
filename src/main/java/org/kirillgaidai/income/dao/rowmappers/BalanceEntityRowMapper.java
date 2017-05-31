package org.kirillgaidai.income.dao.rowmappers;

import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class BalanceEntityRowMapper implements RowMapper<BalanceEntity> {

    @Override
    public BalanceEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new BalanceEntity(resultSet.getInt("account_id"), resultSet.getDate("day").toLocalDate(),
                resultSet.getBigDecimal("amount"), resultSet.getBoolean("manual"));
    }

}
