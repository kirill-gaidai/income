package org.kirillgaidai.income.dao.impl.accountdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

public class AccountDaoDeleteOptimisticTest extends AccountDaoBaseTest {

    /**
     * Test id changed
     *
     * @throws Exception exception
     */
    @Test
    public void testIdChanged() throws Exception {
        AccountEntity entity = new AccountEntity(0, 5, "01", "account1");
        int affectedRows = accountDao.delete(entity);
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
        AccountEntity entity = new AccountEntity(3, 0, "01", "account1");
        int affectedRows = accountDao.delete(entity);
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
        AccountEntity entity = new AccountEntity(3, 5, "00", "account1");
        int affectedRows = accountDao.delete(entity);
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
        AccountEntity entity = new AccountEntity(3, 5, "01", "account0");
        int affectedRows = accountDao.delete(entity);
        assertEquals(0, affectedRows);
        List<AccountEntity> actual = accountDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test dependent balance exist
     *
     * @throws Exception exception
     */
    @Test
    public void testDependentBalanceExist() throws Exception {
        BalanceEntity balanceEntity = new BalanceEntity(3, LocalDate.of(2017, 12, 1), new BigDecimal("10"), false);
        insertBalanceEntity(balanceEntity);
        AccountEntity entity = new AccountEntity(3, 5, "01", "account1");
        int affectedRows = accountDao.delete(entity);
        assertEquals(0, affectedRows);
        List<AccountEntity> actual = accountDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test dependent operation exists
     *
     * @throws Exception exception
     */
    @Test
    public void testDependentOperationExist() throws Exception {
        OperationEntity operationEntity =
                new OperationEntity(1, 3, 2, LocalDate.of(2017, 12, 1), new BigDecimal("10"), "note");
        insertOperationEntity(operationEntity);
        AccountEntity entity = new AccountEntity(3, 5, "01", "account1");
        int affectedRows = accountDao.delete(entity);
        assertEquals(0, affectedRows);
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        AccountEntity entity = new AccountEntity(3, 5, "01", "account1");
        int affectedRows = accountDao.delete(entity);
        assertEquals(1, affectedRows);
        List<AccountEntity> expected = Arrays.asList(orig.get(1), orig.get(2));
        List<AccountEntity> actual = accountDao.getList();
        assertEntityListEquals(expected, actual);
    }

}
