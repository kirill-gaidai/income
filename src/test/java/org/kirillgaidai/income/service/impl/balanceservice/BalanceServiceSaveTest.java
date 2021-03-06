package org.kirillgaidai.income.service.impl.balanceservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.service.dto.BalanceDto;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticCreateException;
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

public class BalanceServiceSaveTest extends BalanceServiceBaseTest {

    /**
     * Test dto is null
     *
     * @throws Exception exception
     */
    @Test
    public void testNull() throws Exception {
        try {
            service.save(null);
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
        LocalDate day = LocalDate.of(2107, 4, 12);

        BalanceDto dto = new BalanceDto(null, null, day, new BigDecimal("12.3"), false);

        try {
            service.save(dto);
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

        BalanceDto dto = new BalanceDto(accountId, null, null, new BigDecimal("12.3"), false);

        try {
            service.save(dto);
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
        LocalDate day = LocalDate.of(2107, 4, 12);

        BalanceDto dto = new BalanceDto(accountId, null, day, new BigDecimal("12.3"), false);

        doReturn(null).when(accountDao).get(accountId);

        try {
            service.save(dto);
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Account with id %d not found", accountId), e.getMessage());
        }

        verify(accountDao).get(accountId);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test failure when balance found
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceFoundFailure() throws Exception {
        Integer accountId = 1;
        LocalDate day = LocalDate.of(2107, 4, 12);

        BalanceDto dto = new BalanceDto(1, null, day, new BigDecimal("12"), true);
        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "title");
        BalanceEntity oldEntity = new BalanceEntity(accountId, day, new BigDecimal("10"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(oldEntity).when(balanceDao).get(accountId, day);
        doReturn(0).when(balanceDao).update(any(BalanceEntity.class), eq(oldEntity));

        try {
            service.save(dto);
        } catch (IncomeServiceOptimisticUpdateException e) {
            assertEquals(String.format("Balance for account with id %d on %s update failure", accountId, day),
                    e.getMessage());
        }

        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).get(accountId);
        verify(balanceDao).get(accountId, day);
        verify(balanceDao).update(argumentCaptor.capture(), eq(oldEntity));

        BalanceEntity expectedEntity = new BalanceEntity(accountId, day, new BigDecimal("12"), true);
        BalanceEntity actualEntity = argumentCaptor.getValue();
        assertEntityEquals(expectedEntity, actualEntity);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test successful when balance was found
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceFoundSuccessful() throws Exception {
        Integer accountId = 1;
        LocalDate day = LocalDate.of(2107, 4, 12);

        BalanceDto dto = new BalanceDto(1, null, day, new BigDecimal("12"), true);
        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "title");
        BalanceEntity oldEntity = new BalanceEntity(accountId, day, new BigDecimal("10"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(oldEntity).when(balanceDao).get(accountId, day);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class), eq(oldEntity));

        BalanceDto expected = new BalanceDto(accountId, "title", day, new BigDecimal("12"), true);
        BalanceDto actual = service.save(dto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).get(accountId);
        verify(balanceDao).get(accountId, day);
        verify(balanceDao).update(argumentCaptor.capture(), eq(oldEntity));

        BalanceEntity expectedEntity = new BalanceEntity(accountId, day, new BigDecimal("12"), true);
        BalanceEntity actualEntity = argumentCaptor.getValue();
        assertEntityEquals(expectedEntity, actualEntity);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test failure when balance not found
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceNotFoundFailure() throws Exception {
        Integer accountId = 1;
        LocalDate day = LocalDate.of(2107, 4, 12);

        BalanceDto dto = new BalanceDto(accountId, null, day, new BigDecimal("12"), true);
        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "title");

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(null).when(balanceDao).get(accountId, day);
        doReturn(0).when(balanceDao).insert(any(BalanceEntity.class));

        try {
            service.save(dto);
        } catch (IncomeServiceOptimisticCreateException e) {
            assertEquals(String.format("Balance for account with id %d on %s create failure", accountId, day),
                    e.getMessage());
        }

        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).get(accountId);
        verify(balanceDao).get(accountId, day);
        verify(balanceDao).insert(argumentCaptor.capture());

        BalanceEntity expectedEntity = new BalanceEntity(accountId, day, new BigDecimal("12"), true);
        BalanceEntity actualEntity = argumentCaptor.getValue();
        assertEntityEquals(expectedEntity, actualEntity);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test successful when balance not found
     *
     * @throws Exception exception
     */
    @Test
    public void testBalanceNotFoundSuccessful() throws Exception {
        Integer accountId = 1;
        LocalDate day = LocalDate.of(2107, 4, 12);

        BalanceDto dto = new BalanceDto(accountId, null, day, new BigDecimal("12"), true);
        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "title");

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(null).when(balanceDao).get(accountId, day);
        doReturn(1).when(balanceDao).insert(any(BalanceEntity.class));

        BalanceDto expected = new BalanceDto(1, "title", day, new BigDecimal("12"), true);
        BalanceDto actual = service.save(dto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).get(accountId);
        verify(balanceDao).get(accountId, day);
        verify(balanceDao).insert(argumentCaptor.capture());

        BalanceEntity expectedEntity = new BalanceEntity(accountId, day, new BigDecimal("12"), true);
        BalanceEntity actualEntity = argumentCaptor.getValue();
        assertEntityEquals(expectedEntity, actualEntity);

        verifyNoMoreDaoInteractions();
    }

}
