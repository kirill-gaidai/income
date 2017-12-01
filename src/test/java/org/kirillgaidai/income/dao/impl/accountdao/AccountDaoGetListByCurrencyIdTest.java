package org.kirillgaidai.income.dao.impl.accountdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;

import java.util.Collections;
import java.util.List;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

public class AccountDaoGetListByCurrencyIdTest extends AccountDaoBaseTest {

    /**
     * Test empty list
     *
     * @throws Exception exception
     */
    @Test
    public void testEmpty() throws Exception {
        Integer currencyId = 4;

        List<AccountEntity> expected = Collections.emptyList();
        List<AccountEntity> actual = accountDao.getList(currencyId);
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Integer currencyId = 5;

        List<AccountEntity> expected = Collections.singletonList(orig.get(0));
        List<AccountEntity> actual = accountDao.getList(currencyId);
        assertEntityListEquals(expected, actual);
    }

}
