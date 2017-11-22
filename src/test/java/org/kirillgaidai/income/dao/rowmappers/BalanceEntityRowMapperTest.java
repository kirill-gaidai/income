package org.kirillgaidai.income.dao.rowmappers;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class BalanceEntityRowMapperTest {

    @Test
    public void testMapRow() throws Exception {
        LocalDate day = LocalDate.of(2017, 4, 12);
        BigDecimal amount = new BigDecimal("12.3");
        RowMapper<BalanceEntity> balanceEntityRowMapper = new BalanceEntityRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        doReturn(1).when(resultSet).getInt("account_id");
        doReturn(Date.valueOf(day)).when(resultSet).getDate("day");
        doReturn(amount).when(resultSet).getBigDecimal("amount");
        doReturn(true).when(resultSet).getBoolean("manual");
        BalanceEntity expected = new BalanceEntity(1, day, amount, true);
        BalanceEntity actual = balanceEntityRowMapper.mapRow(resultSet, 1);
        assertEntityEquals(expected, actual);
    }

}
