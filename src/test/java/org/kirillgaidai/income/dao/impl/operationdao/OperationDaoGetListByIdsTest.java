package org.kirillgaidai.income.dao.impl.operationdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.OperationDao#getList(Set)} test
 *
 * @author Kirill Gaidai
 */
public class OperationDaoGetListByIdsTest extends OperationDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Set<Integer> ids = Sets.newSet(2, 7);
        List<OperationEntity> expected = Arrays.asList(orig.get(1), orig.get(6));
        List<OperationEntity> actual = operationDao.getList(ids);
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test empty
     *
     * @throws Exception exception
     */
    @Test
    public void testEmpty() throws Exception {
        Set<Integer> ids = Sets.newSet(0, -1);
        List<OperationEntity> expected = Collections.emptyList();
        List<OperationEntity> actual = operationDao.getList(ids);
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test ids empty
     *
     * @throws Exception exception
     */
    @Test
    public void testIdsEmpty() throws Exception {
        Set<Integer> ids = Collections.emptySet();
        List<OperationEntity> expected = Collections.emptyList();
        List<OperationEntity> actual = operationDao.getList(ids);
        assertEntityListEquals(expected, actual);
    }

}
