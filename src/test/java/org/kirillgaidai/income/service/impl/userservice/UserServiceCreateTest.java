package org.kirillgaidai.income.service.impl.userservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.UserEntity;
import org.kirillgaidai.income.service.dto.UserDto;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticCreateException;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.kirillgaidai.income.utils.TestUtils.getSerialEntityInsertAnswer;
import static org.kirillgaidai.income.utils.TestUtils.throwUnreachableException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class UserServiceCreateTest extends UserServiceBaseTest {

    /**
     * Test null
     *
     * @throws Exception exception
     */
    @Test
    public void testNull() throws Exception {
        try {
            service.create(null);
            throwUnreachableException();
        } catch (IllegalArgumentException e) {
            assertEquals("Dto is null", e.getMessage());
        }

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test failure
     *
     * @throws Exception exception
     */
    @Test
    public void testFailure() throws Exception {
        UserDto dto = new UserDto(null, "admin", "s3cr3t!", true, false, "token1",
                ZonedDateTime.of(2018, 1, 2, 8, 45, 30, 0, ZoneOffset.UTC));

        doReturn(0).when(userDao).insert(any(UserEntity.class));

        try {
            service.create(dto);
            throwUnreachableException();
        } catch (IncomeServiceOptimisticCreateException e) {
            assertEquals("User create failure", e.getMessage());
        }

        ArgumentCaptor<UserEntity> argumentCaptor = ArgumentCaptor.forClass(UserEntity.class);

        verify(userDao).insert(argumentCaptor.capture());

        verifyNoMoreDaoInteractions();

        UserEntity expectedEntity = new UserEntity(null, "admin", "s3cr3t!", true, false, "token1",
                LocalDateTime.of(2018, 1, 2, 8, 45, 30));
        UserEntity actualEntity = argumentCaptor.getValue();
        assertEntityEquals(expectedEntity, actualEntity);
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Integer id = 1;
        UserDto dto = new UserDto(null, "admin", "s3cr3t!", true, false, "token1",
                ZonedDateTime.of(2018, 1, 2, 8, 45, 30, 0, ZoneOffset.UTC));

        doAnswer(getSerialEntityInsertAnswer(id)).when(userDao).insert(any(UserEntity.class));

        UserDto expected = new UserDto(id, "admin", "s3cr3t!", true, false, "token1",
                ZonedDateTime.of(2018, 1, 2, 8, 45, 30, 0, ZoneOffset.UTC));
        UserDto actual = service.create(dto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<UserEntity> argumentCaptor = ArgumentCaptor.forClass(UserEntity.class);

        verify(userDao).insert(argumentCaptor.capture());

        verifyNoMoreDaoInteractions();

        UserEntity expectedEntity = new UserEntity(id, "admin", "s3cr3t!", true, false, "token1",
                LocalDateTime.of(2018, 1, 2, 8, 45, 30));
        UserEntity actualEntity = argumentCaptor.getValue();
        assertEntityEquals(expectedEntity, actualEntity);
    }

}
