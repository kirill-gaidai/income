package org.kirillgaidai.income.dao.impl.categorydao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

public class CategoryDaoDeleteOptimisticTest extends CategoryDaoBaseTest {

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        CategoryEntity entity = new CategoryEntity(0, "01", "category1");
        int affectedRows = categoryDao.delete(entity);
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
        CategoryEntity entity = new CategoryEntity(3, "00", "category1");
        int affectedRows = categoryDao.delete(entity);
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
        CategoryEntity entity = new CategoryEntity(3, "01", "category0");
        int affectedRows = categoryDao.delete(entity);
        assertEquals(0, affectedRows);
        List<CategoryEntity> actual = categoryDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test dependent operations exist
     *
     * @throws Exception exception
     */
    @Test
    public void testDependentOperationsExist() throws Exception {
        OperationEntity operationEntity =
                new OperationEntity(1, 2, 3, LocalDate.of(2017, 12, 1), new BigDecimal("10"), "note");
        insertOperationEntity(operationEntity);
        CategoryEntity entity = new CategoryEntity(3, "01", "category1");
        int affectedRows = categoryDao.delete(entity);
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
        CategoryEntity entity = new CategoryEntity(3, "01", "category1");
        int affectedRows = categoryDao.delete(entity);
        assertEquals(1, affectedRows);
        List<CategoryEntity> expected = Arrays.asList(orig.get(1), orig.get(2));
        List<CategoryEntity> actual = categoryDao.getList();
        assertEntityListEquals(expected, actual);
    }

}
