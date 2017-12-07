package org.kirillgaidai.income.dao.impl.operationdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.OperationEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

public class OperationDaoUpdateOptimisticTest extends OperationDaoBaseTest {

    /**
     * Test id changed
     *
     * @throws Exception exception
     */
    @Test
    public void testIdChanged() throws Exception {
        OperationEntity newEntity =
                new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_3, DAY_1, new BigDecimal("0.3"), "Note 0");
        OperationEntity oldEntity =
                new OperationEntity(0, ACCOUNT_ID_1, CATEGORY_ID_1, DAY_1, new BigDecimal("0.1"), "Note 1");
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test account id changed
     *
     * @throws Exception exception
     */
    @Test
    public void testAccountIdChanged() throws Exception {
        OperationEntity newEntity =
                new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_3, DAY_1, new BigDecimal("0.3"), "Note 0");
        OperationEntity oldEntity =
                new OperationEntity(1, ACCOUNT_ID_4, CATEGORY_ID_1, DAY_1, new BigDecimal("0.1"), "Note 1");
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test category id changed
     *
     * @throws Exception exception
     */
    @Test
    public void testCategoryIdChanged() throws Exception {
        OperationEntity newEntity =
                new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_3, DAY_1, new BigDecimal("0.3"), "Note 0");
        OperationEntity oldEntity =
                new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_2, DAY_1, new BigDecimal("0.1"), "Note 1");
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test day changed
     *
     * @throws Exception exception
     */
    @Test
    public void testDayChanged() throws Exception {
        OperationEntity newEntity =
                new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_3, DAY_1, new BigDecimal("0.3"), "Note 0");
        OperationEntity oldEntity =
                new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_1, DAY_0, new BigDecimal("0.1"), "Note 1");
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test amount changed
     *
     * @throws Exception exception
     */
    @Test
    public void testAmountChanged() throws Exception {
        OperationEntity newEntity =
                new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_3, DAY_1, new BigDecimal("0.3"), "Note 0");
        OperationEntity oldEntity =
                new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_1, DAY_1, new BigDecimal("0.5"), "Note 1");
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test note changed
     *
     * @throws Exception exception
     */
    @Test
    public void testNoteChanged() throws Exception {
        OperationEntity newEntity =
                new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_3, DAY_1, new BigDecimal("0.3"), "Note 0");
        OperationEntity oldEntity =
                new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_1, DAY_1, new BigDecimal("0.1"), "Note X");
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test id not found
     *
     * @throws Exception exception
     */
    @Test
    public void testIdNotFound() throws Exception {
        OperationEntity newEntity =
                new OperationEntity(0, ACCOUNT_ID_1, CATEGORY_ID_3, DAY_1, new BigDecimal("0.3"), "Note 0");
        OperationEntity oldEntity =
                new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_1, DAY_1, new BigDecimal("0.1"), "Note 1");
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test account id not found
     *
     * @throws Exception exception
     */
    @Test
    public void testAccountIdNotFound() throws Exception {
        OperationEntity newEntity =
                new OperationEntity(1, ACCOUNT_ID_4, CATEGORY_ID_3, DAY_1, new BigDecimal("0.3"), "Note 0");
        OperationEntity oldEntity =
                new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_1, DAY_1, new BigDecimal("0.1"), "Note 1");
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test day not found
     *
     * @throws Exception exception
     */
    @Test
    public void testDayNotFound() throws Exception {
        OperationEntity newEntity =
                new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_3, DAY_0, new BigDecimal("0.3"), "Note 0");
        OperationEntity oldEntity =
                new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_1, DAY_1, new BigDecimal("0.1"), "Note 1");
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        OperationEntity newEntity =
                new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_3, DAY_1, new BigDecimal("0.3"), "Note 0");
        OperationEntity oldEntity =
                new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_1, DAY_1, new BigDecimal("0.1"), "Note 1");
        List<OperationEntity> expected = new ArrayList<>();
        expected.add(0, newEntity);
        expected.addAll(orig.subList(1, orig.size()));
        assertEquals(1, operationDao.update(newEntity, oldEntity));
        assertEntityListEquals(expected, operationDao.getList());
    }

    private void testFailure(OperationEntity newEntity, OperationEntity oldEntity) throws Exception {
        assertEquals(0, operationDao.update(newEntity, oldEntity));
        assertEntityListEquals(orig, operationDao.getList());
    }

}
