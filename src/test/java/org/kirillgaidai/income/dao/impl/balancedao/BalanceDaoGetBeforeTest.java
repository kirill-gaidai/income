package org.kirillgaidai.income.dao.impl.balancedao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.BalanceEntity;

import java.time.LocalDate;

import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.BalanceDao#getBefore(Integer, LocalDate)} test
 *
 * @author Kirill Gaidai
 */
public class BalanceDaoGetBeforeTest extends BalanceDaoBaseTest {

    /**
     * Test success
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccess() throws Exception {
        BalanceEntity expected = orig.get(1);
        BalanceEntity actual = balanceDao.getBefore(ACCOUNT_ID_1, DAY_2);
        assertEntityEquals(expected, actual);
    }

    /**
     * Test not found before
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        BalanceEntity actual = balanceDao.getBefore(ACCOUNT_ID_1, DAY_1);
        assertNull(actual);
    }


}
