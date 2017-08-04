package org.kirillgaidai.income.dao.rowmappers;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.RateEntity;
import org.kirillgaidai.income.dao.utils.PersistenceTestUtils;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;

import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertRateEntityEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class RateEntityRowMapperTest {

    @Test
    public void testMapRow() throws Exception {
        LocalDate day = LocalDate.of(2017, 8, 2);
        BigDecimal value = new BigDecimal("12.3");
        RowMapper<RateEntity> rateEntityRowMapper = new RateEntityRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        doReturn(2).when(resultSet).getInt("currency_id_from");
        doReturn(3).when(resultSet).getInt("currency_id_to");
        doReturn(Date.valueOf(day)).when(resultSet).getDate("day");
        doReturn(false).when(resultSet).wasNull();
        doReturn(value).when(resultSet).getBigDecimal("value");
        RateEntity expected = new RateEntity(2, 3, day, value);
        RateEntity actual = rateEntityRowMapper.mapRow(resultSet, 1);
        assertRateEntityEquals(expected, actual);
    }

    @Test
    public void testMapRowNulls() throws Exception {
        BigDecimal value = new BigDecimal("12.3");
        RowMapper<RateEntity> rateEntityRowMapper = new RateEntityRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        doReturn(2).when(resultSet).getInt("currency_id_from");
        doReturn(3).when(resultSet).getInt("currency_id_to");
        doReturn(null).when(resultSet).getDate("day");
        doReturn(true).when(resultSet).wasNull();
        doReturn(value).when(resultSet).getBigDecimal("value");
        RateEntity expected = new RateEntity(2, 3, null, value);
        RateEntity actual = rateEntityRowMapper.mapRow(resultSet, 1);
        assertRateEntityEquals(expected, actual);
    }

}
