package org.kirillgaidai.income.dao.impl.accountdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.AccountDao#getList(Set)} test
 *
 * @author Kirill Gaidai
 */
public class AccountDaoGetListByIdsTest extends AccountDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Set<Integer> ids = Sets.newSet(3, 2);
        List<AccountEntity> expected = Arrays.asList(orig.get(0), orig.get(1));
        List<AccountEntity> actual = accountDao.getList(ids);
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
        List<AccountEntity> expected = Collections.emptyList();
        List<AccountEntity> actual = accountDao.getList(ids);
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
        List<AccountEntity> expected = Collections.emptyList();
        List<AccountEntity> actual = accountDao.getList(ids);
        assertEntityListEquals(expected, actual);
    }

}
