package org.kirillgaidai.income.dao.impl.operationdao;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OperationDaoGetCountByAccountIdAndDayTest extends OperationDaoBaseTest {

    /**
     * Test account not found
     *
     * @throws Exception exception
     */
    @Test
    public void testAccountNotFound() throws Exception {
        int expected = 0;
        int actual = operationDao.getCountByAccountIdAndDay(ACCOUNT_ID_4, DAY_1);
        assertEquals(expected, actual);
    }

    /**
     * Test day not found
     *
     * @throws Exception exception
     */
    @Test
    public void testDayNotFound() throws Exception {
        int expected = 0;
        int actual = operationDao.getCountByAccountIdAndDay(ACCOUNT_ID_1, DAY_0);
        assertEquals(expected, actual);
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        int expected = 2;
        int actual = operationDao.getCountByAccountIdAndDay(ACCOUNT_ID_1, DAY_1);
        assertEquals(expected, actual);
    }

}
