package org.kirillgaidai.income.dao.impl.categorydao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.entity.IGenericEntity;
import org.kirillgaidai.income.dao.entity.ISerialEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.CategoryDao#insert(ISerialEntity)} test
 *
 * @author Kirill Gaidai
 */
public class CategoryDaoInsertTest extends CategoryDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testInsertEntity_Ok() throws Exception {
        CategoryEntity entity = new CategoryEntity(null, "04", "category4");
        List<CategoryEntity> expected = Arrays.asList(orig.get(0), orig.get(1), orig.get(2), entity);
        int affectedRows = categoryDao.insert(entity);
        assertEquals(1, affectedRows);
        assertEquals(Integer.valueOf(4), entity.getId());
        List<CategoryEntity> actual = categoryDao.getList();
        assertEntityListEquals(expected, actual);
    }

}
