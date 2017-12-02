package org.kirillgaidai.income.dao.impl.operationdao;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OperationDaoGetCountByCategoryIdTest extends OperationDaoBaseTest {

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        int expected = 0;
        int actual = operationDao.getCountByCategoryId(CATEGORY_ID_3);
        assertEquals(expected, actual);
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        int expected = 6;
        int actual = operationDao.getCountByCategoryId(CATEGORY_ID_1);
        assertEquals(expected, actual);
    }

}
