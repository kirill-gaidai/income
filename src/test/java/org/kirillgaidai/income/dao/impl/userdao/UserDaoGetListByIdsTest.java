package org.kirillgaidai.income.dao.impl.userdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.UserEntity;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

public class UserDaoGetListByIdsTest extends UserDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Set<Integer> ids = Sets.newSet(1, 3);
        List<UserEntity> expected = Arrays.asList(orig.get(0), orig.get(2));
        List<UserEntity> actual = userDao.getList(ids);
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test empty
     *
     * @throws Exception exception
     */
    @Test
    public void testEmpty() throws Exception {
        Set<Integer> ids = Sets.newSet(0, -1);
        List<UserEntity> expected = Collections.emptyList();
        List<UserEntity> actual = userDao.getList(ids);
        assertEntityListEquals(expected, actual);
    }


    /**
     * Test ids empty
     *
     * @throws Exception exception
     */
    @Test
    public void testIdsEmpty() throws Exception {
        Set<Integer> ids = Collections.emptySet();
        List<UserEntity> expected = Collections.emptyList();
        List<UserEntity> actual = userDao.getList(ids);
        assertEntityListEquals(expected, actual);
    }

}
