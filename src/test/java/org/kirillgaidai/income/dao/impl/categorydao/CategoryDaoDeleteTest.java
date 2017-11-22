package org.kirillgaidai.income.dao.impl.categorydao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

public class CategoryDaoDeleteTest extends CategoryDaoBaseTest {

    @Test
    public void testDeleteEntity_Ok() throws Exception {
        List<CategoryEntity> expected = Arrays.asList(orig.get(1), orig.get(2));
        int affectedRows = categoryDao.delete(3);
        assertEquals(1, affectedRows);
        List<CategoryEntity> actual = categoryDao.getList();
        assertEntityListEquals(expected, actual);
    }

    @Test
    public void testDeleteEntity_NotFound() throws Exception {
        int affectedRows = categoryDao.delete(0);
        assertEquals(0, affectedRows);
        List<CategoryEntity> actual = categoryDao.getList();
        assertEntityListEquals(orig, actual);
    }

}
