package org.kirillgaidai.income.dao.impl.categorydao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.entity.IGenericEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.CategoryDao#update(IGenericEntity, IGenericEntity)} test
 */
public class CategoryDaoUpdateOptimisticTest extends CategoryDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        CategoryEntity newEntity = new CategoryEntity(3, "04", "category4");
        CategoryEntity oldEntity = orig.get(0);
        List<CategoryEntity> expected = Arrays.asList(orig.get(1), orig.get(2), newEntity);
        int affectedRows = categoryDao.update(newEntity, oldEntity);
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
        CategoryEntity newEntity = new CategoryEntity(4, "04", "category4");
        CategoryEntity oldEntity = new CategoryEntity(4, "03", "category3");
        int affectedRows = categoryDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<CategoryEntity> actual = categoryDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test already modified
     *
     * @throws Exception exception
     */
    @Test
    public void testAlreadyModified() throws Exception {
        CategoryEntity newEntity = new CategoryEntity(3, "04", "category4");
        CategoryEntity oldEntity = new CategoryEntity(3, "02", "category2");
        int affectedRows = categoryDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<CategoryEntity> actual = categoryDao.getList();
        assertEntityListEquals(orig, actual);
    }

}
