package org.kirillgaidai.income.dao.impl.userdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

public class UserDaoUpdateTest extends UserDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        UserEntity entity = new UserEntity(2, "unblocked", "newpass", true, false, "token5",
                LocalDateTime.of(2018, 1, 2, 12, 45, 30));
        UserEntity expectedEntity = new UserEntity(2, "blocked", "newpass", true, false, "token5",
                LocalDateTime.of(2018, 1, 2, 12, 45, 30));
        int affectedRows = userDao.update(entity);
        assertEquals(1, affectedRows);
        List<UserEntity> expected = Arrays.asList(orig.get(0), expectedEntity, orig.get(2));
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
        UserEntity entity = new UserEntity(0, "unblocked", "newpass", true, false, "token5",
                LocalDateTime.of(2018, 1, 2, 12, 45, 30));
        int affectedRows = userDao.update(entity);
        assertEquals(0, affectedRows);
        List<UserEntity> actual = userDao.getList();
        assertEntityListEquals(orig, actual);
    }

}
