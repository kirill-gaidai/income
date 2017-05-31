package org.kirillgaidai.income.dao.rowmappers;

import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CurrencyEntityRowMapper implements RowMapper<CurrencyEntity> {

    @Override
    public CurrencyEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new CurrencyEntity(resultSet.getInt("id"), resultSet.getString("code"), resultSet.getString("title"));
    }

}
