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
 * {@link org.kirillgaidai.income.dao.impl.BalanceDao#getList(Set, LocalDate)} test
 *
 * @author Kirill Gaidai
 */
public class BalanceDaoGetListForDayTest extends BalanceDaoBaseTest {

    /**
     * Test empty
     *
     * @throws Exception exception
     */
    @Test
    public void testEmpty() throws Exception {
        List<BalanceEntity> expected = Collections.emptyList();
        List<BalanceEntity> actual = balanceDao.getList(Collections.emptySet(), DAY_1);
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test balance at this day
     *
     * @throws Exception exception
     */
    @Test
    public void testLastThis() throws Exception {
        List<BalanceEntity> expected = Arrays.asList(orig.get(1), orig.get(3));
        List<BalanceEntity> actual = balanceDao.getList(Sets.newSet(ACCOUNT_ID_1, ACCOUNT_ID_2), DAY_1);
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test no balance before day
     *
     * @throws Exception exception
     */
    @Test
    public void testLastEmpty() throws Exception {
        List<BalanceEntity> expected = Collections.emptyList();
        List<BalanceEntity> actual = balanceDao.getList(Sets.newSet(ACCOUNT_ID_1, ACCOUNT_ID_2), DAY_0);
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test balance before day
     *
     * @throws Exception exception
     */
    @Test
    public void testLastBefore() throws Exception {
        List<BalanceEntity> expected = Arrays.asList(orig.get(0), orig.get(2));
        List<BalanceEntity> actual = balanceDao.getList(Sets.newSet(ACCOUNT_ID_1, ACCOUNT_ID_2), DAY_3);
        assertEntityListEquals(expected, actual);
    }

}
