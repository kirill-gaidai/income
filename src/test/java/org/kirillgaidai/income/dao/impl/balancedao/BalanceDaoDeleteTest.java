package org.kirillgaidai.income.dao.impl.balancedao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.BalanceEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.BalanceDao#delete(Integer, LocalDate)} test
 *
 * @author Kirill Gaidai
 */
public class BalanceDaoDeleteTest extends BalanceDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        List<BalanceEntity> expected = new ArrayList<>(orig);
        expected.remove(0);
        int affectedRows = balanceDao.delete(ACCOUNT_ID_1, DAY_2);
        assertEquals(1, affectedRows);
        List<BalanceEntity> actual = balanceDao.getList();
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        int affectedRows = balanceDao.delete(ACCOUNT_ID_1, DAY_0);
        assertEquals(0, affectedRows);
        List<BalanceEntity> actual = balanceDao.getList();
        assertEntityListEquals(orig, actual);
    }

}
