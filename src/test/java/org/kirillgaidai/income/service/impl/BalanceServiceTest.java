package org.kirillgaidai.income.service.impl;

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
import org.kirillgaidai.income.service.exception.IncomeServiceBalanceNotFoundException;
import org.kirillgaidai.income.service.intf.IBalanceService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertBalanceDtoEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class BalanceServiceTest {

    final private IBalanceDao balanceDao = mock(BalanceDao.class);
    final private IAccountDao accountDao = mock(AccountDao.class);
    final private IGenericConverter<BalanceEntity, BalanceDto> balanceConverter = mock(BalanceConverter.class);
    final private IBalanceService balanceService = new BalanceService(balanceDao, accountDao, balanceConverter);

    @Test
    public void testGetDto_Null() throws Exception {
        try {
            balanceService.getDto(null, null);
        } catch (IncomeServiceBalanceNotFoundException e) {
            assertEquals("Balance not found", e.getMessage());
        }
        verifyNoMoreInteractions(balanceDao, accountDao, balanceConverter);
    }

    @Test
    public void testGetDto_AccountNotFound() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2017, 4, 12);
        try {
            balanceService.getDto(accountId, thisDay);
        } catch (IncomeServiceAccountNotFoundException e) {
            assertEquals(String.format("Account with id %d not found", accountId), e.getMessage());
        }
        verify(accountDao).getEntity(accountId);
        verifyNoMoreInteractions(balanceDao, accountDao, balanceConverter);
    }

    @Test
    public void testGetDto_BalanceNotFound() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2017, 4, 12);
        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "account1");

        doReturn(accountEntity).when(accountDao).getEntity(accountId);

        BalanceDto expected = new BalanceDto(accountId, "account1", thisDay, BigDecimal.ZERO, false);
        BalanceDto actual = balanceService.getDto(accountId, thisDay);

        assertBalanceDtoEquals(expected, actual);
        verify(accountDao).getEntity(accountId);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).getEntity(accountId, thisDay);
        verify(balanceDao).getEntityAfter(accountId, thisDay);
        verifyNoMoreInteractions(balanceDao, accountDao, balanceConverter);
    }

    @Test
    public void testGetDto_PrevBalanceFound() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2017, 4, 12);
        LocalDate prevDay = thisDay.minusDays(2L);
        BigDecimal amount = new BigDecimal("12.3");
        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "account1");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, amount, true);

        doReturn(accountEntity).when(accountDao).getEntity(accountId);
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);

        BalanceDto expected = new BalanceDto(accountId, "account1", thisDay, amount, false);
        BalanceDto actual = balanceService.getDto(accountId, thisDay);

        assertBalanceDtoEquals(expected, actual);
        verify(accountDao).getEntity(accountId);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).getEntity(accountId, thisDay);
        verifyNoMoreInteractions(balanceDao, accountDao, balanceConverter);
    }

    @Test
    public void testGetDto_NextBalanceFound() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2017, 4, 12);
        LocalDate nextDay = thisDay.plusDays(2L);
        BigDecimal amount = new BigDecimal("12.3");
        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "account1");
        BalanceEntity nextBalanceEntity = new BalanceEntity(accountId, nextDay, amount, true);

        doReturn(accountEntity).when(accountDao).getEntity(accountId);
        doReturn(nextBalanceEntity).when(balanceDao).getEntityAfter(accountId, thisDay);

        BalanceDto expected = new BalanceDto(accountId, "account1", thisDay, amount, false);
        BalanceDto actual = balanceService.getDto(accountId, thisDay);

        assertBalanceDtoEquals(expected, actual);
        verify(accountDao).getEntity(accountId);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).getEntity(accountId, thisDay);
        verify(balanceDao).getEntityAfter(accountId, thisDay);
        verifyNoMoreInteractions(balanceDao, accountDao, balanceConverter);
    }

    @Test
    public void testGetDto_Ok() throws Exception {
        Integer accountId = 1;
        LocalDate thisDay = LocalDate.of(2017, 4, 12);
        BigDecimal amount = new BigDecimal("12.3");
        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "account1");
        BalanceEntity balanceEntity = new BalanceEntity(accountId, thisDay, amount, true);
        BalanceDto balanceDto = new BalanceDto(accountId, null, thisDay, amount, true);

        doReturn(accountEntity).when(accountDao).getEntity(1);
        doReturn(balanceEntity).when(balanceDao).getEntity(1, thisDay);
        doReturn(balanceDto).when(balanceConverter).convertToDto(balanceEntity);

        BalanceDto expected = new BalanceDto(accountId, "account1", thisDay, amount, true);
        BalanceDto actual = balanceService.getDto(accountId, thisDay);

        assertBalanceDtoEquals(expected, actual);
        verify(accountDao).getEntity(accountId);
        verify(balanceDao).getEntity(accountId, thisDay);
        verify(balanceConverter).convertToDto(balanceEntity);
        verifyNoMoreInteractions(balanceDao, accountDao, balanceConverter);
    }

    @Test
    public void testSaveDto_Null() throws Exception {
        try {
            balanceService.saveDto(null);
        } catch (IncomeServiceBalanceNotFoundException e) {
            assertEquals("Balance not found", e.getMessage());
        }
        verifyNoMoreInteractions(balanceDao, accountDao, balanceConverter);
    }

    @Test
    public void testSaveDto_AccountNull() throws Exception {
        LocalDate day = LocalDate.of(2107, 4, 12);
        BigDecimal amount = new BigDecimal("12.3");
        BalanceDto balanceDto = new BalanceDto(null, null, day, amount, false);
        try {
            balanceService.saveDto(balanceDto);
        } catch (IncomeServiceAccountNotFoundException e) {
            assertEquals("Account not found", e.getMessage());
        }
        verifyNoMoreInteractions(balanceDao, accountDao, balanceConverter);
    }

    @Test
    public void testSaveDto_AccountNotFound() throws Exception {
        LocalDate day = LocalDate.of(2107, 4, 12);
        BigDecimal amount = new BigDecimal("12.3");
        BalanceDto balanceDto = new BalanceDto(1, null, day, amount, false);
        try {
            balanceService.saveDto(balanceDto);
        } catch (IncomeServiceAccountNotFoundException e) {
            assertEquals("Account with id 1 not found", e.getMessage());
        }
        verify(accountDao).getEntity(1);
        verifyNoMoreInteractions(balanceDao, accountDao, balanceConverter);
    }

    @Test
    public void testSaveDto_BalanceFound() throws Exception {
        LocalDate day = LocalDate.of(2107, 4, 12);
        BigDecimal amount = new BigDecimal("12.3");
        AccountEntity accountEntity = new AccountEntity(1, 2, "01", "account1");
        BalanceEntity balanceEntity = new BalanceEntity(1, day, amount, true);
        BalanceDto balanceDto = new BalanceDto(1, null, day, amount, false);

        doReturn(accountEntity).when(accountDao).getEntity(1);
        doReturn(balanceEntity).when(balanceConverter).convertToEntity(balanceDto);
        doReturn(1).when(balanceDao).updateEntity(balanceEntity);

        balanceService.saveDto(balanceDto);

        verify(accountDao).getEntity(1);
        verify(balanceDao).updateEntity(balanceEntity);
        verify(balanceConverter).convertToEntity(balanceDto);
        verifyNoMoreInteractions(balanceDao, accountDao, balanceConverter);
    }

    @Test
    public void testSaveDto_BalanceNotFound() throws Exception {
        LocalDate day = LocalDate.of(2107, 4, 12);
        BigDecimal amount = new BigDecimal("12.3");
        AccountEntity accountEntity = new AccountEntity(1, 2, "01", "account1");
        BalanceEntity balanceEntity = new BalanceEntity(1, day, amount, true);
        BalanceDto balanceDto = new BalanceDto(1, null, day, amount, false);

        doReturn(accountEntity).when(accountDao).getEntity(1);
        doReturn(balanceEntity).when(balanceConverter).convertToEntity(balanceDto);
        doReturn(0).when(balanceDao).updateEntity(balanceEntity);

        balanceService.saveDto(balanceDto);

        verify(accountDao).getEntity(1);
        verify(balanceDao).updateEntity(balanceEntity);
        verify(balanceDao).insertEntity(balanceEntity);
        verify(balanceConverter).convertToEntity(balanceDto);
        verifyNoMoreInteractions(balanceDao, accountDao, balanceConverter);
    }

}
