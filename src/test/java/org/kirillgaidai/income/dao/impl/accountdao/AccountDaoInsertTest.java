package org.kirillgaidai.income.dao.impl.accountdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.ISerialEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.AccountDao#insert(ISerialEntity)} test
 *
 * @author Kirill Gaidai
 */
public class AccountDaoInsertTest extends AccountDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        AccountEntity entity = new AccountEntity(null, 8, "04", "account4");
        List<AccountEntity> expected = Arrays.asList(orig.get(0), orig.get(1), orig.get(2), entity);
        int affectedRows = accountDao.insert(entity);
        assertEquals(1, affectedRows);
        assertEquals(Integer.valueOf(4), entity.getId());
        List<AccountEntity> actual = accountDao.getList();
        assertEntityListEquals(expected, actual);
    }

}
