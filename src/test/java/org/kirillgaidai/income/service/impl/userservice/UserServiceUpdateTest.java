package org.kirillgaidai.income.service.impl.userservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.UserEntity;
import org.kirillgaidai.income.service.dto.UserDto;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticUpdateException;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.kirillgaidai.income.utils.TestUtils.throwUnreachableException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class UserServiceUpdateTest extends UserServiceBaseTest {

    /**
     * Test null
     *
     * @throws Exception exception
     */
    @Test
    public void testNull() throws Exception {
        try {
            service.update(null);
            throwUnreachableException();
        } catch (IllegalArgumentException e) {
            assertEquals("Dto is null", e.getMessage());
        }

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test id is null
     *
     * @throws Exception exception
     */
    @Test
    public void testIdIsNull() throws Exception {
        UserDto dto = new UserDto(null, "admin", "s3cr3t!", true, false, "token1",
                ZonedDateTime.of(2018, 1, 2, 8, 45, 30, 0, ZoneOffset.UTC));

        try {
            service.update(dto);
            throwUnreachableException();
        } catch (IllegalArgumentException e) {
            assertEquals("Id is null", e.getMessage());
        }

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        Integer id = 1;

        UserDto dto = new UserDto(id, "admin", "s3cr3t!", true, false, "token1",
                ZonedDateTime.of(2018, 1, 2, 8, 45, 30, 0, ZoneOffset.UTC));

        doReturn(null).when(userDao).get(id);

        try {
            service.update(dto);
            throwUnreachableException();
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("User with id %d not found", id), e.getMessage());
        }

        verify(userDao).get(id);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test failure
     *
     * @throws Exception exception
     */
    @Test
    public void testFailure() throws Exception {
        Integer id = 1;

        UserDto dto = new UserDto(id, "admin", "s3cr3t!", true, false, "token1",
                ZonedDateTime.of(2018, 1, 2, 8, 45, 30, 0, ZoneOffset.UTC));
        UserEntity oldEntity = new UserEntity(id, "admin", "s3cr3t", false, true, "token0",
                LocalDateTime.of(2018, 1, 2, 10, 45, 30));

        doReturn(oldEntity).when(userDao).get(id);
        doReturn(0).when(userDao).update(any(UserEntity.class), eq(oldEntity));

        try {
            service.update(dto);
            throwUnreachableException();
        } catch (IncomeServiceOptimisticUpdateException e) {
            assertEquals(String.format("User with id %d update failure", id), e.getMessage());
        }

        ArgumentCaptor<UserEntity> argumentCaptor = ArgumentCaptor.forClass(UserEntity.class);

        verify(userDao).get(id);
        verify(userDao).update(argumentCaptor.capture(), eq(oldEntity));

        verifyNoMoreDaoInteractions();

        UserEntity expectedEntity = new UserEntity(id, "admin", "s3cr3t!", true, false, "token1",
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

        UserDto dto = new UserDto(id, "admin", "s3cr3t!", true, false, "token1",
                ZonedDateTime.of(2018, 1, 2, 8, 45, 30, 0, ZoneOffset.UTC));
        UserEntity oldEntity = new UserEntity(id, "admin", "s3cr3t", false, true, "token0",
                LocalDateTime.of(2018, 1, 2, 10, 45, 30));

        doReturn(oldEntity).when(userDao).get(id);
        doReturn(1).when(userDao).update(any(UserEntity.class), eq(oldEntity));

        UserDto expected = new UserDto(id, "admin", "s3cr3t!", true, false, "token1",
                ZonedDateTime.of(2018, 1, 2, 8, 45, 30, 0, ZoneOffset.UTC));
        UserDto actual = service.update(dto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<UserEntity> argumentCaptor = ArgumentCaptor.forClass(UserEntity.class);

        verify(userDao).get(id);
        verify(userDao).update(argumentCaptor.capture(), eq(oldEntity));

        verifyNoMoreDaoInteractions();

        UserEntity expectedEntity = new UserEntity(id, "admin", "s3cr3t!", true, false, "token1",
                LocalDateTime.of(2018, 1, 2, 8, 45, 30));
        UserEntity actualEntity = argumentCaptor.getValue();
        assertEntityEquals(expectedEntity, actualEntity);
    }

}
