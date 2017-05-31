package org.kirillgaidai.income.dao.rowmappers;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.dao.utils.PersistenceTestUtils;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertOperationEntityEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class OperationEntityRowMapperTest {

    @Test
    public void testMapRow() throws Exception {
        LocalDate day = LocalDate.of(2017, 4, 12);
        BigDecimal amount = new BigDecimal(11.1);
        RowMapper<OperationEntity> operationEntityRowMapper = new OperationEntityRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        doReturn(1).when(resultSet).getInt("id");
        doReturn(2).when(resultSet).getInt("account_id");
        doReturn(3).when(resultSet).getInt("category_id");
        doReturn(Date.valueOf(day)).when(resultSet).getDate("day");
        doReturn(amount).when(resultSet).getBigDecimal("amount");
        doReturn("note1").when(resultSet).getString("note");
        OperationEntity expected = new OperationEntity(1, 2, 3, day, amount, "note1");
        OperationEntity actual = operationEntityRowMapper.mapRow(resultSet, 1);
        assertOperationEntityEquals(expected, actual);
    }
    
}
