package org.kirillgaidai.income.service.impl.operationsservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.service.dto.OperationDto;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.kirillgaidai.income.utils.TestUtils.throwUnreachableException;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class OperationServiceGetTest extends OperationServiceBaseTest {

    /**
     * Test null
     *
     * @throws Exception exception
     */
    @Test
    public void testNull() throws Exception {
        try {
            service.get(null);
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

        doReturn(null).when(operationDao).get(id);

        try {
            service.get(id);
            throwUnreachableException();
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Operation with id %d not found", id), e.getMessage());
        }

        verify(operationDao).get(id);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test account id is null
     *
     * @throws Exception exception
     */
    @Test
    public void testAccountIdIsNull() throws Exception {
        Integer id = 1;
        Integer categoryId = 3;
        LocalDate day = LocalDate.of(2017, 12, 1);
        BigDecimal amount = new BigDecimal("10");

        OperationEntity entity = new OperationEntity(id, null, categoryId, day, amount, "note");

        doReturn(entity).when(operationDao).get(id);

        try {
            service.get(id);
            throwUnreachableException();
        } catch (IllegalArgumentException e) {
            assertEquals("Account id is null", e.getMessage());
        }

        verify(operationDao).get(id);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test category id is null
     *
     * @throws Exception exception
     */
    @Test
    public void testCategoryIdIsNull() throws Exception {
        Integer id = 1;
        Integer accountId = 2;
        LocalDate day = LocalDate.of(2017, 12, 1);
        BigDecimal amount = new BigDecimal("10");

        OperationEntity entity = new OperationEntity(id, accountId, null, day, amount, "note");

        doReturn(entity).when(operationDao).get(id);

        try {
            service.get(id);
            throwUnreachableException();
        } catch (IllegalArgumentException e) {
            assertEquals("Category id is null", e.getMessage());
        }

        verify(operationDao).get(id);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test account isn't found
     *
     * @throws Exception exception
     */
    @Test
    public void testAccountNotFound() throws Exception {
        Integer id = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate day = LocalDate.of(2017, 12, 1);
        BigDecimal amount = new BigDecimal("10");

        OperationEntity entity =
                new OperationEntity(id, accountId, categoryId, day, amount, "note");
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "02", "categoryTitle");

        doReturn(entity).when(operationDao).get(id);
        doReturn(null).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);

        try {
            service.get(id);
            throwUnreachableException();
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Account with id %d not found", accountId), e.getMessage());
        }

        verify(operationDao).get(id);
        verify(accountDao).get(accountId);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test category isn't found
     *
     * @throws Exception exception
     */
    @Test
    public void testCategoryNotFound() throws Exception {
        Integer id = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate day = LocalDate.of(2017, 12, 1);
        BigDecimal amount = new BigDecimal("10");

        OperationEntity entity =
                new OperationEntity(id, accountId, categoryId, day, amount, "note");
        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "accountTitle");

        doReturn(entity).when(operationDao).get(id);
        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(null).when(categoryDao).get(categoryId);

        try {
            service.get(id);
            throwUnreachableException();
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Category with id %d not found", categoryId), e.getMessage());
        }

        verify(operationDao).get(id);
        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);

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
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate day = LocalDate.of(2017, 12, 1);
        BigDecimal amount = new BigDecimal("10");

        OperationEntity entity =
                new OperationEntity(id, accountId, categoryId, day, amount, "note");
        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "accountTitle");
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "02", "categoryTitle");

        doReturn(entity).when(operationDao).get(id);
        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);

        OperationDto expected =
                new OperationDto(id, accountId, "accountTitle", categoryId, "categoryTitle", day, amount, "note");
        OperationDto actual = service.get(id);
        assertEntityEquals(expected, actual);

        verify(operationDao).get(id);
        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);

        verifyNoMoreDaoInteractions();
    }

}
