package org.kirillgaidai.income.dao.impl.accountdao;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AccountDaoGetCountByCurrencyIdTest extends AccountDaoBaseTest {

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        Integer currencyId = 4;
        int expected = 0;
        int actual = accountDao.getCountByCurrencyId(currencyId);
        assertEquals(expected, actual);
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Integer currencyId = 5;
        int expected = 1;
        int actual = accountDao.getCountByCurrencyId(currencyId);
        assertEquals(expected, actual);
    }

}
