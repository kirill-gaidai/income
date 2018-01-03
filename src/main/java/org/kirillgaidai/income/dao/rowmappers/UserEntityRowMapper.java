package org.kirillgaidai.income.dao.rowmappers;

import org.kirillgaidai.income.dao.entity.UserEntity;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * User entity row mapper
 *
 * @author Kirill Gaidai
 */
@Component
public class UserEntityRowMapper implements RowMapper<UserEntity> {

    @Override
    public UserEntity mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        return new UserEntity(
                resultSet.getInt("id"),
                resultSet.getString("login"),
                resultSet.getString("password"),
                resultSet.getBoolean("admin"),
                resultSet.getBoolean("blocked"),
                resultSet.getString("token"),
                resultSet.getTimestamp("expires").toLocalDateTime());
    }

}
