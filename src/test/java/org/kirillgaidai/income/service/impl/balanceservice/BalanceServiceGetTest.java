package org.kirillgaidai.income.service.impl.balanceservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.service.dto.BalanceDto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
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

    /**
     * Test this balance found
     *
     * @throws Exception exception
     */
    @Test
    public void testThisBalanceFound() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2017, 4, 12);

        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "title");
        BalanceEntity thisEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(thisEntity).when(balanceDao).get(accountId, thisDay);

        BalanceDto expected = new BalanceDto(accountId, "title", thisDay, new BigDecimal("10"), true);
        BalanceDto actual = service.get(accountId, thisDay);
        assertEntityEquals(expected, actual);

        verify(accountDao).get(accountId);
        verify(balanceDao).get(accountId, thisDay);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test before balance found
     *
     * @throws Exception exception
     */
    @Test
    public void testBeforeBalanceFound() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2017, 4, 12);
        LocalDate prevDay = LocalDate.of(2017, 4, 10);

        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "account1");
        BalanceEntity beforeEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(null).when(balanceDao).get(accountId, thisDay);
        doReturn(beforeEntity).when(balanceDao).getBefore(accountId, thisDay);

        BalanceDto expected = new BalanceDto(accountId, "account1", thisDay, new BigDecimal("10"), false);
        BalanceDto actual = service.get(accountId, thisDay);
        assertEntityEquals(expected, actual);

        verify(accountDao).get(accountId);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getBefore(accountId, thisDay);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test after balance found
     *
     * @throws Exception exception
     */
    @Test
    public void testAfterBalanceFound() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2017, 4, 12);
        LocalDate nextDay = LocalDate.of(2017, 4, 14);

        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "account1");
        BalanceEntity afterEntity = new BalanceEntity(accountId, nextDay, new BigDecimal("10"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(null).when(balanceDao).get(accountId, thisDay);
        doReturn(null).when(balanceDao).getBefore(accountId, thisDay);
        doReturn(afterEntity).when(balanceDao).getAfter(accountId, thisDay);

        BalanceDto expected = new BalanceDto(accountId, "account1", thisDay, new BigDecimal("10"), false);
        BalanceDto actual = service.get(accountId, thisDay);
        assertEntityEquals(expected, actual);

        verify(accountDao).get(accountId);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getBefore(accountId, thisDay);
        verify(balanceDao).getAfter(accountId, thisDay);

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
        LocalDate day = LocalDate.of(2017, 4, 12);

        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "account1");

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(null).when(balanceDao).get(accountId, day);
        doReturn(null).when(balanceDao).getBefore(accountId, day);
        doReturn(null).when(balanceDao).getAfter(accountId, day);

        BalanceDto expected = new BalanceDto(accountId, "account1", day, BigDecimal.ZERO, false);
        BalanceDto actual = service.get(accountId, day);
        assertEntityEquals(expected, actual);

        verify(accountDao).get(accountId);
        verify(balanceDao).get(accountId, day);
        verify(balanceDao).getBefore(accountId, day);
        verify(balanceDao).getAfter(accountId, day);

        verifyNoMoreDaoInteractions();
    }

}
