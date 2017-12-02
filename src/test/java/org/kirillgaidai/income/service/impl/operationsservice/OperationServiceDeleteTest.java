package org.kirillgaidai.income.service.impl.operationsservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;
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
 * {@link org.kirillgaidai.income.service.impl.OperationService#delete(Integer)} test
 *
 * @author Kirill Gaidai
 */
public class OperationServiceDeleteTest extends OperationServiceBaseTest {

    /**
     * Test id is null
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
     * Test operation isn't found
     *
     * @throws Exception exception
     */
    @Test
    public void testOperationNotFound() throws Exception {
        Integer operationId = 1;

        doReturn(null).when(operationDao).get(operationId);

        try {
            service.delete(operationId);
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Operation with id %d not found", operationId), e.getMessage());
        }

        verify(operationDao).get(operationId);
        verifyNoMoreDaoInteractions();
    }

    /**
     * Test this balance isn't found
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceThisNotFound() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 9, 5);
        LocalDate prevDay = LocalDate.of(2017, 9, 3);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), false);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(null).when(balanceDao).get(accountId, thisDay);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);

        try {
            service.delete(operationId);
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Balance for account with id %d on %s not found", accountId, thisDay),
                    e.getMessage());
        }

        verify(operationDao).get(operationId);
        verify(balanceDao).get(accountId, thisDay);
        verifyNoMoreDaoInteractions();
    }

    /**
     * Test previous balance isn't found
     *
     * @throws Exception exception
     */
    @Test
    public void testBalancePrevNotFound() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 9, 5);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note");
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), false);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(null).when(balanceDao).getBefore(accountId, thisDay);

        try {
            service.delete(operationId);
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Balance for account with id %d before %s not found", accountId, thisDay),
                    e.getMessage());
        }

        verify(operationDao).get(operationId);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verifyNoMoreDaoInteractions();
    }

    /**
     * Previous balance is fixed, this balance is fixed.
     * No balance is updated. Operation is deleted
     *
     * @throws Exception exception
     */
    @Test
    public void testBalancePrevFixedThisFixed() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("11.25"), true);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), true);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(1).when(operationDao).delete(operationEntity);

        service.delete(operationId);

        verify(operationDao).get(operationId);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(operationDao).delete(operationEntity);
        verifyNoMoreDaoInteractions();
    }

    /**
     * Previous balance is fixed. This balance isn't fixed. After balance isn't found.
     * This balance is updated. Operation is deleted
     *
     * @throws Exception exception
     */
    @Test
    public void testBalancePrevFixedThisNotFixedAfterNotFound() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("11.25"), true);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("9.00"), false);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(null).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class), eq(thisBalanceEntity));
        doReturn(1).when(operationDao).delete(operationEntity);

        service.delete(operationId);

        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(operationDao).get(operationId);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(balanceDao).update(argumentCaptor.capture(), eq(thisBalanceEntity));
        verify(operationDao).delete(operationEntity);
        verifyNoMoreDaoInteractions();

        BalanceEntity expected = new BalanceEntity(accountId, thisDay, new BigDecimal("10.25"), false);
        assertEntityEquals(expected, argumentCaptor.getValue());
    }

    /**
     * Prev balance is fixed. This balance isn't fixed. After balance is found.
     * No balance is updated. Operation is deleted
     *
     * @throws Exception exception
     */
    @Test
    public void testBalancePrevFixedThisNotFixedAfterFound() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        LocalDate afterDay = LocalDate.of(2017, 5, 9);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("11.25"), true);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), false);
        BalanceEntity afterBalanceEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("10.00"), false);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterBalanceEntity).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(1).when(operationDao).delete(operationEntity);

        service.delete(operationId);

        verify(operationDao).get(operationId);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(operationDao).delete(operationEntity);
        verifyNoMoreDaoInteractions();
    }

    /**
     * Before balance isn't found. Previous balance isn't fixed. This balance is fixed.
     * Previous balance is updated. Operation is deleted
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeNotFoundPrevNotFixedThisFixed() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("12.25"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), true);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(null).when(balanceDao).getBefore(accountId, prevDay);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class), eq(prevBalanceEntity));
        doReturn(1).when(operationDao).delete(operationEntity);

        service.delete(operationId);

        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(operationDao).get(operationId);
        verify(balanceDao).getBefore(accountId, prevDay);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).update(argumentCaptor.capture(), eq(prevBalanceEntity));
        verify(operationDao).delete(operationEntity);
        verifyNoMoreDaoInteractions();

        BalanceEntity expected = new BalanceEntity(accountId, prevDay, new BigDecimal("11.00"), false);
        assertEntityEquals(expected, argumentCaptor.getValue());
    }

    /**
     * Before balance is found. Previous balance isn't fixed. This balance is fixed.
     * No balance is updated. Operation is deleted
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeFoundPrevNotFixedThisFixed() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate beforeDay = LocalDate.of(2017, 5, 3);
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note");
        BalanceEntity beforeBalanceEntity = new BalanceEntity(accountId, beforeDay, new BigDecimal("11.25"), false);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("11.25"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), true);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(beforeBalanceEntity).when(balanceDao).getBefore(accountId, prevDay);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(1).when(operationDao).delete(operationEntity);

        service.delete(operationId);

        verify(operationDao).get(operationId);
        verify(balanceDao).getBefore(accountId, prevDay);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(operationDao).delete(operationEntity);
        verifyNoMoreDaoInteractions();
    }

    /**
     * Before balance isn't found. Previous balance isn't fixed. This balance isn't fixed. After balance isn't found.
     * This balance is updated. Operation is deleted.
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeNotFoundPrevNotFixedThisNotFixedAfterNotFound() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("7.75"), false);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class), eq(thisBalanceEntity));
        doReturn(1).when(operationDao).delete(operationEntity);

        service.delete(operationId);

        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(operationDao).get(operationId);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(balanceDao).update(argumentCaptor.capture(), eq(thisBalanceEntity));
        verify(operationDao).delete(operationEntity);
        verifyNoMoreDaoInteractions();

        BalanceEntity expected = new BalanceEntity(accountId, thisDay, new BigDecimal("9.00"), false);
        assertEntityEquals(expected, argumentCaptor.getValue());
    }

    /**
     * Before balance is found. Previous balance isn't fixed. This balance isn't fixed. After balance isn't found.
     * This balance is updated. Operation is deleted.
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeFoundPrevNotFixedThisNotFixedAfterNotFound() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate beforeDay = LocalDate.of(2017, 5, 3);
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity beforeBalanceEntity = new BalanceEntity(accountId, beforeDay, new BigDecimal("10.00"), false);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("7.75"), false);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(beforeBalanceEntity).when(balanceDao).getBefore(accountId, prevDay);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(null).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class), eq(thisBalanceEntity));
        doReturn(1).when(operationDao).delete(operationEntity);

        service.delete(operationId);

        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(operationDao).get(operationId);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(balanceDao).update(argumentCaptor.capture(), eq(thisBalanceEntity));
        verify(operationDao).delete(operationEntity);
        verifyNoMoreDaoInteractions();

        BalanceEntity expected = new BalanceEntity(accountId, thisDay, new BigDecimal("9.00"), false);
        assertEntityEquals(expected, argumentCaptor.getValue());
    }

    /**
     * Before balance isn't found. Previous balance isn't fixed. This balance isn't fixed. After balance is found.
     * Previous balance is updated. Operation is deleted
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeNotFondPrevNotFixedThisNotFixedAfterFond() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        LocalDate afterDay = LocalDate.of(2017, 5, 9);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("11.00"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), false);
        BalanceEntity afterBalanceEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("8.75"), false);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterBalanceEntity).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class), eq(prevBalanceEntity));
        doReturn(1).when(operationDao).delete(operationEntity);

        service.delete(operationId);

        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(operationDao).get(operationId);
        verify(balanceDao).getBefore(accountId, prevDay);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(balanceDao).update(argumentCaptor.capture(), eq(prevBalanceEntity));
        verify(operationDao).delete(operationEntity);
        verifyNoMoreDaoInteractions();

        BalanceEntity expected = new BalanceEntity(accountId, prevDay, new BigDecimal("9.75"), false);
        assertEntityEquals(expected, argumentCaptor.getValue());
    }

    /**
     * Before balance is found. Previous balance isn't fixed. This balance isn't fixed. After balance is found.
     * No balance is updated. Operation is deleted
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceBeforeFoundPrevNotFixedThisNotFixedAfterFound() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate beforeDay = LocalDate.of(2017, 5, 3);
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        LocalDate afterDay = LocalDate.of(2017, 5, 9);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity beforeBalanceEntity = new BalanceEntity(accountId, beforeDay, new BigDecimal("10.00"), false);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), false);
        BalanceEntity afterBalanceEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("8.75"), false);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(beforeBalanceEntity).when(balanceDao).getBefore(accountId, prevDay);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterBalanceEntity).when(balanceDao).getAfter(accountId, thisDay);
        doReturn(1).when(operationDao).delete(operationEntity);

        service.delete(operationId);

        verify(operationDao).get(operationId);
        verify(balanceDao).getBefore(accountId, prevDay);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verify(operationDao).delete(operationEntity);
        verifyNoMoreDaoInteractions();
    }

}
