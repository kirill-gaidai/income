package org.kirillgaidai.income.dao.impl.operationdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.mockito.internal.util.collections.Sets;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.OperationDao#getList(Set, LocalDate)} test
 *
 * @author Kirill Gaidai
 */
public class OperationDaoGetListForDayTest extends OperationDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Set<Integer> accountIds = Sets.newSet(ACCOUNT_ID_1, ACCOUNT_ID_2);
        List<OperationEntity> expected = Arrays.asList(orig.get(0), orig.get(2), orig.get(4), orig.get(6));
        List<OperationEntity> actual = operationDao.getList(accountIds, DAY_1);
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
        List<OperationEntity> actual = operationDao.getList(accountIds, DAY_1);
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test day empty
     *
     * @throws Exception exception
     */
    @Test
    public void testDayEmpty() throws Exception {
        Set<Integer> accountIds = Sets.newSet(ACCOUNT_ID_1, ACCOUNT_ID_2);
        List<OperationEntity> expected = Collections.emptyList();
        List<OperationEntity> actual = operationDao.getList(accountIds, DAY_0);
        assertEntityListEquals(expected, actual);
    }

}
