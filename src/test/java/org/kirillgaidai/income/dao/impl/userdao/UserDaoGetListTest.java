package org.kirillgaidai.income.dao.impl.userdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.UserEntity;

import java.util.Collections;
import java.util.List;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

public class UserDaoGetListTest extends UserDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        List<UserEntity> actual = userDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test empty
     *
     * @throws Exception exception
     */
    @Test
    public void testEmpty() throws Exception {
        deleteUserEntities();
        List<UserEntity> expected = Collections.emptyList();
        List<UserEntity> actual = userDao.getList();
        assertEntityListEquals(expected, actual);
    }

}
