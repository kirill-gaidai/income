package org.kirillgaidai.income.dao.impl.accountdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.AccountDao#getList()} test
 *
 * @author Kirill Gaidai
 */
public class AccountDaoGetListTest extends AccountDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        List<AccountEntity> expected = Arrays.asList(orig.get(0), orig.get(1), orig.get(2));
        List<AccountEntity> actual = accountDao.getList();
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test empty
     *
     * @throws Exception exception
     */
    @Test
    public void testEmpty() throws Exception {
        deleteAccountEntities();
        List<AccountEntity> expected = Collections.emptyList();
        List<AccountEntity> actual = accountDao.getList();
        assertEntityListEquals(expected, actual);
    }

}
