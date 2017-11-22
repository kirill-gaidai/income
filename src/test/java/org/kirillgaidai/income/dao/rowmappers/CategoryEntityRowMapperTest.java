package org.kirillgaidai.income.dao.rowmappers;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class CategoryEntityRowMapperTest {

    @Test
    public void testMapRow() throws Exception {
        RowMapper<CategoryEntity> categoryEntityRowMapper = new CategoryEntityRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        doReturn(1).when(resultSet).getInt("id");
        doReturn("01").when(resultSet).getString("sort");
        doReturn("category1").when(resultSet).getString("title");
        CategoryEntity expected = new CategoryEntity(1, "01", "category1");
        CategoryEntity actual = categoryEntityRowMapper.mapRow(resultSet, 1);
        assertEntityEquals(expected, actual);
    }

}
