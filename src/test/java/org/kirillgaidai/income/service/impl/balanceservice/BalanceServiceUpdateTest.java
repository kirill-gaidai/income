package org.kirillgaidai.income.service.impl.balanceservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.service.dto.BalanceDto;
import org.kirillgaidai.income.service.exception.IncomeServiceException;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticDeleteException;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticUpdateException;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class BalanceServiceUpdateTest extends BalanceServiceBaseTest {

    /**
     * Test dto is null
     *
     * @throws Exception exception
     */
    @Test
    public void testNull() throws Exception {
        try {
            service.update(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Dto is null", e.getMessage());
        }

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test account id is null
     *
     * @throws Exception exception
     */
    @Test
    public void testAccountIdIsNull() throws Exception {
        LocalDate day = LocalDate.of(2017, 11, 27);

        BalanceDto dto = new BalanceDto(null, null, day, new BigDecimal("10"), true);

        try {
            service.update(dto);
        } catch (IllegalArgumentException e) {
            assertEquals("Account id is null", e.getMessage());
        }

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test day is null
     *
     * @throws Exception exception
     */
    @Test
    public void testDayIsNull() throws Exception {
        Integer accountId = 1;

        BalanceDto dto = new BalanceDto(accountId, null, null, new BigDecimal("10"), true);

        try {
            service.update(dto);
        } catch (IllegalArgumentException e) {
            assertEquals("Day is null", e.getMessage());
        }

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test account not found
     *
     * @throws Exception exception
     */
    @Test
    public void testAccountNotFound() throws Exception {
        Integer accountId = 1;
        LocalDate day = LocalDate.of(2017, 11, 27);

        BalanceDto dto = new BalanceDto(accountId, null, day, new BigDecimal("10"), true);

        doReturn(null).when(accountDao).get(accountId);

        try {
            service.update(dto);
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Account with id %d not found", accountId), e.getMessage());
        }

        verify(accountDao).get(accountId);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        Integer accountId = 1;
        LocalDate day = LocalDate.of(2017, 11, 27);

        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "title");
        BalanceDto dto = new BalanceDto(accountId, null, day, new BigDecimal("10"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(null).when(balanceDao).get(accountId, day);

        try {
            service.update(dto);
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Balance for account with id %d on %s not found", accountId, day),
                    e.getMessage());
        }

        verify(accountDao).get(accountId);
        verify(balanceDao).get(accountId, day);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test failure
     *
     * @throws Exception exception
     */
    @Test
    public void testFailure() throws Exception {
        Integer accountId = 1;
        LocalDate day = LocalDate.of(2017, 11, 27);

        BalanceDto dto = new BalanceDto(accountId, null, day, new BigDecimal("10"), true);
        BalanceEntity oldEntity = new BalanceEntity(accountId, day, new BigDecimal("5"), false);
        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "title");

        doReturn(oldEntity).when(balanceDao).get(accountId, day);
        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(0).when(balanceDao).update(any(BalanceEntity.class), eq(oldEntity));

        try {
            service.update(dto);
        } catch (IncomeServiceOptimisticUpdateException e) {
            assertEquals(String.format("Balance for account with id %d on %s update failure",accountId, day),
                    e.getMessage());
        }

        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(balanceDao).get(accountId, day);
        verify(accountDao).get(accountId);
        verify(balanceDao).update(argumentCaptor.capture(), eq(oldEntity));

        BalanceEntity expectedEntity = new BalanceEntity(accountId, day, new BigDecimal("10"), true);
        BalanceEntity actualEntity = argumentCaptor.getValue();
        assertEntityEquals(expectedEntity, actualEntity);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Integer accountId = 1;
        LocalDate day = LocalDate.of(2017, 11, 27);

        BalanceDto dto = new BalanceDto(accountId, null, day, new BigDecimal("10"), true);
        BalanceEntity oldEntity = new BalanceEntity(accountId, day, new BigDecimal("5"), false);
        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "title");

        doReturn(oldEntity).when(balanceDao).get(accountId, day);
        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class), eq(oldEntity));

        BalanceDto expected = new BalanceDto(accountId, "title", day, new BigDecimal("10"), true);
        BalanceDto actual = service.update(dto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(balanceDao).get(accountId, day);
        verify(accountDao).get(accountId);
        verify(balanceDao).update(argumentCaptor.capture(), eq(oldEntity));

        BalanceEntity expectedEntity = new BalanceEntity(accountId, day, new BigDecimal("10"), true);
        BalanceEntity actualEntity = argumentCaptor.getValue();
        assertEntityEquals(expectedEntity, actualEntity);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test balance to auto this day operations found
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceToAutoThisDayOperationsFound() throws Exception {
        Integer accountId = 1;
        LocalDate day = LocalDate.of(2017, 11, 27);

        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "title");
        BalanceDto dto = new BalanceDto(accountId, null, day, new BigDecimal("10"), false);
        BalanceEntity oldEntity = new BalanceEntity(accountId, day, new BigDecimal("5"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(oldEntity).when(balanceDao).get(accountId, day);
        doReturn(1).when(operationDao).getCountByAccountIdAndDay(accountId, day);

        try {
            service.update(dto);
        } catch (IncomeServiceException e) {
            assertEquals(String.format("Balance for account with is %d on %s must be manual", accountId, day),
                    e.getMessage());
        }

        verify(balanceDao).get(accountId, day);
        verify(accountDao).get(accountId);
        verify(operationDao).getCountByAccountIdAndDay(accountId, day);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test balance to auto previous balance found. Failure
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceToAutoPreviousBalanceFoundFailure() throws Exception {
        Integer accountId = 1;
        LocalDate prevDay = LocalDate.of(2017, 11, 25);
        LocalDate thisDay = LocalDate.of(2017, 11, 27);

        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "title");
        BalanceDto dto = new BalanceDto(accountId, null, thisDay, new BigDecimal("10"), false);
        BalanceEntity prevEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("5"), true);
        BalanceEntity thisEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("5"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(prevEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(0).when(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        doReturn(0).when(balanceDao).delete(thisEntity);

        try {
            service.update(dto);
        } catch (IncomeServiceOptimisticDeleteException e) {
            assertEquals(String.format("Balance for account with id %d on %s delete failure", accountId, thisDay),
                    e.getMessage());
        }

        verify(accountDao).get(accountId);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        verify(balanceDao).delete(thisEntity);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test balance to auto previous balance found. Success
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceToAutoPreviousBalanceFoundSuccess() throws Exception {
        Integer accountId = 1;
        LocalDate prevDay = LocalDate.of(2017, 11, 25);
        LocalDate thisDay = LocalDate.of(2017, 11, 27);

        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "title");
        BalanceDto dto = new BalanceDto(accountId, null, thisDay, new BigDecimal("10"), false);
        BalanceEntity prevEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("5"), true);
        BalanceEntity thisEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("5"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(prevEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(0).when(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        doReturn(1).when(balanceDao).delete(thisEntity);

        BalanceDto expected = new BalanceDto(accountId, "title", thisDay, new BigDecimal("10"), false);
        BalanceDto actual = service.update(dto);
        assertEntityEquals(expected, actual);

        verify(accountDao).get(accountId);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        verify(balanceDao).delete(thisEntity);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test balance to auto next balance not found. Failure
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceToAutoNextBalanceNotFoundFailure() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2017, 11, 27);

        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "title");
        BalanceDto dto = new BalanceDto(accountId, null, thisDay, new BigDecimal("10"), false);
        BalanceEntity thisEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("5"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(null).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(null).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(0).when(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        doReturn(0).when(balanceDao).delete(thisEntity);

        try {
            service.update(dto);
        } catch (IncomeServiceOptimisticDeleteException e) {
            assertEquals(String.format("Balance for account with id %d on %s delete failure", accountId, thisDay),
                    e.getMessage());
        }

        verify(accountDao).get(accountId);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        verify(balanceDao).delete(thisEntity);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test balance to auto next balance not found. Success
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceToAutoNextBalanceNotFoundSuccess() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2017, 11, 27);

        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "title");
        BalanceDto dto = new BalanceDto(accountId, null, thisDay, new BigDecimal("10"), false);
        BalanceEntity thisEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("5"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(null).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(null).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(0).when(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        doReturn(1).when(balanceDao).delete(thisEntity);

        BalanceDto expected = new BalanceDto(accountId, "title", thisDay, new BigDecimal("10"), false);
        BalanceDto actual = service.update(dto);
        assertEntityEquals(expected, actual);

        verify(accountDao).get(accountId);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        verify(balanceDao).delete(thisEntity);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test balance to auto next balance found. Failure
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceToAutoNextBalanceFoundFailure() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2017, 11, 27);
        LocalDate afterDay = LocalDate.of(2017, 11, 29);

        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "title");
        BalanceDto dto = new BalanceDto(accountId, null, thisDay, new BigDecimal("10"), false);
        BalanceEntity thisEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("5"), true);
        BalanceEntity afterEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("5"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(null).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterEntity).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(0).when(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        doReturn(0).when(operationDao).getCountByAccountIdAndDay(accountId, afterDay);
        doReturn(0).when(balanceDao).delete(thisEntity);

        try {
            service.update(dto);
        } catch (IncomeServiceOptimisticDeleteException e) {
            assertEquals(String.format("Balance for account with id %d on %s delete failure", accountId, thisDay),
                    e.getMessage());
        }

        verify(accountDao).get(accountId);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        verify(operationDao).getCountByAccountIdAndDay(accountId, afterDay);
        verify(balanceDao).delete(thisEntity);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test balance to auto next balance found. Success
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceToAutoNextBalanceFoundSuccess() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2017, 11, 27);
        LocalDate afterDay = LocalDate.of(2017, 11, 29);

        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "title");
        BalanceDto dto = new BalanceDto(accountId, null, thisDay, new BigDecimal("10"), false);
        BalanceEntity thisEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("5"), true);
        BalanceEntity afterEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("5"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(null).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterEntity).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(0).when(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        doReturn(0).when(operationDao).getCountByAccountIdAndDay(accountId, afterDay);
        doReturn(1).when(balanceDao).delete(thisEntity);

        BalanceDto expected = new BalanceDto(accountId, "title", thisDay, new BigDecimal("10"), false);
        BalanceDto actual = service.update(dto);
        assertEntityEquals(expected, actual);

        verify(accountDao).get(accountId);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        verify(operationDao).getCountByAccountIdAndDay(accountId, afterDay);
        verify(balanceDao).delete(thisEntity);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test balance to auto next balance and operations found
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceToAutoNextBalanceAndOperationsFound() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2017, 11, 27);
        LocalDate afterDay = LocalDate.of(2017, 11, 29);

        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "title");
        BalanceDto dto = new BalanceDto(accountId, null, thisDay, new BigDecimal("10"), false);
        BalanceEntity thisEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("5"), true);
        BalanceEntity afterEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("5"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(null).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterEntity).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(0).when(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        doReturn(1).when(operationDao).getCountByAccountIdAndDay(accountId, afterDay);

        try {
            service.update(dto);
        } catch (IncomeServiceException e) {
            assertEquals(String.format("Balance for account with is %d on %s must be manual", accountId, thisDay),
                    e.getMessage());
        }

        verify(accountDao).get(accountId);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        verify(operationDao).getCountByAccountIdAndDay(accountId, afterDay);

        verifyNoMoreDaoInteractions();
    }

}
