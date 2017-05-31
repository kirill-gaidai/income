package org.kirillgaidai.income.dao.rowmappers;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;

import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertAccountEntityEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class AccountEntityRowMapperTest {

    @Test
    public void testMapRow() throws Exception {
        RowMapper<AccountEntity> accountEntityRowMapper = new AccountEntityRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        doReturn(1).when(resultSet).getInt("id");
        doReturn(2).when(resultSet).getInt("currency_id");
        doReturn("01").when(resultSet).getString("sort");
        doReturn("account1").when(resultSet).getString("title");
        AccountEntity expected = new AccountEntity(1, 2, "01", "account1");
        AccountEntity actual = accountEntityRowMapper.mapRow(resultSet, 1);
        assertAccountEntityEquals(expected, actual);
    }

}
