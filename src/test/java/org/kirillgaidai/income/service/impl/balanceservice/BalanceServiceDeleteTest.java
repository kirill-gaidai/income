package org.kirillgaidai.income.service.impl.balanceservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.service.exception.IncomeServiceDependentOnException;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticDeleteException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class BalanceServiceDeleteTest extends BalanceServiceBaseTest {

    /**
     * Test account id is null
     *
     * @throws Exception exception
     */
    @Test
    public void testAccountIdIsNull() throws Exception {
        LocalDate day = LocalDate.of(2018, 3, 8);
        try {
            service.get(null, day);
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
        try {
            service.get(accountId, null);
        } catch (IllegalArgumentException e) {
            assertEquals("Day is null", e.getMessage());
        }
        verifyNoMoreDaoInteractions();
    }

    /**
     * Test balance not found
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceNotFound() throws Exception {
        Integer accountId = 1;
        LocalDate day = LocalDate.of(2018, 3, 8);

        doReturn(null).when(balanceDao).get(accountId, day);

        service.delete(accountId, day);

        verify(balanceDao).get(accountId, day);
        verifyNoMoreDaoInteractions();
    }

    /**
     * Test dependent operations found
     *
     * @throws Exception exception
     */
    @Test
    public void testDependentOperationsFound() throws Exception {
        Integer accountId = 1;
        LocalDate day = LocalDate.of(2018, 3, 8);

        BalanceEntity oldBalanceEntity = new BalanceEntity(accountId, day, new BigDecimal("1.00"), true);

        doReturn(oldBalanceEntity).when(balanceDao).get(accountId, day);
        doReturn(1).when(operationDao).getCountByAccountIdAndDay(accountId, day);

        try {
            service.delete(accountId, day);
        } catch (IncomeServiceDependentOnException e) {
            assertEquals(String.format("Operations dependent on account with id %d on %s found", accountId, day),
                    e.getMessage());
        }

        verify(balanceDao).get(accountId, day);
        verify(operationDao).getCountByAccountIdAndDay(accountId, day);
        verifyNoMoreDaoInteractions();
    }

    /**
     * Test balance before found. Failure
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeFoundFailure() throws Exception {
        Integer accountId = 1;
        LocalDate beforeDay = LocalDate.of(2018, 3, 6);
        LocalDate thisDay = LocalDate.of(2018, 3, 8);

        BalanceEntity beforeBalanceEntity = new BalanceEntity(accountId, beforeDay, new BigDecimal("1.00"), true);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("1.00"), true);

        doReturn(beforeBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(0).when(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        doReturn(0).when(balanceDao).delete(thisBalanceEntity);

        try {
            service.delete(accountId, thisDay);
        } catch (IncomeServiceOptimisticDeleteException e) {
            assertEquals(String.format("Balance for account with id %d on %s delete failure", accountId, thisDay),
                    e.getMessage());
        }

        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        verify(balanceDao).delete(thisBalanceEntity);
        verifyNoMoreDaoInteractions();
    }

    /**
     * Test balance before found. Successful
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeFoundSuccessful() throws Exception {
        Integer accountId = 1;
        LocalDate beforeDay = LocalDate.of(2018, 3, 6);
        LocalDate thisDay = LocalDate.of(2018, 3, 8);

        BalanceEntity beforeBalanceEntity = new BalanceEntity(accountId, beforeDay, new BigDecimal("1.00"), true);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("1.00"), true);

        doReturn(beforeBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(0).when(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        doReturn(1).when(balanceDao).delete(thisBalanceEntity);

        service.delete(accountId, thisDay);

        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        verify(balanceDao).delete(thisBalanceEntity);
        verifyNoMoreDaoInteractions();
    }

    /**
     * Test balance before not found, after not found. Failure
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeNotFoundAfterNotFoundFailure() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2018, 3, 8);

        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("1.00"), true);

        doReturn(null).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(null).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(0).when(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        doReturn(0).when(balanceDao).delete(thisBalanceEntity);

        try {
            service.delete(accountId, thisDay);
        } catch (IncomeServiceOptimisticDeleteException e) {
            assertEquals(String.format("Balance for account with id %d on %s delete failure", accountId, thisDay),
                    e.getMessage());
        }

        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        verify(balanceDao).delete(thisBalanceEntity);
        verifyNoMoreDaoInteractions();
    }

    /**
     * Test balance before not found, after not found. Successful
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeNotFoundAfterNotFoundSuccessful() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2018, 3, 8);

        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("1.00"), true);

        doReturn(null).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(null).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(0).when(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        doReturn(1).when(balanceDao).delete(thisBalanceEntity);

        service.delete(accountId, thisDay);

        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        verify(balanceDao).delete(thisBalanceEntity);
        verifyNoMoreDaoInteractions();
    }

    /**
     * Test balance before not found balance and operations after found
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeNotFoundBalanceAndOperationsAfterFound() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2018, 3, 8);
        LocalDate afterDay = LocalDate.of(2018, 3, 10);

        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("1.00"), true);
        BalanceEntity afterBalanceEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("1.00"), true);

        doReturn(null).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterBalanceEntity).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(0).when(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        doReturn(1).when(operationDao).getCountByAccountIdAndDay(accountId, afterDay);

        try {
            service.delete(accountId, thisDay);
        } catch (IncomeServiceDependentOnException e) {
            assertEquals(String.format("Operations dependent on account with id %d on %s found", accountId, afterDay),
                    e.getMessage());
        }

        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        verify(operationDao).getCountByAccountIdAndDay(accountId, afterDay);
        verifyNoMoreDaoInteractions();
    }

    /**
     * Test balance before not found balance after found. Failure
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeNotFoundBalanceAfterFoundFailure() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2018, 3, 8);
        LocalDate afterDay = LocalDate.of(2018, 3, 10);

        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("1.00"), true);
        BalanceEntity afterBalanceEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("1.00"), true);

        doReturn(null).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterBalanceEntity).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(0).when(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        doReturn(0).when(operationDao).getCountByAccountIdAndDay(accountId, afterDay);
        doReturn(0).when(balanceDao).delete(thisBalanceEntity);

        try {
            service.delete(accountId, thisDay);
        } catch (IncomeServiceOptimisticDeleteException e) {
            assertEquals(String.format("Balance for account with id %d on %s delete failure", accountId, thisDay),
                    e.getMessage());
        }

        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        verify(operationDao).getCountByAccountIdAndDay(accountId, afterDay);
        verify(balanceDao).delete(thisBalanceEntity);
        verifyNoMoreDaoInteractions();
    }

    /**
     * Test balance before not found balance after found. Successful
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeNotFoundBalanceAfterFoundSuccessful() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2018, 3, 8);
        LocalDate afterDay = LocalDate.of(2018, 3, 10);

        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("1.00"), true);
        BalanceEntity afterBalanceEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("1.00"), true);

        doReturn(null).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterBalanceEntity).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(0).when(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        doReturn(0).when(operationDao).getCountByAccountIdAndDay(accountId, afterDay);
        doReturn(1).when(balanceDao).delete(thisBalanceEntity);

        service.delete(accountId, thisDay);

        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(operationDao).getCountByAccountIdAndDay(accountId, thisDay);
        verify(operationDao).getCountByAccountIdAndDay(accountId, afterDay);
        verify(balanceDao).delete(thisBalanceEntity);
        verifyNoMoreDaoInteractions();
    }

}
