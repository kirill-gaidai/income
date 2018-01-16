package org.kirillgaidai.income.dao.impl.userdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.UserEntity;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.kirillgaidai.income.utils.TestUtils.throwUnreachableException;

public class UserDaoGetByTokenTest extends UserDaoBaseTest {

    /**
     * Test empty token
     *
     * @throws Exception exception
     */
    @Test
    public void testEmptyToken() throws Exception {
        String token = "";
        UserEntity actual = userDao.getByToken(token);
        assertNull(actual);
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        String token = "token";
        UserEntity actual = userDao.getByToken(token);
        assertNull(actual);
    }

    /**
     * Test duplicate
     *
     * @throws Exception exception
     */
    @Test
    public void testDuplicate() throws Exception {
        String token = "token1";
        UserEntity duplicateEntity = new UserEntity(1, "admin2", "s3cr3t!!", true, false, token,
                LocalDateTime.of(2018, 1, 2, 15, 45, 30));
        userDao.insert(duplicateEntity);

        try {
            userDao.getByToken(token);
            throwUnreachableException();
        } catch (IllegalStateException e) {
            assertEquals("Duplicate user token in db", e.getMessage());
        }
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        String token = "token1";
        UserEntity expected = new UserEntity(1, "admin", "s3cr3t!", true, false, token,
                LocalDateTime.of(2018, 1, 2, 8, 45, 30));
        UserEntity actual = userDao.getByToken(token);
        assertEntityEquals(expected, actual);
    }

}
