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

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        BalanceEntity newEntity = new BalanceEntity(ACCOUNT_ID_1, DAY_0, new BigDecimal("0.8"), false);
        BalanceEntity oldEntity = new BalanceEntity(ACCOUNT_ID_1, DAY_0, new BigDecimal("0.1"), true);
        int affectedRows = balanceDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<BalanceEntity> actual = balanceDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test already updated
     *
     * @throws Exception exception
     */
    @Test
    public void testAlreadyUpdated() throws Exception {
        BalanceEntity newEntity = new BalanceEntity(ACCOUNT_ID_1, DAY_2, new BigDecimal("0.8"), false);
        BalanceEntity oldEntity = new BalanceEntity(ACCOUNT_ID_1, DAY_2, new BigDecimal("0.1"), false);
        int affectedRows = balanceDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<BalanceEntity> actual = balanceDao.getList();
        assertEntityListEquals(orig, actual);
    }

}
