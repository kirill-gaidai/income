package org.kirillgaidai.income.service.impl.accountservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class AccountServiceGetTest extends AccountServiceBaseTest {

    /**
     * Test id is null
     *
     * @throws Exception exception
     */
    @Test
    public void testNull() throws Exception {
        try {
            service.get(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Id is null", e.getMessage());
        }

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        Integer id = 1;

        doReturn(null).when(accountDao).get(id);

        try {
            service.get(id);
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Account with id %d not found", id), e.getMessage());
        }

        verify(accountDao).get(id);

        verifyNoMoreDaoInteractions();
    }

    @Test
    public void testCurrencyIdIsNull() throws Exception {
        Integer id = 1;

        AccountEntity accountEntity = new AccountEntity(id, null, "01", "account1");

        doReturn(accountEntity).when(accountDao).get(id);

        try {
            service.get(id);
        } catch (IllegalStateException e) {
            assertEquals("Currency id is null", e.getMessage());
        }

        verify(accountDao).get(id);

        verifyNoMoreDaoInteractions();
    }

    @Test
    public void testCurrencyNotFound() throws Exception {
        Integer id = 1;
        Integer currencyId = 11;

        AccountEntity accountEntity = new AccountEntity(id, currencyId, "01", "account1");

        doReturn(accountEntity).when(accountDao).get(id);

        try {
            service.get(1);
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Currency with id %d not found", currencyId), e.getMessage());
        }

        verify(accountDao).get(id);
        verify(currencyDao).get(currencyId);

        verifyNoMoreDaoInteractions();
    }

    @Test
    public void testGetDtoById() throws Exception {
        Integer id = 1;
        Integer currencyId = 11;

        AccountEntity accountEntity = new AccountEntity(id, currencyId, "01", "account1");
        CurrencyEntity currencyEntity = new CurrencyEntity(currencyId, "cc1", "currency1", 2);

        doReturn(accountEntity).when(accountDao).get(id);
        doReturn(currencyEntity).when(currencyDao).get(currencyId);

        AccountDto expected = new AccountDto(id, currencyId, "cc1", "currency1", "01", "account1");
        AccountDto actual = service.get(id);
        assertEntityEquals(expected, actual);

        verify(accountDao).get(id);
        verify(currencyDao).get(currencyId);

        verifyNoMoreDaoInteractions();
    }

}
