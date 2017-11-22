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
 * {@link org.kirillgaidai.income.dao.impl.OperationDao#getList(Set, LocalDate, Integer)} test
 *
 * @author Kirill Gaidai
 */
public class OperationDaoGetListForDayAndCategoryTest extends OperationDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Set<Integer> accountIds = Sets.newSet(ACCOUNT_ID_1, ACCOUNT_ID_2);
        List<OperationEntity> expected = Arrays.asList(orig.get(0), orig.get(4));
        List<OperationEntity> actual = operationDao.getList(accountIds, DAY_1, CATEGORY_ID_1);
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test accounts ids empty
     *
     * @throws Exception exception
     */
    @Test
    public void testAccountIdsEmpty() throws Exception {
        Set<Integer> accountIds = Collections.emptySet();
        List<OperationEntity> expected = Collections.emptyList();
        List<OperationEntity> actual = operationDao.getList(accountIds, DAY_1, CATEGORY_ID_1);
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test day empty
     *
     * @throws Exception exception
     */
    @Test
    public void testGetEntityList_AccountIdsForDayCategoryEmpty() throws Exception {
        Set<Integer> accountIds = Sets.newSet(ACCOUNT_ID_1, ACCOUNT_ID_2);
        List<OperationEntity> expected = Collections.emptyList();
        List<OperationEntity> actual = operationDao.getList(accountIds, DAY_0, CATEGORY_ID_1);
        assertEntityListEquals(expected, actual);
    }

}
