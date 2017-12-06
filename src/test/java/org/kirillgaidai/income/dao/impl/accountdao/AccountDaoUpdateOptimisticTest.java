package org.kirillgaidai.income.dao.impl.accountdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;

import static org.junit.Assert.assertEquals;

public class AccountDaoUpdateOptimisticTest extends AccountDaoBaseTest {

    /**
     * Test old id not equal
     *
     * @throws Exception exception
     */
    @Test
    public void testOldIdNotEqual() throws Exception {
        AccountEntity newEntity = new AccountEntity(1, 4, "04", "account4");
        AccountEntity oldEntity = new AccountEntity(0, 7, "03", "account3");
        int affectedRows = accountDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
    }

    /**
     * Test old currency id not equal
     *
     * @throws Exception exception
     */
    @Test
    public void testOldCurrencyIdNotEqual() throws Exception {
        AccountEntity newEntity = new AccountEntity(1, 4, "04", "account4");
        AccountEntity oldEntity = new AccountEntity(1, 0, "03", "account3");
        int affectedRows = accountDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
    }

    /**
     * Test old sort not equal
     *
     * @throws Exception exception
     */
    @Test
    public void testOldSortNotEqual() throws Exception {
        AccountEntity newEntity = new AccountEntity(1, 4, "04", "account4");
        AccountEntity oldEntity = new AccountEntity(1, 7, "00", "account3");
        int affectedRows = accountDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
    }

    /**
     * Test old title not equal
     *
     * @throws Exception exception
     */
    @Test
    public void testOldTitleNotEqual() throws Exception {
        AccountEntity newEntity = new AccountEntity(1, 4, "04", "account4");
        AccountEntity oldEntity = new AccountEntity(1, 7, "03", "account0");
        int affectedRows = accountDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        AccountEntity newEntity = new AccountEntity(0, 4, "04", "account4");
        AccountEntity oldEntity = new AccountEntity(1, 7, "03", "account3");
        int affectedRows = accountDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        AccountEntity newEntity = new AccountEntity(1, 4, "04", "account4");
        AccountEntity oldEntity = new AccountEntity(1, 7, "03", "account3");
        int affectedRows = accountDao.update(newEntity, oldEntity);
        assertEquals(1, affectedRows);
    }

}
