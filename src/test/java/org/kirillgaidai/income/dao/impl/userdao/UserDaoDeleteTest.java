package org.kirillgaidai.income.dao.impl.userdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.UserEntity;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

public class UserDaoDeleteTest extends UserDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        int affectedRows = userDao.delete(1);
        assertEquals(1, affectedRows);
        List<UserEntity> expected = orig.subList(1, 3);
        List<UserEntity> actual = userDao.getList();
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        int affectedRows = userDao.delete(0);
        assertEquals(0, affectedRows);
    }

}
