package org.kirillgaidai.income.dao.impl.accountdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class AccountDaoDeleteOptimisticTest extends AccountDaoBaseTest {

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        AccountEntity entity = new AccountEntity(0, 7, "03", "account3");
        int affectedRows = accountDao.delete(entity);
        assertEquals(0, affectedRows);
    }

    /**
     * Test old currency id not equal
     *
     * @throws Exception exception
     */
    @Test
    public void testCurrencyIdChanged() throws Exception {
        AccountEntity entity = new AccountEntity(1, 0, "03", "account3");
        int affectedRows = accountDao.delete(entity);
        assertEquals(0, affectedRows);
    }

    /**
     * Test old sort not equal
     *
     * @throws Exception exception
     */
    @Test
    public void testSortChanged() throws Exception {
        AccountEntity entity = new AccountEntity(1, 7, "00", "account3");
        int affectedRows = accountDao.delete(entity);
        assertEquals(0, affectedRows);
    }

    /**
     * Test old title not equal
     *
     * @throws Exception exception
     */
    @Test
    public void testTitleChanged() throws Exception {
        AccountEntity entity = new AccountEntity(1, 7, "03", "account0");
        int affectedRows = accountDao.delete(entity);
        assertEquals(0, affectedRows);
    }

    /**
     * Test dependent balance exists
     *
     * @throws Exception exception
     */
    @Test
    public void testDependentBalanceExist() throws Exception {
        BalanceEntity balanceEntity = new BalanceEntity(1, LocalDate.of(2017, 12, 1), new BigDecimal("10"), false);
        insertBalanceEntity(balanceEntity);
        AccountEntity entity = new AccountEntity(1, 7, "03", "account3");
        int affectedRows = accountDao.delete(entity);
        assertEquals(0, affectedRows);
    }

    /**
     * Test dependent operation exists
     *
     * @throws Exception exception
     */
    @Test
    public void testDependentOperationExist() throws Exception {
        OperationEntity operationEntity =
                new OperationEntity(2, 1, 3, LocalDate.of(2017, 12, 1), new BigDecimal("10"), "note");
        insertOperationEntity(operationEntity);
        AccountEntity entity = new AccountEntity(1, 7, "03", "account3");
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
        AccountEntity entity = new AccountEntity(1, 7, "03", "account3");
        int affectedRows = accountDao.delete(entity);
        assertEquals(1, affectedRows);
    }

}
