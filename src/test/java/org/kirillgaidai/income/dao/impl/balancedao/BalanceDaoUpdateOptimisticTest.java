package org.kirillgaidai.income.dao.impl.balancedao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.entity.IGenericEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.BalanceDao#update(IGenericEntity, IGenericEntity)} test
 *
 * @author Kirill Gaidai
 */
public class BalanceDaoUpdateOptimisticTest extends BalanceDaoBaseTest {

    /**
     * Test account id changed
     *
     * @throws Exception exception
     */
    @Test
    public void testAccountIdChanged() throws Exception {
        BalanceEntity newEntity = new BalanceEntity(ACCOUNT_ID_1, DAY_1, new BigDecimal("0.2"), false);
        BalanceEntity oldEntity = new BalanceEntity(ACCOUNT_ID_4, DAY_1, new BigDecimal("0.2"), false);
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test day changed
     *
     * @throws Exception exception
     */
    @Test
    public void testDayChanged() throws Exception {
        BalanceEntity newEntity = new BalanceEntity(ACCOUNT_ID_1, DAY_1, new BigDecimal("0.2"), false);
        BalanceEntity oldEntity = new BalanceEntity(ACCOUNT_ID_1, DAY_0, new BigDecimal("0.2"), false);
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test amount changed
     *
     * @throws Exception exception
     */
    @Test
    public void testAmountChanged() throws Exception {
        BalanceEntity newEntity = new BalanceEntity(ACCOUNT_ID_1, DAY_1, new BigDecimal("0.2"), false);
        BalanceEntity oldEntity = new BalanceEntity(ACCOUNT_ID_1, DAY_1, new BigDecimal("0.3"), false);
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test manual changed
     *
     * @throws Exception exception
     */
    @Test
    public void testManualChanged() throws Exception {
        BalanceEntity newEntity = new BalanceEntity(ACCOUNT_ID_1, DAY_1, new BigDecimal("0.2"), false);
        BalanceEntity oldEntity = new BalanceEntity(ACCOUNT_ID_1, DAY_0, new BigDecimal("0.2"), true);
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testAccountIdNotFound() throws Exception {
        BalanceEntity newEntity = new BalanceEntity(ACCOUNT_ID_4, DAY_1, new BigDecimal("0.2"), false);
        BalanceEntity oldEntity = new BalanceEntity(ACCOUNT_ID_1, DAY_1, new BigDecimal("0.2"), false);
        testFailure(newEntity, oldEntity);
    }

    /**
     * @throws Exception exception
     */
    @Test
    public void testDayNotFound() throws Exception {
        BalanceEntity newEntity = new BalanceEntity(ACCOUNT_ID_1, DAY_0, new BigDecimal("0.2"), false);
        BalanceEntity oldEntity = new BalanceEntity(ACCOUNT_ID_1, DAY_1, new BigDecimal("0.2"), false);
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        BalanceEntity newEntity = new BalanceEntity(ACCOUNT_ID_1, DAY_2, new BigDecimal("0.8"), false);
        BalanceEntity oldEntity = orig.get(0);
        List<BalanceEntity> expected = new ArrayList<>(orig);
        expected.set(0, newEntity);
        int affectedRows = balanceDao.update(newEntity, oldEntity);
        assertEquals(1, affectedRows);
        List<BalanceEntity> actual = balanceDao.getList();
        assertEntityListEquals(expected, actual);
    }

    private void testFailure(BalanceEntity newEntity, BalanceEntity oldEntity) throws Exception {
        int affectedRows = balanceDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<BalanceEntity> actual = balanceDao.getList();
        assertEntityListEquals(orig, actual);
    }

}
