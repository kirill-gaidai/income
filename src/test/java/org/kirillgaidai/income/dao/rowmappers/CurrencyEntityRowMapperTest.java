package org.kirillgaidai.income.dao.rowmappers;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;

import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertCurrencyEntityEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class CurrencyEntityRowMapperTest {

    @Test
    public void testMapRow() throws Exception {
        RowMapper<CurrencyEntity> currencyEntityRowMapper = new CurrencyEntityRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        doReturn(1).when(resultSet).getInt("id");
        doReturn("cc1").when(resultSet).getString("code");
        doReturn("currency1").when(resultSet).getString("title");
        doReturn(2).when(resultSet).getInt("accuracy");
        CurrencyEntity expected = new CurrencyEntity(1, "cc1", "currency1", 2);
        CurrencyEntity actual = currencyEntityRowMapper.mapRow(resultSet, 1);
        assertCurrencyEntityEquals(expected, actual);
    }

}
