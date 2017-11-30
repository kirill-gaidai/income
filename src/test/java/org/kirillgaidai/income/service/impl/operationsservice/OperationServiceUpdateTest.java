package org.kirillgaidai.income.service.impl.operationsservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.service.dto.OperationDto;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.kirillgaidai.income.utils.TestUtils.throwUnreachableException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * Operation service update test
 *
 * @author Kirill Gaidai
 */
public class OperationServiceUpdateTest extends OperationServiceBaseTest {

    /**
     * Operation isn't found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        BigDecimal newAmount = new BigDecimal("1.25");
        String newNote = "newNote";
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 9, 5);

        OperationDto dto =
                new OperationDto(operationId, accountId, null, categoryId, null, thisDay, newAmount, newNote);

        doReturn(null).when(operationDao).get(operationId);

        try {
            service.update(dto);
            throwUnreachableException();
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Operation with id %d not found", operationId), e.getMessage());
        }

        verify(operationDao).get(operationId);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Account isn't found
     *
     * @throws Exception exception
     */
    @Test
    public void testAccountNotFound() throws Exception {
        BigDecimal newAmount = new BigDecimal("1.25");
        BigDecimal oldAmount = new BigDecimal("1.25");
        String newNote = "newNote";
        String oldNote = "oldNote";
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 9, 5);

        OperationDto dto =
                new OperationDto(operationId, accountId, null, categoryId, null, thisDay, newAmount, newNote);
        OperationEntity oldEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, oldAmount, oldNote);

        doReturn(oldEntity).when(operationDao).get(operationId);
        doReturn(null).when(accountDao).get(accountId);

        try {
            service.update(dto);
            throwUnreachableException();
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Account with id %d not found", accountId), e.getMessage());
        }

        verify(operationDao).get(operationId);
        verify(accountDao).get(accountId);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Category isn't found
     *
     * @throws Exception exception
     */
    @Test
    public void testCategoryNotFound() throws Exception {
        BigDecimal amount = new BigDecimal("1.25");
        BigDecimal oldAmount = new BigDecimal("1.25");
        String note = "note";
        String oldNote = "oldNote";
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 9, 5);

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "account1");
        OperationDto dto = new OperationDto(operationId, accountId, null, categoryId, null, thisDay, amount, note);
        OperationEntity oldEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, oldAmount, oldNote);


        doReturn(oldEntity).when(operationDao).get(operationId);
        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(null).when(categoryDao).get(categoryId);

        try {
            service.update(dto);
            throwUnreachableException();
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Category with id %d not found", categoryId), e.getMessage());
        }

        verify(operationDao).get(operationId);
        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Balance before this day isn't found
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceThisNotFound() throws Exception {
        BigDecimal amount = new BigDecimal("1.25");
        BigDecimal oldAmount = new BigDecimal("1.25");
        String note = "note";
        String oldNote = "oldNote";
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 9, 5);

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "account1");
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "02", "category1");
        OperationDto dto = new OperationDto(operationId, accountId, null, categoryId, null, thisDay, amount, note);
        OperationEntity oldEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, oldAmount, oldNote);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10"), false);

        doReturn(oldEntity).when(operationDao).get(operationId);
        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(null).when(balanceDao).getBefore(accountId, thisDay);

        try {
            service.update(dto);
            throwUnreachableException();
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Balance for account with id %d before %s not found", accountId, thisDay),
                    e.getMessage());
        }

        verify(operationDao).get(operationId);
        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getBefore(accountId, thisDay);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Balance on this day isn't found
     *
     * @throws Exception exception
     */
    @Test
    public void testBalancePrevNotFound() throws Exception {
        BigDecimal amount = new BigDecimal("1.25");
        BigDecimal oldAmount = new BigDecimal("1.25");
        String note = "note";
        String oldNote = "oldNote";
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 9, 5);
        LocalDate prevDay = LocalDate.of(2017, 9, 3);

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "account1");
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "02", "category1");
        OperationDto dto = new OperationDto(operationId, accountId, null, categoryId, null, thisDay, amount, note);
        OperationEntity oldEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, oldAmount, oldNote);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), false);

        doReturn(oldEntity).when(operationDao).get(operationId);
        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(null).when(balanceDao).get(accountId, thisDay);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);

        try {
            service.update(dto);
            throwUnreachableException();
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Balance for account with id %d on %s not found", accountId, thisDay),
                    e.getMessage());
        }

        verify(operationDao).get(operationId);
        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).get(accountId, thisDay);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Previous balance is fixed. This balance is fixed.
     * No balance is updated. Operation is updated
     *
     * @throws Exception exception
     */
    @Test
    public void testBalancePrevFixedThisFixed() throws Exception {
        BigDecimal newAmount = new BigDecimal("1.75");
        BigDecimal oldAmount = new BigDecimal("1.25");
        String newNote = "newNote";
        String oldNote = "oldNote";
        String accountTitle = "account1";
        String categoryTitle = "category1";
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 9, 5);
        LocalDate prevDay = LocalDate.of(2017, 9, 3);

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", accountTitle);
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "02", categoryTitle);
        OperationDto dto = new OperationDto(operationId, accountId, null, categoryId, null,
                thisDay, newAmount, newNote);
        OperationEntity oldEntity = new OperationEntity(operationId, accountId, categoryId,
                thisDay, oldAmount, oldNote);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), true);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(oldEntity).when(operationDao).get(operationId);
        doReturn(1).when(operationDao).update(any(OperationEntity.class), eq(oldEntity));

        OperationDto expected = new OperationDto(operationId, accountId, accountTitle, categoryId, categoryTitle,
                thisDay, newAmount, newNote);
        OperationDto actual = service.update(dto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<OperationEntity> operationArgumentCaptor = ArgumentCaptor.forClass(OperationEntity.class);

        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(operationDao).get(operationId);
        verify(operationDao).update(operationArgumentCaptor.capture(), eq(oldEntity));

        verifyNoMoreDaoInteractions();

        OperationEntity expectedOperationEntity = new OperationEntity(operationId, accountId, categoryId,
                thisDay, newAmount, newNote);
        OperationEntity actualOperationEntity = operationArgumentCaptor.getValue();
        assertEntityEquals(expectedOperationEntity, actualOperationEntity);
    }

    /**
     * Previous balance is fixed. This balance isn't fixed. After balance isn't found.
     * This balance is updated. Operation is updated
     *
     * @throws Exception exception
     */
    @Test
    public void testBalancePrevFixedThisNotFixedAfterNotFound() throws Exception {
        BigDecimal newAmount = new BigDecimal("1.75");
        BigDecimal oldAmount = new BigDecimal("1.25");
        String newNote = "newNote";
        String oldNote = "oldNote";
        String accountTitle = "account1";
        String categoryTitle = "category1";
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 9, 5);
        LocalDate prevDay = LocalDate.of(2017, 9, 3);

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", accountTitle);
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "02", categoryTitle);
        OperationDto dto = new OperationDto(operationId, accountId, null, categoryId, null,
                thisDay, newAmount, newNote);
        OperationEntity oldEntity = new OperationEntity(operationId, accountId, categoryId,
                thisDay, oldAmount, oldNote);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), true);
        BalanceEntity oldThisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(oldThisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(null).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class), eq(oldThisBalanceEntity));
        doReturn(oldEntity).when(operationDao).get(operationId);
        doReturn(1).when(operationDao).update(any(OperationEntity.class), eq(oldEntity));

        OperationDto expected = new OperationDto(operationId, accountId, accountTitle, categoryId, categoryTitle,
                thisDay, newAmount, newNote);
        OperationDto actual = service.update(dto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<BalanceEntity> balanceArgumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);
        ArgumentCaptor<OperationEntity> operationArgumentCaptor = ArgumentCaptor.forClass(OperationEntity.class);

        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(balanceDao).update(balanceArgumentCaptor.capture(), eq(oldThisBalanceEntity));
        verify(operationDao).get(operationId);
        verify(operationDao).update(operationArgumentCaptor.capture(), eq(oldEntity));

        verifyNoMoreDaoInteractions();

        BalanceEntity expectedBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.25"), false);
        BalanceEntity actualBalanceEntity = balanceArgumentCaptor.getValue();
        assertEntityEquals(expectedBalanceEntity, actualBalanceEntity);

        OperationEntity expectedOperationEntity = new OperationEntity(operationId, accountId, categoryId,
                thisDay, newAmount, newNote);
        OperationEntity actualOperationEntity = operationArgumentCaptor.getValue();
        assertEntityEquals(expectedOperationEntity, actualOperationEntity);
    }

    /**
     * Previous balance is fixed. This balance isn't fixed. After balance is found.
     * No balance is updated. Operation is updated
     *
     * @throws Exception exception
     */
    @Test
    public void testBalancePrevFixedThisNotFixedAfterFound() throws Exception {
        BigDecimal newAmount = new BigDecimal("1.75");
        BigDecimal oldAmount = new BigDecimal("1.25");
        String newNote = "newNote";
        String oldNote = "oldNote";
        String accountTitle = "account1";
        String categoryTitle = "category1";
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 9, 3);
        LocalDate thisDay = LocalDate.of(2017, 9, 5);
        LocalDate afterDay = LocalDate.of(2017, 9, 7);

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", accountTitle);
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "02", categoryTitle);
        OperationDto dto = new OperationDto(operationId, accountId, null, categoryId, null,
                thisDay, newAmount, newNote);
        OperationEntity oldEntity = new OperationEntity(operationId, accountId, categoryId,
                thisDay, oldAmount, oldNote);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), true);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), false);
        BalanceEntity afterBalanceEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("8.75"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterBalanceEntity).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(oldEntity).when(operationDao).get(operationId);
        doReturn(1).when(operationDao).update(any(OperationEntity.class), eq(oldEntity));

        OperationDto expected = new OperationDto(operationId, accountId, accountTitle, categoryId, categoryTitle,
                thisDay, newAmount, newNote);
        OperationDto actual = service.update(dto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<OperationEntity> operationArgumentCaptor = ArgumentCaptor.forClass(OperationEntity.class);

        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(operationDao).get(operationId);
        verify(operationDao).update(operationArgumentCaptor.capture(), eq(oldEntity));

        verifyNoMoreDaoInteractions();

        OperationEntity expectedOperationEntity = new OperationEntity(operationId, accountId, categoryId,
                thisDay, newAmount, newNote);
        OperationEntity actualOperationEntity = operationArgumentCaptor.getValue();
        assertEntityEquals(expectedOperationEntity, actualOperationEntity);
    }

    /**
     * Before balance isn't found. Previous balance isn't fixed. This balance is fixed.
     * Previous balance is updated. Operation is updated
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeNotFoundPrevNotFixedThisFixed() throws Exception {
        BigDecimal newAmount = new BigDecimal("1.75");
        BigDecimal oldAmount = new BigDecimal("1.25");
        String newNote = "newNote";
        String oldNote = "oldNote";
        String accountTitle = "account1";
        String categoryTitle = "category1";
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 9, 5);
        LocalDate prevDay = LocalDate.of(2017, 9, 3);

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", accountTitle);
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "02", categoryTitle);
        OperationDto dto = new OperationDto(operationId, accountId, null, categoryId, null,
                thisDay, newAmount, newNote);
        OperationEntity oldEntity = new OperationEntity(operationId, accountId, categoryId,
                thisDay, oldAmount, oldNote);
        BalanceEntity oldPrevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(oldPrevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(null).when(balanceDao).getBefore(accountId, prevDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class), eq(oldPrevBalanceEntity));
        doReturn(oldEntity).when(operationDao).get(operationId);
        doReturn(1).when(operationDao).update(any(OperationEntity.class), eq(oldEntity));

        OperationDto expected = new OperationDto(operationId, accountId, accountTitle, categoryId, categoryTitle,
                thisDay, newAmount, newNote);
        OperationDto actual = service.update(dto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<BalanceEntity> balanceArgumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);
        ArgumentCaptor<OperationEntity> operationArgumentCaptor = ArgumentCaptor.forClass(OperationEntity.class);

        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).getBefore(accountId, prevDay);
        verify(balanceDao).update(balanceArgumentCaptor.capture(), eq(oldPrevBalanceEntity));
        verify(operationDao).get(operationId);
        verify(operationDao).update(operationArgumentCaptor.capture(), eq(oldEntity));

        verifyNoMoreDaoInteractions();

        BalanceEntity expectedBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.5"), false);
        BalanceEntity actualBalanceEntity = balanceArgumentCaptor.getValue();
        assertEntityEquals(expectedBalanceEntity, actualBalanceEntity);

        OperationEntity expectedOperationEntity = new OperationEntity(operationId, accountId, categoryId,
                thisDay, newAmount, newNote);
        OperationEntity actualOperationEntity = operationArgumentCaptor.getValue();
        assertEntityEquals(expectedOperationEntity, actualOperationEntity);
    }

    /**
     * Before balance is found. Previous balance isn't fixed. This balance is fixed.
     * No balance is updated. Operation is updated
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeFoundPrevNotFixedThisFixed() throws Exception {
        BigDecimal newAmount = new BigDecimal("1.75");
        BigDecimal oldAmount = new BigDecimal("1.25");
        String newNote = "newNote";
        String oldNote = "oldNote";
        String accountTitle = "account1";
        String categoryTitle = "category1";
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate beforeDay = LocalDate.of(2017, 9, 1);
        LocalDate prevDay = LocalDate.of(2017, 9, 3);
        LocalDate thisDay = LocalDate.of(2017, 9, 5);

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", accountTitle);
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "02", categoryTitle);
        OperationDto dto = new OperationDto(operationId, accountId, null, categoryId, null,
                thisDay, newAmount, newNote);
        OperationEntity oldEntity = new OperationEntity(operationId, accountId, categoryId,
                thisDay, oldAmount, oldNote);
        BalanceEntity beforeBalanceEntity = new BalanceEntity(accountId, beforeDay, new BigDecimal("10"), false);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(beforeBalanceEntity).when(balanceDao).getBefore(accountId, prevDay);
        doReturn(oldEntity).when(operationDao).get(operationId);
        doReturn(1).when(operationDao).update(any(OperationEntity.class), eq(oldEntity));

        OperationDto expected = new OperationDto(operationId, accountId, accountTitle, categoryId, categoryTitle,
                thisDay, newAmount, newNote);
        OperationDto actual = service.update(dto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<OperationEntity> operationArgumentCaptor = ArgumentCaptor.forClass(OperationEntity.class);

        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).getBefore(accountId, prevDay);
        verify(operationDao).get(operationId);
        verify(operationDao).update(operationArgumentCaptor.capture(), eq(oldEntity));

        verifyNoMoreDaoInteractions();

        OperationEntity expectedOperationEntity = new OperationEntity(operationId, accountId, categoryId,
                thisDay, newAmount, newNote);
        OperationEntity actualOperationEntity = operationArgumentCaptor.getValue();
        assertEntityEquals(expectedOperationEntity, actualOperationEntity);
    }

    /**
     * Test before balance isn't found. Previous balance isn't fixed.
     * This balance isn't fixed. After balance isn't found.
     * This balance is updated. Operation is updated.
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeNotFoundPrevNotFixedThisNotFixedAfterNotFound() throws Exception {
        BigDecimal newAmount = new BigDecimal("1.75");
        BigDecimal oldAmount = new BigDecimal("1.25");
        String newNote = "newNote";
        String oldNote = "oldNote";
        String accountTitle = "account1";
        String categoryTitle = "category1";
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 9, 3);
        LocalDate thisDay = LocalDate.of(2017, 9, 5);

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", accountTitle);
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "02", categoryTitle);
        OperationDto dto = new OperationDto(operationId, accountId, null, categoryId, null,
                thisDay, newAmount, newNote);
        OperationEntity oldEntity = new OperationEntity(operationId, accountId, categoryId,
                thisDay, oldAmount, oldNote);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(null).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class), eq(thisBalanceEntity));
        doReturn(oldEntity).when(operationDao).get(operationId);
        doReturn(1).when(operationDao).update(any(OperationEntity.class), eq(oldEntity));

        OperationDto expected = new OperationDto(operationId, accountId, accountTitle, categoryId, categoryTitle,
                thisDay, newAmount, newNote);
        OperationDto actual = service.update(dto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<BalanceEntity> balanceArgumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);
        ArgumentCaptor<OperationEntity> operationArgumentCaptor = ArgumentCaptor.forClass(OperationEntity.class);

        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(balanceDao).update(balanceArgumentCaptor.capture(), eq(thisBalanceEntity));
        verify(operationDao).get(operationId);
        verify(operationDao).update(operationArgumentCaptor.capture(), eq(oldEntity));

        verifyNoMoreDaoInteractions();

        BalanceEntity expectedBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.25"), false);
        BalanceEntity actualBalanceEntity = balanceArgumentCaptor.getValue();
        assertEntityEquals(expectedBalanceEntity, actualBalanceEntity);

        OperationEntity expectedOperationEntity = new OperationEntity(operationId, accountId, categoryId,
                thisDay, newAmount, newNote);
        OperationEntity actualOperationEntity = operationArgumentCaptor.getValue();
        assertEntityEquals(expectedOperationEntity, actualOperationEntity);
    }

    /**
     * Test before balance isn't found. Previous balance isn't fixed.
     * This balance isn't fixed. After balance is found.
     * Previous balance is updated. Operation is updated
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeNotFoundPrevNotFixedThisNotFixedAfterFound() throws Exception {
        BigDecimal newAmount = new BigDecimal("1.75");
        BigDecimal oldAmount = new BigDecimal("1.25");
        String newNote = "newNote";
        String oldNote = "oldNote";
        String accountTitle = "account1";
        String categoryTitle = "category1";
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 9, 3);
        LocalDate thisDay = LocalDate.of(2017, 9, 5);
        LocalDate afterDay = LocalDate.of(2017, 9, 7);

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", accountTitle);
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "02", categoryTitle);
        OperationDto dto = new OperationDto(operationId, accountId, null, categoryId, null,
                thisDay, newAmount, newNote);
        OperationEntity oldEntity = new OperationEntity(operationId, accountId, categoryId,
                thisDay, oldAmount, oldNote);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), false);
        BalanceEntity afterBalanceEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("8.75"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(null).when(balanceDao).getBefore(accountId, prevDay);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterBalanceEntity).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class), eq(prevBalanceEntity));
        doReturn(oldEntity).when(operationDao).get(operationId);
        doReturn(1).when(operationDao).update(any(OperationEntity.class), eq(oldEntity));

        OperationDto expected = new OperationDto(operationId, accountId, accountTitle, categoryId, categoryTitle,
                thisDay, newAmount, newNote);
        OperationDto actual = service.update(dto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<BalanceEntity> balanceArgumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);
        ArgumentCaptor<OperationEntity> operationArgumentCaptor = ArgumentCaptor.forClass(OperationEntity.class);

        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).getBefore(accountId, prevDay);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(balanceDao).update(balanceArgumentCaptor.capture(), eq(prevBalanceEntity));
        verify(operationDao).get(operationId);
        verify(operationDao).update(operationArgumentCaptor.capture(), eq(oldEntity));

        verifyNoMoreDaoInteractions();

        BalanceEntity expectedBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.5"), false);
        BalanceEntity actualBalanceEntity = balanceArgumentCaptor.getValue();
        assertEntityEquals(expectedBalanceEntity, actualBalanceEntity);

        OperationEntity expectedOperationEntity = new OperationEntity(operationId, accountId, categoryId,
                thisDay, newAmount, newNote);
        OperationEntity actualOperationEntity = operationArgumentCaptor.getValue();
        assertEntityEquals(expectedOperationEntity, actualOperationEntity);
    }

    /**
     * Test before balance isn't found. Previous balance isn't fixed.
     * This balance isn't fixed. After balance is found.
     * No balance is updated. Operation is updated
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeFoundPrevFixedThisFixedAfterFound() throws Exception {
        BigDecimal newAmount = new BigDecimal("1.75");
        BigDecimal oldAmount = new BigDecimal("1.25");
        String newNote = "newNote";
        String oldNote = "oldNote";
        String accountTitle = "account1";
        String categoryTitle = "category1";
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate beforeDay = LocalDate.of(2017, 9, 1);
        LocalDate prevDay = LocalDate.of(2017, 9, 3);
        LocalDate thisDay = LocalDate.of(2017, 9, 5);
        LocalDate afterDay = LocalDate.of(2017, 9, 7);

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", accountTitle);
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "02", categoryTitle);
        OperationDto dto = new OperationDto(operationId, accountId, null, categoryId, null,
                thisDay, newAmount, newNote);
        OperationEntity oldEntity = new OperationEntity(operationId, accountId, categoryId,
                thisDay, oldAmount, oldNote);
        BalanceEntity beforeBalanceEntity = new BalanceEntity(accountId, beforeDay, new BigDecimal("10"), false);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), false);
        BalanceEntity afterBalanceEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("8.75"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(beforeBalanceEntity).when(balanceDao).getBefore(accountId, prevDay);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterBalanceEntity).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(oldEntity).when(operationDao).get(operationId);
        doReturn(1).when(operationDao).update(any(OperationEntity.class), eq(oldEntity));

        OperationDto expected = new OperationDto(operationId, accountId, accountTitle, categoryId, categoryTitle,
                thisDay, newAmount, newNote);
        OperationDto actual = service.update(dto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<OperationEntity> operationArgumentCaptor = ArgumentCaptor.forClass(OperationEntity.class);

        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).getBefore(accountId, prevDay);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(operationDao).get(operationId);
        verify(operationDao).update(operationArgumentCaptor.capture(), eq(oldEntity));

        verifyNoMoreDaoInteractions();

        OperationEntity expectedOperationEntity = new OperationEntity(operationId, accountId, categoryId,
                thisDay, newAmount, newNote);
        OperationEntity actualOperationEntity = operationArgumentCaptor.getValue();
        assertEntityEquals(expectedOperationEntity, actualOperationEntity);
    }

}
