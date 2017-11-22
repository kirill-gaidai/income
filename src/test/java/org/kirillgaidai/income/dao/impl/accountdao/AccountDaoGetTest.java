package org.kirillgaidai.income.dao.impl.accountdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;

import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.AccountDao#get(Integer)} test
 *
 * @author Kirill Gaidai
 */
public class AccountDaoGetTest extends AccountDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        AccountEntity expected = orig.get(0);
        AccountEntity actual = accountDao.get(3);
        assertEntityEquals(expected, actual);
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        AccountEntity actual = accountDao.get(0);
        assertNull(actual);
    }

}
