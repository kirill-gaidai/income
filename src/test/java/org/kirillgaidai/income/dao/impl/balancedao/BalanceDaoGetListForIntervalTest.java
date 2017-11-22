package org.kirillgaidai.income.dao.impl.balancedao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.mockito.internal.util.collections.Sets;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.BalanceDao#getList(Set, LocalDate, LocalDate)} test
 *
 * @author Kirill Gaidai
 */
public class BalanceDaoGetListForIntervalTest extends BalanceDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Set<Integer> accountIds = Sets.newSet(ACCOUNT_ID_1, ACCOUNT_ID_2);
        List<BalanceEntity> expected = Arrays.asList(orig.get(0), orig.get(2));
        List<BalanceEntity> actual = balanceDao.getList(accountIds, DAY_2, DAY_3);
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
        List<BalanceEntity> expected = Collections.emptyList();
        List<BalanceEntity> actual = balanceDao.getList(accountIds, DAY_0, DAY_3);
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
        List<BalanceEntity> expected = Collections.emptyList();
        List<BalanceEntity> actual = balanceDao.getList(accountIds, DAY_3, DAY_3);
        assertEntityListEquals(expected, actual);
    }

}
