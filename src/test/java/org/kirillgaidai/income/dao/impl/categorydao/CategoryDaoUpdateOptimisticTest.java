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
     * Test id changed
     *
     * @throws Exception exception
     */
    @Test
    public void testIdChanged() throws Exception {
        CategoryEntity newEntity = new CategoryEntity(3, "00", "category0");
        CategoryEntity oldEntity = new CategoryEntity(0, "01", "category1");
        int affectedRows = categoryDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<CategoryEntity> actual = categoryDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test sort changed
     *
     * @throws Exception exception
     */
    @Test
    public void testSortChanged() throws Exception {
        CategoryEntity newEntity = new CategoryEntity(3, "00", "category0");
        CategoryEntity oldEntity = new CategoryEntity(3, "00", "category1");
        int affectedRows = categoryDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<CategoryEntity> actual = categoryDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test title changed
     *
     * @throws Exception exception
     */
    @Test
    public void testTitleChanged() throws Exception {
        CategoryEntity newEntity = new CategoryEntity(3, "00", "category0");
        CategoryEntity oldEntity = new CategoryEntity(3, "01", "category0");
        int affectedRows = categoryDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<CategoryEntity> actual = categoryDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        CategoryEntity newEntity = new CategoryEntity(0, "00", "category0");
        CategoryEntity oldEntity = new CategoryEntity(3, "01", "category1");
        int affectedRows = categoryDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<CategoryEntity> actual = categoryDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        CategoryEntity newEntity = new CategoryEntity(3, "00", "category0");
        CategoryEntity oldEntity = new CategoryEntity(3, "01", "category1");
        int affectedRows = categoryDao.update(newEntity, oldEntity);
        assertEquals(1, affectedRows);
        List<CategoryEntity> expected = Arrays.asList(newEntity, orig.get(1), orig.get(2));
        List<CategoryEntity> actual = categoryDao.getList();
        assertEntityListEquals(expected, actual);
    }

}
