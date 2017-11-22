package org.kirillgaidai.income.dao.impl.operationdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.mockito.internal.util.collections.Sets;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.OperationDao#getList(Set, LocalDate, LocalDate)} test
 *
 * @author Kirill Gaidai
 */
public class OperationDaoGetListForIntervalTest extends OperationDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testGetEntityList_AccountIdsForInterval() throws Exception {
        Set<Integer> accountIds = Sets.newSet(ACCOUNT_ID_1, ACCOUNT_ID_2);
        List<OperationEntity> expected = orig.subList(0, 8);
        List<OperationEntity> actual = operationDao.getList(accountIds, DAY_0, DAY_3);
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test account ids empty
     *
     * @throws Exception exception
     */
    @Test
    public void testAccountIdsEmpty() throws Exception {
        Set<Integer> accountIds = Collections.emptySet();
        List<OperationEntity> expected = Collections.emptyList();
        List<OperationEntity> actual = operationDao.getList(accountIds, DAY_0, DAY_3);
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test interval empty
     *
     * @throws Exception exception
     */
    @Test
    public void testIntervalEmpty() throws Exception {
        Set<Integer> accountIds = Sets.newSet(ACCOUNT_ID_1, ACCOUNT_ID_2);
        List<OperationEntity> expected = Collections.emptyList();
        List<OperationEntity> actual = operationDao.getList(accountIds, DAY_3, DAY_3);
        assertEntityListEquals(expected, actual);
    }

}
