package org.kirillgaidai.income.dao.rowmappers;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AccountEntityRowMapper implements RowMapper<AccountEntity> {

    @Override
    public AccountEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new AccountEntity(resultSet.getInt("id"), resultSet.getInt("currency_id"), resultSet.getString("sort"),
                resultSet.getString("title"));
    }

}
