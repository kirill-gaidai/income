package org.kirillgaidai.income.service.impl.balanceservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.service.dto.BalanceDto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertBalanceDtoEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class BalanceServiceGetTest extends BalanceServiceBaseTest {

    /**
     * Test account id is null
     *
     * @throws Exception exception
     */
    @Test
    public void testAccountIdIsNull() throws Exception {
        LocalDate day = LocalDate.of(2017, 4, 12);

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
        try {
            service.get(1, null);
        } catch (IllegalArgumentException e) {
            assertEquals("Day is null", e.getMessage());
        }
        verifyNoMoreDaoInteractions();
    }

    @Test
    public void testBalanceNotFound() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2017, 4, 12);
        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "account1");

        doReturn(accountEntity).when(accountDao).get(accountId);

        BalanceDto expected = new BalanceDto(accountId, "account1", thisDay, BigDecimal.ZERO, false);
        BalanceDto actual = service.get(accountId, thisDay);

        assertBalanceDtoEquals(expected, actual);
        verify(accountDao).get(accountId);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verifyNoMoreDaoInteractions();
    }

    @Test
    public void testPrevBalanceFound() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2017, 4, 12);
        LocalDate prevDay = thisDay.minusDays(2L);
        BigDecimal amount = new BigDecimal("12.3");
        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "account1");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, amount, true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(prevBalanceEntity).when(balanceDao).getBefore(accountId, thisDay);

        BalanceDto expected = new BalanceDto(accountId, "account1", thisDay, amount, false);
        BalanceDto actual = service.get(accountId, thisDay);

        assertBalanceDtoEquals(expected, actual);
        verify(accountDao).get(accountId);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verifyNoMoreDaoInteractions();
    }

    @Test
    public void testNextBalanceFound() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2017, 4, 12);
        LocalDate nextDay = thisDay.plusDays(2L);
        BigDecimal amount = new BigDecimal("12.3");
        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "account1");
        BalanceEntity nextBalanceEntity = new BalanceEntity(accountId, nextDay, amount, true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(nextBalanceEntity).when(balanceDao).getAfter(accountId, thisDay);

        BalanceDto expected = new BalanceDto(accountId, "account1", thisDay, amount, false);
        BalanceDto actual = service.get(accountId, thisDay);

        assertBalanceDtoEquals(expected, actual);
        verify(accountDao).get(accountId);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);
        verifyNoMoreDaoInteractions();
    }

    @Test
    public void testOk() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2017, 4, 12);
        BigDecimal amount = new BigDecimal("12.3");
        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "account1");
        BalanceEntity balanceEntity = new BalanceEntity(accountId, thisDay, amount, true);
        BalanceDto balanceDto = new BalanceDto(accountId, null, thisDay, amount, true);

        doReturn(accountEntity).when(accountDao).get(1);
        doReturn(balanceEntity).when(balanceDao).get(1, thisDay);
        doReturn(balanceDto).when(converter).convertToDto(balanceEntity);

        BalanceDto expected = new BalanceDto(accountId, "account1", thisDay, amount, true);
        BalanceDto actual = service.get(accountId, thisDay);

        assertBalanceDtoEquals(expected, actual);
        verify(accountDao).get(accountId);
        verify(balanceDao).get(accountId, thisDay);
        verify(converter).convertToDto(balanceEntity);
        verifyNoMoreDaoInteractions();
    }

}
