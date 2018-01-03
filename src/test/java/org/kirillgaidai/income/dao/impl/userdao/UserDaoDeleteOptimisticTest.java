package org.kirillgaidai.income.dao.impl.userdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

public class UserDaoDeleteOptimisticTest extends UserDaoBaseTest {

    /**
     * Test id changed
     *
     * @throws Exception exception
     */
    @Test
    public void testIdChanged() throws Exception {
        UserEntity entity = new UserEntity(4, "blocked", "pass", false, true, "token2",
                LocalDateTime.of(2018, 1, 2, 9, 45, 30));
        testFailure(entity);
    }

    /**
     * Test login changed
     *
     * @throws Exception exception
     */
    @Test
    public void testLoginChanged() throws Exception {
        UserEntity entity = new UserEntity(2, "blocked!", "pass", false, true, "token2",
                LocalDateTime.of(2018, 1, 2, 9, 45, 30));
        testFailure(entity);
    }

    /**
     * Test password changed
     *
     * @throws Exception exception
     */
    @Test
    public void testPasswordChanged() throws Exception {
        UserEntity entity = new UserEntity(2, "blocked", "pass!", false, true, "token2",
                LocalDateTime.of(2018, 1, 2, 9, 45, 30));
        testFailure(entity);
    }

    /**
     * Test admin changed
     *
     * @throws Exception exception
     */
    @Test
    public void testAdminChanged() throws Exception {
        UserEntity entity = new UserEntity(2, "blocked", "pass", true, true, "token2",
                LocalDateTime.of(2018, 1, 2, 9, 45, 30));
        testFailure(entity);
    }

    /**
     * Test blocked changed
     *
     * @throws Exception exception
     */
    @Test
    public void testBlockedChanged() throws Exception {
        UserEntity entity = new UserEntity(2, "blocked", "pass", false, false, "token2",
                LocalDateTime.of(2018, 1, 2, 9, 45, 30));
        testFailure(entity);
    }

    /**
     * Test token changed
     *
     * @throws Exception exception
     */
    @Test
    public void testTokenChanged() throws Exception {
        UserEntity entity = new UserEntity(2, "blocked", "pass", false, true, "token4",
                LocalDateTime.of(2018, 1, 2, 9, 45, 30));
        testFailure(entity);
    }

    /**
     * Test expired changed
     *
     * @throws Exception exception
     */
    @Test
    public void testExpiredChanged() throws Exception {
        UserEntity entity = new UserEntity(2, "blocked", "pass", false, true, "token2",
                LocalDateTime.of(2018, 1, 2, 20, 45, 30));
        testFailure(entity);
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        UserEntity entity = new UserEntity(2, "blocked", "pass", false, true, "token2",
                LocalDateTime.of(2018, 1, 2, 9, 45, 30));
        int affectedRows = userDao.delete(entity);
        assertEquals(1, affectedRows);
        List<UserEntity> expected = Arrays.asList(orig.get(0), orig.get(2));
        List<UserEntity> actual = userDao.getList();
        assertEntityListEquals(expected, actual);
    }

    private void testFailure(UserEntity entity) throws Exception {
        int affectedRows = userDao.delete(entity);
        assertEquals(0, affectedRows);
        List<UserEntity> actual = userDao.getList();
        assertEntityListEquals(orig, actual);
    }

}
