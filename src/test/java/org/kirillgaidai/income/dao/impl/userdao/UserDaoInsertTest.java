package org.kirillgaidai.income.dao.impl.userdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

public class UserDaoInsertTest extends UserDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        UserEntity entity = new UserEntity(null, "new", "secret", false, true, "token4",
                LocalDateTime.of(2018, 1, 2, 11, 45, 30));
        int affectedRows = userDao.insert(entity);
        assertEquals(1, affectedRows);
        List<UserEntity> expected = new ArrayList<>(orig);
        expected.add(2, entity);
        List<UserEntity> actual = userDao.getList();
        assertEntityListEquals(expected, actual);
    }

}
