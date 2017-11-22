package org.kirillgaidai.income.dao.impl.balancedao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.BalanceEntity;

import java.time.LocalDate;

import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.BalanceDao#getAfter(Integer, LocalDate)} test
 *
 * @author Kirill Gaidai
 */
public class BalanceDaoGetAfterTest extends BalanceDaoBaseTest {

    /**
     * Test success
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccess() throws Exception {
        BalanceEntity expected = orig.get(0);
        BalanceEntity actual = balanceDao.getAfter(ACCOUNT_ID_1, DAY_1);
        assertEntityEquals(expected, actual);
    }

    /**
     * Test not found after
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        BalanceEntity actual = balanceDao.getAfter(ACCOUNT_ID_1, DAY_2);
        assertNull(actual);
    }

}
