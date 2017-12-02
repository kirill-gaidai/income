package org.kirillgaidai.income.dao.impl.operationdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.mockito.internal.util.collections.Sets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

public class OperationDaoGetListByAccountIdsAndCategoryIdsForIntervalTest extends OperationDaoBaseTest {

    /**
     * Test account ids are empty
     *
     * @throws Exception exception
     */
    @Test
    public void testAccountIdsEmpty() throws Exception {
        List<OperationEntity> expected = Collections.emptyList();
        List<OperationEntity> actual =
                operationDao.getList(Collections.emptySet(), Collections.emptySet(), DAY_0, DAY_3);
        assertEntityListEquals(expected, actual);
    }


    /**
     * Test category ids empty
     *
     * @throws Exception exception
     */
    @Test
    public void testCategoryIdsEmpty() throws Exception {
        List<OperationEntity> expected = new ArrayList<>(orig.subList(0, 8));
        List<OperationEntity> actual =
                operationDao.getList(Sets.newSet(ACCOUNT_ID_1, ACCOUNT_ID_2), Collections.emptySet(), DAY_0, DAY_3);
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test category ids is not empty
     *
     * @throws Exception exception
     */
    @Test
    public void testCategoryIdsNotEmpty() throws Exception {
        List<OperationEntity> expected = new ArrayList<>(orig.subList(0, 2));
        expected.addAll(orig.subList(4, 6));
        List<OperationEntity> actual = operationDao.getList(
                Sets.newSet(ACCOUNT_ID_1, ACCOUNT_ID_2), Collections.singleton(CATEGORY_ID_1), DAY_0, DAY_3);
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test no operations in interval
     *
     * @throws Exception exception
     */
    @Test
    public void testNoOperationsInInterval() throws Exception {
        List<OperationEntity> expected = Collections.emptyList();
        List<OperationEntity> actual =
                operationDao.getList(Sets.newSet(ACCOUNT_ID_1, ACCOUNT_ID_2), Collections.emptySet(), DAY_0, DAY_0);
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test some operations are in interval
     *
     * @throws Exception exception
     */
    @Test
    public void testSomeOperationsInInterval() throws Exception {
        List<OperationEntity> expected = new ArrayList<>();
        for (int index = 0; index < orig.size(); index += 2) {
            expected.add(orig.get(index));
        }
        List<OperationEntity> actual = operationDao.getList(
                Sets.newSet(ACCOUNT_ID_1, ACCOUNT_ID_2, ACCOUNT_ID_3), Collections.emptySet(), DAY_0, DAY_1);
        assertEntityListEquals(expected, actual);
    }

}
