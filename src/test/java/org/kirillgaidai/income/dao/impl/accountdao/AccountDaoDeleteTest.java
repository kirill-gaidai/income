package org.kirillgaidai.income.dao.impl.accountdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.AccountDao#delete(Integer)} test
 *
 * @author Kirill Gaidai
 */
public class AccountDaoDeleteTest extends AccountDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        List<AccountEntity> expected = Arrays.asList(orig.get(1), orig.get(2));
        int affectedRows = accountDao.delete(3);
        assertEquals(1, affectedRows);
        List<AccountEntity> actual = accountDao.getList();
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        int affectedRows = accountDao.delete(0);
        assertEquals(0, affectedRows);
        List<AccountEntity> actual = accountDao.getList();
        assertEntityListEquals(orig, actual);
    }

}
