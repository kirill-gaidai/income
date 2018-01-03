package org.kirillgaidai.income.dao.rowmappers;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.UserEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class UserEntityRowMapperTest {

    /**
     * @throws Exception exception
     */
    @Test
    public void testMapRow() throws Exception {
        LocalDateTime expires = LocalDateTime.of(2018, 1, 2, 8, 45, 30);
        RowMapper<UserEntity> userEntityRowMapper = new UserEntityRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        doReturn(1).when(resultSet).getInt("id");
        doReturn("login").when(resultSet).getString("login");
        doReturn("password").when(resultSet).getString("password");
        doReturn(true).when(resultSet).getBoolean("admin");
        doReturn(false).when(resultSet).getBoolean("blocked");
        doReturn("token").when(resultSet).getString("token");
        doReturn(Timestamp.valueOf(expires)).when(resultSet).getTimestamp("expires");
        UserEntity expected = new UserEntity(1, "login", "password", true, false, "token", expires);
        UserEntity actual = userEntityRowMapper.mapRow(resultSet, 1);
        assertEntityEquals(expected, actual);
    }

}
