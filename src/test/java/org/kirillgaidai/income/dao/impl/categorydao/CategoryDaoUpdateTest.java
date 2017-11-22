package org.kirillgaidai.income.dao.impl.categorydao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.entity.IGenericEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.CategoryDao#update(IGenericEntity)} test
 *
 * @author Kirill Gaidai
 */
public class CategoryDaoUpdateTest extends CategoryDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testUpdateEntity_Ok() throws Exception {
        CategoryEntity entity = new CategoryEntity(3, "04", "category4");
        List<CategoryEntity> expected = Arrays.asList(orig.get(1), orig.get(2), entity);
        int affectedRows = categoryDao.update(entity);
        assertEquals(1, affectedRows);
        List<CategoryEntity> actual = categoryDao.getList();
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        CategoryEntity entity = new CategoryEntity(4, "04", "category4");
        int affectedRows = categoryDao.update(entity);
        assertEquals(0, affectedRows);
        List<CategoryEntity> actual = categoryDao.getList();
        assertEntityListEquals(orig, actual);
    }

}
