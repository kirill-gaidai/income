package org.kirillgaidai.income.service.impl.userservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.UserEntity;
import org.kirillgaidai.income.service.dto.UserDto;
import org.mockito.internal.util.collections.Sets;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class UserServiceGetListByIdsTest extends UserServiceBaseTest {

    /**
     * Test empty
     *
     * @throws Exception exception
     */
    @Test
    public void testEmpty() throws Exception {
        Set<Integer> ids = Sets.newSet(1, 2);

        List<UserEntity> entityList = Collections.emptyList();

        doReturn(entityList).when(userDao).getList(ids);

        List<UserDto> expected = Collections.emptyList();
        List<UserDto> actual = service.getList(ids);
        assertEntityListEquals(expected, actual);

        verify(userDao).getList(ids);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Set<Integer> ids = Sets.newSet(1, 2);

        List<UserEntity> entityList = Arrays.asList(
                new UserEntity(1, "admin", "s3cr3t!", true, false, "token1", LocalDateTime.of(2018, 1, 2, 8, 45, 30)),
                new UserEntity(2, "blocked", "pass", false, true, "token2", LocalDateTime.of(2018, 1, 2, 9, 45, 30))
        );

        doReturn(entityList).when(userDao).getList(ids);

        List<UserDto> expected = Arrays.asList(
                new UserDto(1, "admin", "s3cr3t!", true, false, "token1", LocalDateTime.of(2018, 1, 2, 8, 45, 30)),
                new UserDto(2, "blocked", "pass", false, true, "token2", LocalDateTime.of(2018, 1, 2, 9, 45, 30))
        );
        List<UserDto> actual = service.getList(ids);
        assertEntityListEquals(expected, actual);

        verify(userDao).getList(ids);

        verifyNoMoreDaoInteractions();
    }

}
