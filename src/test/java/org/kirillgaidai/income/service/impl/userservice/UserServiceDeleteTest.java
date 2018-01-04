package org.kirillgaidai.income.service.impl.userservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.UserEntity;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticDeleteException;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.throwUnreachableException;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class UserServiceDeleteTest extends UserServiceBaseTest {

    /**
     * Test null
     *
     * @throws Exception exception
     */
    @Test
    public void testNull() throws Exception {
        try {
            service.delete(null);
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

        doReturn(null).when(userDao).get(id);

        try {
            service.delete(id);
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

        UserEntity entity = new UserEntity(id, "admin", "s3cr3t!", true, false, "token1",
                LocalDateTime.of(2018, 1, 2, 8, 45, 30));

        doReturn(entity).when(userDao).get(id);
        doReturn(0).when(userDao).delete(entity);

        try {
            service.delete(id);
            throwUnreachableException();
        } catch (IncomeServiceOptimisticDeleteException e) {
            assertEquals(String.format("User with id %d delete failure", id), e.getMessage());
        }

        verify(userDao).get(id);
        verify(userDao).delete(entity);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Integer id = 1;

        UserEntity entity = new UserEntity(id, "admin", "s3cr3t!", true, false, "token1",
                LocalDateTime.of(2018, 1, 2, 8, 45, 30));

        doReturn(entity).when(userDao).get(id);
        doReturn(1).when(userDao).delete(entity);

        service.delete(id);

        verify(userDao).get(id);
        verify(userDao).delete(entity);

        verifyNoMoreDaoInteractions();
    }

}
