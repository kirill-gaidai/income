package org.kirillgaidai.income.dao.impl.accountdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

public class AccountDaoUpdateOptimisticTest extends AccountDaoBaseTest {

    /**
     * Test id changed
     *
     * @throws Exception exception
     */
    @Test
    public void testIdChanged() throws Exception {
        AccountEntity newEntity = new AccountEntity(3, 0, "00", "account0");
        AccountEntity oldEntity = new AccountEntity(0, 5, "01", "account1");
        int affectedRows = accountDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<AccountEntity> actual = accountDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test currency id changed
     *
     * @throws Exception exception
     */
    @Test
    public void testCurrencyIdChanged() throws Exception {
        AccountEntity newEntity = new AccountEntity(3, 0, "00", "account0");
        AccountEntity oldEntity = new AccountEntity(3, 1, "01", "account1");
        int affectedRows = accountDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<AccountEntity> actual = accountDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test sort changed
     *
     * @throws Exception exception
     */
    @Test
    public void testSortChanged() throws Exception {
        AccountEntity newEntity = new AccountEntity(3, 0, "00", "account0");
        AccountEntity oldEntity = new AccountEntity(3, 5, "00", "account1");
        int affectedRows = accountDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<AccountEntity> actual = accountDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test title changed
     *
     * @throws Exception exception
     */
    @Test
    public void testTitleChanged() throws Exception {
        AccountEntity newEntity = new AccountEntity(3, 0, "00", "account0");
        AccountEntity oldEntity = new AccountEntity(3, 5, "01", "account0");
        int affectedRows = accountDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<AccountEntity> actual = accountDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        AccountEntity newEntity = new AccountEntity(0, 0, "00", "account0");
        AccountEntity oldEntity = new AccountEntity(3, 5, "01", "account1");
        int affectedRows = accountDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<AccountEntity> actual = accountDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        AccountEntity newEntity = new AccountEntity(3, 0, "00", "account0");
        AccountEntity oldEntity = new AccountEntity(3, 5, "01", "account1");
        int affectedRows = accountDao.update(newEntity, oldEntity);
        assertEquals(1, affectedRows);
        List<AccountEntity> expected = Arrays.asList(newEntity, orig.get(1), orig.get(2));
        List<AccountEntity> actual = accountDao.getList();
        assertEntityListEquals(expected, actual);
    }

}
