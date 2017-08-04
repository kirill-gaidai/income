package org.kirillgaidai.income.dao.rowmappers;

import org.kirillgaidai.income.dao.entity.RateEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RateEntityRowMapper implements RowMapper<RateEntity> {

    @Override
    public RateEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        RateEntity result = new RateEntity();
        result.setCurrencyIdFrom(resultSet.getInt("currency_id_from"));
        result.setCurrencyIdTo(resultSet.getInt("currency_id_to"));
        Date day = resultSet.getDate("day");
        if (!resultSet.wasNull()) {
            result.setDay(day.toLocalDate());
        }
        result.setValue(resultSet.getBigDecimal("value"));
        return result;
    }

}
