package org.kirillgaidai.income.dao.impl.userdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

public class UserDaoUpdateOptimisticTest extends UserDaoBaseTest {

    /**
     * Test id changed
     *
     * @throws Exception exception
     */
    @Test
    public void testIdChanged() throws Exception {
        UserEntity newEntity = new UserEntity(2, "blocked", "newpass", true, false, "token5",
                LocalDateTime.of(2018, 1, 2, 12, 45, 30));
        UserEntity oldEntity = new UserEntity(4, "blocked", "pass", false, true, "token2",
                LocalDateTime.of(2018, 1, 2, 9, 45, 30));
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test login changed
     *
     * @throws Exception exception
     */
    @Test
    public void testLoginChanged() throws Exception {
        UserEntity newEntity = new UserEntity(2, "blocked", "newpass", true, false, "token5",
                LocalDateTime.of(2018, 1, 2, 12, 45, 30));
        UserEntity oldEntity = new UserEntity(2, "blocked!", "pass", false, true, "token2",
                LocalDateTime.of(2018, 1, 2, 9, 45, 30));
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test password changed
     *
     * @throws Exception exception
     */
    @Test
    public void testPasswordChanged() throws Exception {
        UserEntity newEntity = new UserEntity(2, "blocked", "newpass", true, false, "token5",
                LocalDateTime.of(2018, 1, 2, 12, 45, 30));
        UserEntity oldEntity = new UserEntity(2, "blocked", "pass!", false, true, "token2",
                LocalDateTime.of(2018, 1, 2, 9, 45, 30));
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test admin changed
     *
     * @throws Exception exception
     */
    @Test
    public void testAdminChanged() throws Exception {
        UserEntity newEntity = new UserEntity(2, "blocked", "newpass", true, false, "token5",
                LocalDateTime.of(2018, 1, 2, 12, 45, 30));
        UserEntity oldEntity = new UserEntity(2, "blocked", "pass", true, true, "token2",
                LocalDateTime.of(2018, 1, 2, 9, 45, 30));
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test blocked changed
     *
     * @throws Exception exception
     */
    @Test
    public void testBlockedChanged() throws Exception {
        UserEntity newEntity = new UserEntity(2, "blocked", "newpass", true, false, "token5",
                LocalDateTime.of(2018, 1, 2, 12, 45, 30));
        UserEntity oldEntity = new UserEntity(2, "blocked", "pass", false, false, "token2",
                LocalDateTime.of(2018, 1, 2, 9, 45, 30));
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test token changed
     *
     * @throws Exception exception
     */
    @Test
    public void testTokenChanged() throws Exception {
        UserEntity newEntity = new UserEntity(2, "blocked", "newpass", true, false, "token5",
                LocalDateTime.of(2018, 1, 2, 12, 45, 30));
        UserEntity oldEntity = new UserEntity(2, "blocked", "pass", false, true, "token!",
                LocalDateTime.of(2018, 1, 2, 9, 45, 30));
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test expires changed
     *
     * @throws Exception exception
     */
    @Test
    public void testExpiresChanged() throws Exception {
        UserEntity newEntity = new UserEntity(2, "blocked", "newpass", true, false, "token5",
                LocalDateTime.of(2018, 1, 2, 12, 45, 30));
        UserEntity oldEntity = new UserEntity(2, "blocked", "pass", false, true, "token2",
                LocalDateTime.of(2018, 1, 2, 2, 45, 30));
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test id not found
     *
     * @throws Exception exception
     */
    @Test
    public void testIdNotFound() throws Exception {
        UserEntity newEntity = new UserEntity(4, "blocked", "newpass", true, false, "token5",
                LocalDateTime.of(2018, 1, 2, 12, 45, 30));
        UserEntity oldEntity = new UserEntity(2, "blocked", "pass", false, true, "token2",
                LocalDateTime.of(2018, 1, 2, 9, 45, 30));
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test login not found
     *
     * @throws Exception exception
     */
    @Test
    public void testLoginNotFound() throws Exception {
        UserEntity newEntity = new UserEntity(2, "blocked!", "newpass", true, false, "token5",
                LocalDateTime.of(2018, 1, 2, 12, 45, 30));
        UserEntity oldEntity = new UserEntity(2, "blocked", "pass", false, true, "token2",
                LocalDateTime.of(2018, 1, 2, 9, 45, 30));
        testFailure(newEntity, oldEntity);
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        UserEntity newEntity = new UserEntity(2, "blocked", "newpass", true, false, "token5",
                LocalDateTime.of(2018, 1, 2, 12, 45, 30));
        UserEntity oldEntity = new UserEntity(2, "blocked", "pass", false, true, "token2",
                LocalDateTime.of(2018, 1, 2, 9, 45, 30));
        int affectedRows = userDao.update(newEntity, oldEntity);
        assertEquals(1, affectedRows);
        List<UserEntity> expected = Arrays.asList(orig.get(0), newEntity, orig.get(2));
        List<UserEntity> actual = userDao.getList();
        assertEntityListEquals(expected, actual);
    }

    private void testFailure(UserEntity newEntity, UserEntity oldEntity) throws Exception {
        int affectedRows = userDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<UserEntity> actual = userDao.getList();
        assertEntityListEquals(orig, actual);
    }

}
