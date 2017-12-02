package org.kirillgaidai.income.dao.impl.operationdao;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OperationDaoGetCountByAccountIdTest extends OperationDaoBaseTest {

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        int expected = 0;
        int actual = operationDao.getCountByAccountId(ACCOUNT_ID_4);
        assertEquals(expected, actual);
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        int expected = 4;
        int actual = operationDao.getCountByAccountId(ACCOUNT_ID_1);
        assertEquals(expected, actual);
    }

}
