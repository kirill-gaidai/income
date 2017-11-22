package org.kirillgaidai.income.dao.impl.accountdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.IGenericEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.AccountDao#update(IGenericEntity)} test
 *
 * @author Kirill Gaidai
 */
public class AccountDaoUpdateTest extends AccountDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        AccountEntity entity = new AccountEntity(3, 8, "04", "account4");
        List<AccountEntity> expected = Arrays.asList(orig.get(1), orig.get(2), entity);
        int affectedRows = accountDao.update(entity);
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
        AccountEntity entity = new AccountEntity(4, 8, "04", "account4");
        int affectedRows = accountDao.update(entity);
        assertEquals(0, affectedRows);
        List<AccountEntity> actual = accountDao.getList();
        assertEntityListEquals(orig, actual);
    }

}
