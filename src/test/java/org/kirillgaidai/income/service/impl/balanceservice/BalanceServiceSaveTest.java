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
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class BalanceServiceSaveTest {

    final private IBalanceDao balanceDao = mock(BalanceDao.class);
    final private IAccountDao accountDao = mock(AccountDao.class);
    final private IGenericConverter<BalanceEntity, BalanceDto> converter = mock(BalanceConverter.class);
    final private IBalanceService service = new BalanceService(balanceDao, accountDao, converter);

    @Test
    public void testNull() throws Exception {
        try {
            service.save(null);
        } catch (IllegalArgumentException e) {
            assertEquals("null", e.getMessage());
        }
        verifyNoMoreInteractions(balanceDao, accountDao, converter);
    }

    @Test
    public void testAccountNull() throws Exception {
        LocalDate day = LocalDate.of(2107, 4, 12);
        BigDecimal amount = new BigDecimal("12.3");
        BalanceDto dto = new BalanceDto(null, null, day, amount, false);
        try {
            service.save(dto);
        } catch (IllegalArgumentException e) {
            assertEquals("null", e.getMessage());
        }
        verifyNoMoreInteractions(balanceDao, accountDao, converter);
    }

    @Test
    public void testAccountNotFound() throws Exception {
        LocalDate day = LocalDate.of(2107, 4, 12);
        BigDecimal amount = new BigDecimal("12.3");
        BalanceDto dto = new BalanceDto(1, null, day, amount, false);
        try {
            service.save(dto);
        } catch (IncomeServiceAccountNotFoundException e) {
            assertEquals("Account with id 1 not found", e.getMessage());
        }
        verify(accountDao).get(1);
        verifyNoMoreInteractions(balanceDao, accountDao, converter);
    }

    @Test
    public void testBalanceFound() throws Exception {
        LocalDate day = LocalDate.of(2107, 4, 12);
        BigDecimal amount = new BigDecimal("12.3");
        AccountEntity accountEntity = new AccountEntity(1, 2, "01", "account1");
        BalanceEntity balanceEntity = new BalanceEntity(1, day, amount, true);
        BalanceDto balanceDto = new BalanceDto(1, null, day, amount, false);

        doReturn(accountEntity).when(accountDao).get(1);
        doReturn(balanceEntity).when(converter).convertToEntity(balanceDto);
        doReturn(1).when(balanceDao).update(balanceEntity);

        service.save(balanceDto);

        verify(accountDao).get(1);
        verify(balanceDao).update(balanceEntity);
        verify(converter).convertToEntity(balanceDto);
        verifyNoMoreInteractions(balanceDao, accountDao, converter);
    }

    @Test
    public void testBalanceNotFound() throws Exception {
        LocalDate day = LocalDate.of(2107, 4, 12);
        BigDecimal amount = new BigDecimal("12.3");
        AccountEntity accountEntity = new AccountEntity(1, 2, "01", "account1");
        BalanceEntity balanceEntity = new BalanceEntity(1, day, amount, true);
        BalanceDto dto = new BalanceDto(1, null, day, amount, true);
        BalanceDto expected = new BalanceDto(1, "account1", day, amount, true);

        doReturn(balanceEntity).when(converter).convertToEntity(dto);
        doReturn(accountEntity).when(accountDao).get(1);
        doReturn(0).when(balanceDao).update(balanceEntity);
        doReturn(1).when(balanceDao).insert(balanceEntity);

        BalanceDto actual = service.save(dto);
        assertEntityEquals(expected, actual);

        verify(accountDao).get(1);
        verify(balanceDao).update(balanceEntity);
        verify(balanceDao).insert(balanceEntity);
        verify(converter).convertToEntity(dto);
        verifyNoMoreInteractions(balanceDao, accountDao, converter);
    }

}
