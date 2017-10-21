package org.kirillgaidai.income.service.impl.balanceservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.impl.AccountDao;
import org.kirillgaidai.income.dao.impl.BalanceDao;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.kirillgaidai.income.service.converter.BalanceConverter;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.BalanceDto;
import org.kirillgaidai.income.service.exception.IncomeServiceAccountNotFoundException;
import org.kirillgaidai.income.service.impl.BalanceService;
import org.kirillgaidai.income.service.intf.IBalanceService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertBalanceDtoEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class BalanceServiceGetTest {

    final private IBalanceDao balanceDao = mock(BalanceDao.class);
    final private IAccountDao accountDao = mock(AccountDao.class);
    final private IGenericConverter<BalanceEntity, BalanceDto> converter = mock(BalanceConverter.class);
    final private IBalanceService service = new BalanceService(balanceDao, accountDao, converter);

    @Test
    public void testAccountNull() throws Exception {
        LocalDate day = LocalDate.of(2017, 4, 12);
        try {
            service.get(null, day);
        } catch (IllegalArgumentException e) {
            assertEquals("null", e.getMessage());
        }
        verifyNoMoreInteractions(balanceDao, accountDao, converter);
    }

    @Test
    public void testDayNull() throws Exception {
        try {
            service.get(1, null);
        } catch (IllegalArgumentException e) {
            assertEquals("null", e.getMessage());
        }
        verifyNoMoreInteractions(balanceDao, accountDao, converter);
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
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getEntityAfter(accountId, thisDay);
        verifyNoMoreInteractions(balanceDao, accountDao, converter);
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
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);

        BalanceDto expected = new BalanceDto(accountId, "account1", thisDay, amount, false);
        BalanceDto actual = service.get(accountId, thisDay);

        assertBalanceDtoEquals(expected, actual);
        verify(accountDao).get(accountId);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verifyNoMoreInteractions(balanceDao, accountDao, converter);
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
        doReturn(nextBalanceEntity).when(balanceDao).getEntityAfter(accountId, thisDay);

        BalanceDto expected = new BalanceDto(accountId, "account1", thisDay, amount, false);
        BalanceDto actual = service.get(accountId, thisDay);

        assertBalanceDtoEquals(expected, actual);
        verify(accountDao).get(accountId);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getEntityAfter(accountId, thisDay);
        verifyNoMoreInteractions(balanceDao, accountDao, converter);
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
        verifyNoMoreInteractions(balanceDao, accountDao, converter);
    }

}
