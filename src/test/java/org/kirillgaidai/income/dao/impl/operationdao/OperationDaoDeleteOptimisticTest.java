package org.kirillgaidai.income.dao.impl.operationdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.OperationEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

public class OperationDaoDeleteOptimisticTest extends OperationDaoBaseTest {

    /**
     * Test id changed
     *
     * @throws Exception exception
     */
    @Test
    public void testIdChanged() throws Exception {
        testFailure(new OperationEntity(0, ACCOUNT_ID_1, CATEGORY_ID_1, DAY_1, new BigDecimal("0.1"), "Note 1"));
    }

    /**
     * Test account id changed
     *
     * @throws Exception exception
     */
    @Test
    public void testAccountIdChanged() throws Exception {
        testFailure(new OperationEntity(0, ACCOUNT_ID_4, CATEGORY_ID_1, DAY_1, new BigDecimal("0.1"), "Note 1"));
    }

    /**
     * Test category id changed
     *
     * @throws Exception exception
     */
    @Test
    public void testCategoryIdChanged() throws Exception {
        testFailure(new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_3, DAY_1, new BigDecimal("0.1"), "Note 1"));
    }

    /**
     * Test day changed
     *
     * @throws Exception exception
     */
    @Test
    public void testDayChanged() throws Exception {
        testFailure(new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_1, DAY_0, new BigDecimal("0.1"), "Note 1"));
    }

    /**
     * Test amount changed
     *
     * @throws Exception exception
     */
    @Test
    public void testAmountChanged() throws Exception {
        testFailure(new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_1, DAY_1, new BigDecimal("0.0"), "Note 1"));
    }

    /**
     * Test note changed
     *
     * @throws Exception exception
     */
    @Test
    public void testNoteChanged() throws Exception {
        testFailure(new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_1, DAY_1, new BigDecimal("0.1"), "Note 0"));
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        OperationEntity entity =
                new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_1, DAY_1, new BigDecimal("0.1"), "Note 1");
        assertEquals(1, operationDao.delete(entity));
        assertEntityListEquals(orig.subList(1, orig.size()), operationDao.getList());
    }

    private void testFailure(OperationEntity entity) throws Exception {
        assertEquals(0, operationDao.delete(entity));
        assertEntityListEquals(orig, operationDao.getList());
    }

}
