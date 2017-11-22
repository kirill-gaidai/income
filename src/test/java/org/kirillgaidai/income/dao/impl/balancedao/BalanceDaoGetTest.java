package org.kirillgaidai.income.dao.impl.balancedao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.BalanceEntity;

import java.time.LocalDate;

import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.BalanceDao#get(Integer, LocalDate)} test
 *
 * @author Kirill Gaidai
 */
public class BalanceDaoGetTest extends BalanceDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        BalanceEntity expected = orig.get(1);
        BalanceEntity actual = balanceDao.get(ACCOUNT_ID_1, DAY_1);
        assertEntityEquals(expected, actual);
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        BalanceEntity actual = balanceDao.get(ACCOUNT_ID_1, DAY_0);
        assertNull(actual);
    }

}
