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
 * Operation service create test
 *
 * @author Kirill Gaidai
 */
public class OperationServiceCreateTest extends OperationServiceBaseTest {

    /**
     * Account isn't found
     *
     * @throws Exception exception
     */
    @Test
    public void testAccountNotFound() throws Exception {
        BigDecimal amount = new BigDecimal("1.25");
        String note = "note";
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 9, 5);

        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "02", "category1");
        OperationDto origDto = new OperationDto(null, accountId, null, categoryId, null, thisDay, amount, note);

        doReturn(null).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);

        try {
            service.create(origDto);
            throwUnreachableException();
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Account with id %d not found", accountId), e.getMessage());
        }

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
        String note = "note";
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 9, 5);

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "account1");
        OperationDto origDto = new OperationDto(null, accountId, null, categoryId, null, thisDay, amount, note);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(null).when(categoryDao).get(categoryId);

        try {
            service.create(origDto);
            throwUnreachableException();
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Category with id %d not found", categoryId), e.getMessage());
        }

        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verifyNoMoreDaoInteractions();
    }

    /**
     * Previous balance isn't found. This balance isn't found
     *
     * @throws Exception exception
     */
    @Test
    public void testBalancePrevNotFoundThisNotFound() throws Exception {
        BigDecimal amount = new BigDecimal("1.25");
        String note = "note";
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 9, 5);

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "account1");
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "02", "category1");
        OperationDto origDto = new OperationDto(null, accountId, null, categoryId, null, thisDay, amount, note);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(null).when(balanceDao).get(accountId, thisDay);
        doReturn(null).when(balanceDao).get(accountId, thisDay);

        try {
            service.create(origDto);
            throwUnreachableException();
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Balance for account with id %d on %s not found", accountId, thisDay),
                    e.getMessage());
        }

        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getBefore(accountId, thisDay);
        verifyNoMoreDaoInteractions();
    }

    /**
     * Previous balance is found. This balance isn't found.
     * This balance is inserted. Operation is inserted
     *
     * @throws Exception exception
     */
    @Test
    public void testBalancePrevFoundThisNotFound() throws Exception {
        BigDecimal amount = new BigDecimal("1.25");
        String note = "note";
        String accountTitle = "account1";
        String categoryTitle = "category1";
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 9, 3);
        LocalDate thisDay = LocalDate.of(2017, 9, 5);

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", accountTitle);
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "02", categoryTitle);
        OperationDto origDto = new OperationDto(null, accountId, null, categoryId, null, thisDay, amount, note);
        OperationEntity entity = new OperationEntity(operationId, accountId, categoryId, thisDay, amount, note);
        OperationDto newDto = new OperationDto(operationId, accountId, null, categoryId, null, thisDay, amount, note);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), true);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(null).when(balanceDao).get(accountId, thisDay);
        doReturn(1).when(balanceDao).insert(any(BalanceEntity.class));
        doReturn(entity).when(converter).convertToEntity(origDto);
        doReturn(newDto).when(converter).convertToDto(entity);
        doReturn(1).when(operationDao).insert(entity);

        OperationDto expected = new OperationDto(operationId, accountId, accountTitle, categoryId, categoryTitle,
                thisDay, amount, note);
        OperationDto actual = service.create(origDto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).insert(argumentCaptor.capture());
        verify(converter).convertToEntity(origDto);
        verify(converter).convertToDto(entity);
        verify(operationDao).insert(entity);
        verifyNoMoreDaoInteractions();

        assertEntityEquals(thisBalanceEntity, argumentCaptor.getValue());
    }

    /**
     * Previous balance isn't found. This balance is found.
     * Previous balance (yesterday) is inserted. Operation is inserted
     *
     * @throws Exception exception
     */
    @Test
    public void testBalancePrevNotFoundThisFound() throws Exception {
        BigDecimal amount = new BigDecimal("1.25");
        String note = "note";
        String accountTitle = "account1";
        String categoryTitle = "category1";
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 9, 4);
        LocalDate thisDay = prevDay.plusDays(1L);

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", accountTitle);
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "02", categoryTitle);
        OperationDto origDto = new OperationDto(null, accountId, null, categoryId, null, thisDay, amount, note);
        OperationEntity entity = new OperationEntity(operationId, accountId, categoryId, thisDay, amount, note);
        OperationDto newDto = new OperationDto(operationId, accountId, null, categoryId, null, thisDay, amount, note);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(null).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(1).when(balanceDao).insert(any(BalanceEntity.class));
        doReturn(entity).when(converter).convertToEntity(origDto);
        doReturn(newDto).when(converter).convertToDto(entity);
        doReturn(1).when(operationDao).insert(entity);

        OperationDto expected = new OperationDto(operationId, accountId, accountTitle, categoryId, categoryTitle,
                thisDay, amount, note);
        OperationDto actual = service.create(origDto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).insert(argumentCaptor.capture());
        verify(converter).convertToEntity(origDto);
        verify(converter).convertToDto(entity);
        verify(operationDao).insert(entity);
        verifyNoMoreDaoInteractions();

        assertEntityEquals(prevBalanceEntity, argumentCaptor.getValue());
    }

    /**
     * Balance for this day is fixed. Balance for prev day is fixed.
     * No balance is updated. Operation is inserted
     *
     * @throws Exception exception
     */
    @Test
    public void testBalancePrevFixedThisFixed() throws Exception {
        BigDecimal amount = new BigDecimal("1.25");
        String note = "note";
        String accountTitle = "account1";
        String categoryTitle = "category1";
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 9, 3);
        LocalDate thisDay = LocalDate.of(2017, 9, 5);

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", accountTitle);
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "02", categoryTitle);
        OperationDto origDto = new OperationDto(null, accountId, null, categoryId, null, thisDay, amount, note);
        OperationEntity entity = new OperationEntity(operationId, accountId, categoryId, thisDay, amount, note);
        OperationDto newDto = new OperationDto(operationId, accountId, null, categoryId, null, thisDay, amount, note);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), true);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(entity).when(converter).convertToEntity(origDto);
        doReturn(newDto).when(converter).convertToDto(entity);
        doReturn(1).when(operationDao).insert(entity);

        OperationDto expected = new OperationDto(operationId, accountId, accountTitle, categoryId, categoryTitle,
                thisDay, amount, note);
        OperationDto actual = service.create(origDto);
        assertEntityEquals(expected, actual);

        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(converter).convertToEntity(origDto);
        verify(converter).convertToDto(entity);
        verify(operationDao).insert(entity);
        verifyNoMoreDaoInteractions();
    }

    /**
     * Previous balance is fixed. This balance isn't fixed. After balance isn't found.
     * This balance is updated. Operation is inserted
     *
     * @throws Exception exception
     */
    @Test
    public void testBalancePrevFixedThisNotFixedAfterNotFound() throws Exception {
        BigDecimal amount = new BigDecimal("1.25");
        String note = "note";
        String accountTitle = "account1";
        String categoryTitle = "category1";
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 9, 3);
        LocalDate thisDay = LocalDate.of(2017, 9, 5);

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", accountTitle);
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "02", categoryTitle);
        OperationDto origDto = new OperationDto(null, accountId, null, categoryId, null, thisDay, amount, note);
        OperationEntity entity = new OperationEntity(operationId, accountId, categoryId, thisDay, amount, note);
        OperationDto newDto = new OperationDto(operationId, accountId, null, categoryId, null, thisDay, amount, note);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), true);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(null).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class), eq(thisBalanceEntity));
        doReturn(entity).when(converter).convertToEntity(origDto);
        doReturn(newDto).when(converter).convertToDto(entity);
        doReturn(1).when(operationDao).insert(entity);

        OperationDto expected = new OperationDto(operationId, accountId, accountTitle, categoryId, categoryTitle,
                thisDay, amount, note);
        OperationDto actual = service.create(origDto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(balanceDao).update(argumentCaptor.capture(), eq(thisBalanceEntity));
        verify(converter).convertToEntity(origDto);
        verify(converter).convertToDto(entity);
        verify(operationDao).insert(entity);
        verifyNoMoreDaoInteractions();

        BalanceEntity expectedBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), false);
        assertEntityEquals(expectedBalanceEntity, argumentCaptor.getValue());
    }

    /**
     * Previous balance is fixed. This balance isn't fixed. After balance isn't found.
     * No balance is updated. Operation is inserted
     *
     * @throws Exception exception
     */
    @Test
    public void testBalancePrevFixedThisNotFixedAfterFound() throws Exception {
        BigDecimal amount = new BigDecimal("1.25");
        String note = "note";
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
        OperationDto origDto = new OperationDto(null, accountId, null, categoryId, null, thisDay, amount, note);
        OperationEntity entity = new OperationEntity(operationId, accountId, categoryId, thisDay, amount, note);
        OperationDto newDto = new OperationDto(operationId, accountId, null, categoryId, null, thisDay, amount, note);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), true);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10"), false);
        BalanceEntity afterBalanceEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("10"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterBalanceEntity).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(entity).when(converter).convertToEntity(origDto);
        doReturn(newDto).when(converter).convertToDto(entity);
        doReturn(1).when(operationDao).insert(entity);

        OperationDto expected = new OperationDto(operationId, accountId, accountTitle, categoryId, categoryTitle,
                thisDay, amount, note);
        OperationDto actual = service.create(origDto);
        assertEntityEquals(expected, actual);

        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(converter).convertToEntity(origDto);
        verify(converter).convertToDto(entity);
        verify(operationDao).insert(entity);
        verifyNoMoreDaoInteractions();
    }

    /**
     * Before balance isn't found. Previous balance isn't fixed. This balance is fixed.
     * Previous balance is updated. Operation is inserted
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeNotFoundPrevNotFixedThisFixed() throws Exception {
        BigDecimal amount = new BigDecimal("1.25");
        String note = "note";
        String accountTitle = "account1";
        String categoryTitle = "category1";
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 9, 3);
        LocalDate thisDay = LocalDate.of(2017, 9, 5);

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", accountTitle);
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "02", categoryTitle);
        OperationDto origDto = new OperationDto(null, accountId, null, categoryId, null, thisDay, amount, note);
        OperationEntity entity = new OperationEntity(operationId, accountId, categoryId, thisDay, amount, note);
        OperationDto newDto = new OperationDto(operationId, accountId, null, categoryId, null, thisDay, amount, note);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(null).when(balanceDao).getBefore(accountId, prevDay);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class), eq(prevBalanceEntity));
        doReturn(entity).when(converter).convertToEntity(origDto);
        doReturn(newDto).when(converter).convertToDto(entity);
        doReturn(1).when(operationDao).insert(entity);

        OperationDto expected = new OperationDto(operationId, accountId, accountTitle, categoryId, categoryTitle,
                thisDay, amount, note);
        OperationDto actual = service.create(origDto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).getBefore(accountId, prevDay);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).update(argumentCaptor.capture(), eq(prevBalanceEntity));
        verify(converter).convertToEntity(origDto);
        verify(converter).convertToDto(entity);
        verify(operationDao).insert(entity);
        verifyNoMoreDaoInteractions();

        BalanceEntity expectedBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("11.25"), false);
        assertEntityEquals(expectedBalanceEntity, argumentCaptor.getValue());
    }

    /**
     * Before balance is found. Previous balance isn't fixed. This balance is fixed.
     * Prev balance is updated. Operation is inserted
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeFoundPrevNotFixedThisFixed() throws Exception {
        BigDecimal amount = new BigDecimal("1.25");
        String note = "note";
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
        OperationDto origDto = new OperationDto(null, accountId, null, categoryId, null, thisDay, amount, note);
        OperationEntity entity = new OperationEntity(operationId, accountId, categoryId, thisDay, amount, note);
        OperationDto newDto = new OperationDto(operationId, accountId, null, categoryId, null, thisDay, amount, note);
        BalanceEntity beforeBalanceEntity = new BalanceEntity(accountId, beforeDay, new BigDecimal("10"), false);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(beforeBalanceEntity).when(balanceDao).getBefore(accountId, prevDay);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(entity).when(converter).convertToEntity(origDto);
        doReturn(newDto).when(converter).convertToDto(entity);
        doReturn(1).when(operationDao).insert(entity);

        OperationDto expected = new OperationDto(operationId, accountId, accountTitle, categoryId, categoryTitle,
                thisDay, amount, note);
        OperationDto actual = service.create(origDto);
        assertEntityEquals(expected, actual);

        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).getBefore(accountId, prevDay);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(converter).convertToEntity(origDto);
        verify(converter).convertToDto(entity);
        verify(operationDao).insert(entity);
        verifyNoMoreDaoInteractions();
    }

    /**
     * Before balance isn't found. Previous balance isn't fixed. This balance isn't fixed. After balance isn't found.
     * This balance is updated. Operation is inserted
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeNotFoundPrevNotFixedThisNotFixedAfterNotFound() throws Exception {
        BigDecimal amount = new BigDecimal("1.25");
        String note = "note";
        String accountTitle = "account1";
        String categoryTitle = "category1";
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 9, 3);
        LocalDate thisDay = LocalDate.of(2017, 9, 5);

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", accountTitle);
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "02", categoryTitle);
        OperationDto origDto = new OperationDto(null, accountId, null, categoryId, null, thisDay, amount, note);
        OperationEntity entity = new OperationEntity(operationId, accountId, categoryId, thisDay, amount, note);
        OperationDto newDto = new OperationDto(operationId, accountId, null, categoryId, null, thisDay, amount, note);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(null).when(balanceDao).getBefore(accountId, prevDay);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(null).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class), eq(thisBalanceEntity));
        doReturn(entity).when(converter).convertToEntity(origDto);
        doReturn(newDto).when(converter).convertToDto(entity);
        doReturn(1).when(operationDao).insert(entity);

        OperationDto expected = new OperationDto(operationId, accountId, accountTitle, categoryId, categoryTitle,
                thisDay, amount, note);
        OperationDto actual = service.create(origDto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(balanceDao).update(argumentCaptor.capture(), eq(thisBalanceEntity));
        verify(converter).convertToEntity(origDto);
        verify(converter).convertToDto(entity);
        verify(operationDao).insert(entity);
        verifyNoMoreDaoInteractions();

        BalanceEntity expectedBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), false);
        assertEntityEquals(expectedBalanceEntity, argumentCaptor.getValue());
    }

    /**
     * Before balance isn't found. Previous balance isn't fixed. This balance isn't fixed. After balance is found.
     * Previous balance is updated. Operation is inserted.
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeNotFoundPrevNotFixedThisNotFixedAfterFound() throws Exception {
        BigDecimal amount = new BigDecimal("1.25");
        String note = "note";
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
        OperationDto origDto = new OperationDto(null, accountId, null, categoryId, null, thisDay, amount, note);
        OperationEntity entity = new OperationEntity(operationId, accountId, categoryId, thisDay, amount, note);
        OperationDto newDto = new OperationDto(operationId, accountId, null, categoryId, null, thisDay, amount, note);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10"), false);
        BalanceEntity afterBalanceEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("10"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(null).when(balanceDao).getBefore(accountId, prevDay);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterBalanceEntity).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class), eq(prevBalanceEntity));
        doReturn(entity).when(converter).convertToEntity(origDto);
        doReturn(newDto).when(converter).convertToDto(entity);
        doReturn(1).when(operationDao).insert(entity);

        OperationDto expected = new OperationDto(operationId, accountId, accountTitle, categoryId, categoryTitle,
                thisDay, amount, note);
        OperationDto actual = service.create(origDto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).getBefore(accountId, prevDay);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(balanceDao).update(argumentCaptor.capture(), eq(prevBalanceEntity));
        verify(converter).convertToEntity(origDto);
        verify(converter).convertToDto(entity);
        verify(operationDao).insert(entity);
        verifyNoMoreDaoInteractions();

        BalanceEntity expectedBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("11.25"), false);
        assertEntityEquals(expectedBalanceEntity, argumentCaptor.getValue());
    }

    /**
     * Before balance is found. Previous balance isn't fixed. This balance isn't fixed. After balance is found.
     * No balance is updated. Operation is inserted.
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeFoundPrevNotFixedThisNotFixedAfterFound() throws Exception {
        BigDecimal amount = new BigDecimal("1.25");
        String note = "note";
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
        OperationDto origDto = new OperationDto(null, accountId, null, categoryId, null, thisDay, amount, note);
        OperationEntity entity = new OperationEntity(operationId, accountId, categoryId, thisDay, amount, note);
        OperationDto newDto = new OperationDto(operationId, accountId, null, categoryId, null, thisDay, amount, note);
        BalanceEntity beforeBalanceEntity = new BalanceEntity(accountId, beforeDay, new BigDecimal("10"), false);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10"), false);
        BalanceEntity afterBalanceEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("10"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(beforeBalanceEntity).when(balanceDao).getBefore(accountId, prevDay);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterBalanceEntity).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(entity).when(converter).convertToEntity(origDto);
        doReturn(newDto).when(converter).convertToDto(entity);
        doReturn(1).when(operationDao).insert(entity);

        OperationDto expected = new OperationDto(operationId, accountId, accountTitle, categoryId, categoryTitle,
                thisDay, amount, note);
        OperationDto actual = service.create(origDto);
        assertEntityEquals(expected, actual);

        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verify(balanceDao).getBefore(accountId, prevDay);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(converter).convertToEntity(origDto);
        verify(converter).convertToDto(entity);
        verify(operationDao).insert(entity);
        verifyNoMoreDaoInteractions();
    }

}
