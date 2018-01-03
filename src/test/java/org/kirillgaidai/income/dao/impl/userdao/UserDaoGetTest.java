package org.kirillgaidai.income.dao.impl.userdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.UserEntity;

import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;

public class UserDaoGetTest extends UserDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        UserEntity expected = orig.get(0);
        UserEntity actual = userDao.get(1);
        assertEntityEquals(expected, actual);
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        UserEntity actual = userDao.get(0);
        assertNull(actual);
    }

}
