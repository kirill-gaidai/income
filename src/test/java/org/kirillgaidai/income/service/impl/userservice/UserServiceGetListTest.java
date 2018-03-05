package org.kirillgaidai.income.service.impl.userservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.UserEntity;
import org.kirillgaidai.income.service.dto.UserDto;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class UserServiceGetListTest extends UserServiceBaseTest {

    /**
     * Test empty
     *
     * @throws Exception exception
     */
    @Test
    public void testEmpty() throws Exception {
        List<UserEntity> entityList = Collections.emptyList();

        doReturn(entityList).when(userDao).getList();

        List<UserDto> expected = Collections.emptyList();
        List<UserDto> actual = service.getList();
        assertEntityListEquals(expected, actual);

        verify(userDao).getList();

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        List<UserEntity> entityList = Arrays.asList(
                new UserEntity(1, "admin", "s3cr3t!", true, false, "token1", LocalDateTime.of(2018, 1, 2, 8, 45, 30)),
                new UserEntity(2, "blocked", "pass", false, true, "token2", LocalDateTime.of(2018, 1, 2, 9, 45, 30)),
                new UserEntity(3, "user", "password", false, false, "token3", LocalDateTime.of(2018, 1, 2, 10, 45, 30))
        );

        doReturn(entityList).when(userDao).getList();

        List<UserDto> expected = Arrays.asList(
                new UserDto(1, "admin", "s3cr3t!", true, false, "token1",
                        ZonedDateTime.of(2018, 1, 2, 8, 45, 30, 0, ZoneOffset.UTC)),
                new UserDto(2, "blocked", "pass", false, true, "token2",
                        ZonedDateTime.of(2018, 1, 2, 9, 45, 30, 0, ZoneOffset.UTC)),
                new UserDto(3, "user", "password", false, false, "token3",
                        ZonedDateTime.of(2018, 1, 2, 10, 45, 30, 0, ZoneOffset.UTC))
        );
        List<UserDto> actual = service.getList();
        assertEntityListEquals(expected, actual);

        verify(userDao).getList();

        verifyNoMoreDaoInteractions();
    }

}
