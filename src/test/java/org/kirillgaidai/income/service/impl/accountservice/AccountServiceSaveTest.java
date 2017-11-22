package org.kirillgaidai.income.service.impl.accountservice;

import org.junit.Ignore;
import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.exception.IncomeServiceAccountNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@Ignore
public class AccountServiceSaveTest extends AccountServiceBaseTest {

    @Test
    public void testSaveNull() throws Exception {
        try {
            service.save(null);
        } catch (IllegalArgumentException e) {
            assertEquals("null", e.getMessage());
        }
        verifyNoMoreInteractions();
    }

    @Test
    public void testInsert() throws Exception {
        AccountDto accountDto = new AccountDto(null, 2, null, null, "01", "account1");
        AccountEntity accountEntity = new AccountEntity(null, 2, "01", "account1");
        CurrencyEntity currencyEntity = new CurrencyEntity(2, "cc1", "currency1", 2);
        AccountDto expected = new AccountDto(1, 2, "cc1", "currency1", "01", "account1");

        doReturn(accountEntity).when(converter).convertToEntity(accountDto);
        doReturn(expected).when(converter).convertToDto(accountEntity);
        doReturn(currencyEntity).when(currencyDao).get(2);
        doReturn(1).when(accountDao).insert(accountEntity);

        AccountDto actual = service.save(accountDto);
        assertEntityEquals(expected, actual);

        verify(converter).convertToEntity(accountDto);
        verify(converter).convertToDto(accountEntity);
        verify(accountDao).insert(accountEntity);
        verifyNoMoreInteractions();
    }

    @Test
    public void testUpdate() throws Exception {
        AccountDto accountDto = new AccountDto(1, 2, null, null, "01", "account1");
        AccountEntity accountEntity = new AccountEntity(1, 2, "01", "account1");
        CurrencyEntity currencyEntity = new CurrencyEntity(2, "cc1", "currency1", 2);
        AccountDto expected =  new AccountDto(1, 2, "cc1", "currency1", "01", "account1");

        doReturn(accountEntity).when(converter).convertToEntity(accountDto);
        doReturn(expected).when(converter).convertToDto(accountEntity);
        doReturn(1).when(accountDao).update(accountEntity);
        doReturn(currencyEntity).when(currencyDao).get(2);

        AccountDto actual = service.save(accountDto);
        assertEntityEquals(expected, actual);

        verify(converter).convertToEntity(accountDto);
        verify(converter).convertToDto(accountEntity);
        verify(accountDao).update(accountEntity);
        verifyNoMoreInteractions();
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        AccountDto accountDto = new AccountDto(1, 2, "cc1", "currency1", "01", "account1");
        AccountEntity accountEntity = new AccountEntity(1, 2, "01", "account1");
        doReturn(accountEntity).when(converter).convertToEntity(accountDto);
        doReturn(0).when(accountDao).update(accountEntity);
        try {
            service.save(accountDto);
        } catch (IncomeServiceAccountNotFoundException e) {
            assertEquals("Account with id 1 not found", e.getMessage());
        }
        verify(converter).convertToEntity(accountDto);
        verify(accountDao).update(accountEntity);
        verifyNoMoreInteractions();
    }

}
