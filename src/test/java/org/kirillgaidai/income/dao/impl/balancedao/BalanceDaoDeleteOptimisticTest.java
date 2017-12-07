package org.kirillgaidai.income.dao.impl.balancedao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

public class BalanceDaoDeleteOptimisticTest extends BalanceDaoBaseTest {

    /**
     * Test account id changed
     *
     * @throws Exception exception
     */
    @Test
    public void testAccountIdChanged() throws Exception {
        BalanceEntity entity = new BalanceEntity(ACCOUNT_ID_4, DAY_1, new BigDecimal("0.2"), false);
        testDeleteFailure(entity);
    }

    /**
     * Test day changed
     *
     * @throws Exception exception
     */
    @Test
    public void testDayChanged() throws Exception {
        BalanceEntity entity = new BalanceEntity(ACCOUNT_ID_1, DAY_3, new BigDecimal("0.2"), false);
        testDeleteFailure(entity);
    }

    /**
     * Test amount changed
     *
     * @throws Exception exception
     */
    @Test
    public void testAmountChanged() throws Exception {
        BalanceEntity entity = new BalanceEntity(ACCOUNT_ID_1, DAY_1, new BigDecimal("0.3"), false);
        testDeleteFailure(entity);
    }

    /**
     * Test manual changed
     *
     * @throws Exception exception
     */
    @Test
    public void testManualChanged() throws Exception {
        BalanceEntity entity = new BalanceEntity(ACCOUNT_ID_1, DAY_1, new BigDecimal("0.2"), true);
        testDeleteFailure(entity);
    }

    /**
     * Test dependent operation exist at this day
     *
     * @throws Exception exception
     */
    @Test
    public void testDependentOperationExistThisDay() throws Exception {
        OperationEntity operationEntity = new OperationEntity(1, ACCOUNT_ID_1, 3, DAY_2, new BigDecimal("0.1"), "note");
        BalanceEntity entity = new BalanceEntity(ACCOUNT_ID_1, DAY_2, new BigDecimal("0.1"), true);
        insertOperationEntity(operationEntity);
        testDeleteFailure(entity);
    }

    /**
     * Test dependent operation exist after day
     *
     * @throws Exception exception
     */
    @Test
    public void testDependentOperationExistAfterDay() throws Exception {
        OperationEntity operationEntity = new OperationEntity(1, ACCOUNT_ID_1, 3, DAY_2, new BigDecimal("0.1"), "note");
        BalanceEntity entity = new BalanceEntity(ACCOUNT_ID_1, DAY_1, new BigDecimal("0.1"), true);
        insertOperationEntity(operationEntity);
        testDeleteFailure(entity);
    }

    /**
     * Test successful from middle
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessfulFromMiddle() throws Exception {
        BalanceEntity balanceEntity = new BalanceEntity(ACCOUNT_ID_1, DAY_0, new BigDecimal("0.1"), true);
        insertBalanceEntity(balanceEntity);
        OperationEntity operationEntity = new OperationEntity(1, ACCOUNT_ID_1, 3, DAY_2, new BigDecimal("0.1"), "note");
        insertOperationEntity(operationEntity);
        BalanceEntity entity = new BalanceEntity(ACCOUNT_ID_1, DAY_1, new BigDecimal("0.2"), false);
        int affectedRows = balanceDao.delete(entity);
        assertEquals(1, affectedRows);
        List<BalanceEntity> expected = new ArrayList<>();
        expected.add(orig.get(0));
        expected.add(balanceEntity);
        expected.addAll(orig.subList(2, orig.size()));
        List<BalanceEntity> actual = balanceDao.getList();
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        BalanceEntity entity = new BalanceEntity(ACCOUNT_ID_1, DAY_1, new BigDecimal("0.2"), false);
        int affectedRows = balanceDao.delete(entity);
        assertEquals(1, affectedRows);
        List<BalanceEntity> expected = new ArrayList<>();
        expected.add(orig.get(0));
        expected.addAll(orig.subList(2, orig.size()));
        List<BalanceEntity> actual = balanceDao.getList();
        assertEntityListEquals(expected, actual);
    }

    private void testDeleteFailure(BalanceEntity entity) throws Exception {
        int affectedRows = balanceDao.delete(entity);
        assertEquals(0, affectedRows);
        List<BalanceEntity> actual = balanceDao.getList();
        assertEntityListEquals(orig, actual);
    }

}
