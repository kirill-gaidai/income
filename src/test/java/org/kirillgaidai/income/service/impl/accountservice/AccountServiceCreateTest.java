package org.kirillgaidai.income.service.impl.accountservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticCreateException;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.kirillgaidai.income.utils.TestUtils.getSerialEntityInsertAnswer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class AccountServiceCreateTest extends AccountServiceBaseTest {

    /**
     * Test dto is null
     *
     * @throws Exception exception
     */
    @Test
    public void testNull() throws Exception {
        try {
            service.create(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Dto is null", e.getMessage());
        }

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test currency id is null
     *
     * @throws Exception exception
     */
    @Test
    public void testCurrencyIdNull() throws Exception {
        AccountDto accountDto = new AccountDto(null, null, null, null, "01", "accountTitle");

        try {
            service.create(accountDto);
        } catch (IllegalArgumentException e) {
            assertEquals("Currency id is null", e.getMessage());
        }

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test currency isn't found
     *
     * @throws Exception exception
     */
    @Test
    public void testCurrencyNotFound() throws Exception {
        Integer currencyId = 2;

        AccountDto accountDto = new AccountDto(null, currencyId, null, null, "01", "accountTitle");

        doReturn(null).when(currencyDao).get(currencyId);

        try {
            service.create(accountDto);
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Currency with id %d not found", currencyId), e.getMessage());
        }

        verify(currencyDao).get(currencyId);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test failure
     *
     * @throws Exception exception
     */
    @Test
    public void testFailure() throws Exception {
        Integer currencyId = 2;

        AccountDto dto = new AccountDto(null, currencyId, null, null, "01", "accountTitle");
        CurrencyEntity currencyEntity = new CurrencyEntity(currencyId, "CC1", "currencyTitle", 2);

        doReturn(currencyEntity).when(currencyDao).get(currencyId);
        doReturn(0).when(accountDao).insert(any(AccountEntity.class));

        try {
            service.create(dto);
        } catch (IncomeServiceOptimisticCreateException e) {
            assertEquals("Account create failure", e.getMessage());
        }

        ArgumentCaptor<AccountEntity> argumentCaptor = ArgumentCaptor.forClass(AccountEntity.class);

        verify(currencyDao).get(currencyId);
        verify(accountDao).insert(argumentCaptor.capture());
        verifyNoMoreDaoInteractions();

        AccountEntity expectedAccountEntity = new AccountEntity(null, currencyId, "01", "accountTitle");
        assertEntityEquals(expectedAccountEntity, argumentCaptor.getValue());
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Integer id = 1;
        Integer currencyId = 2;

        AccountDto accountDto = new AccountDto(null, currencyId, null, null, "01", "accountTitle");
        CurrencyEntity currencyEntity = new CurrencyEntity(currencyId, "CC1", "currencyTitle", 2);

        doReturn(currencyEntity).when(currencyDao).get(currencyId);
        doAnswer(getSerialEntityInsertAnswer(id)).when(accountDao).insert(any(AccountEntity.class));

        AccountDto expected = new AccountDto(id, currencyId, "CC1", "currencyTitle", "01", "accountTitle");
        AccountDto actual = service.create(accountDto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<AccountEntity> argumentCaptor = ArgumentCaptor.forClass(AccountEntity.class);

        verify(currencyDao).get(currencyId);
        verify(accountDao).insert(argumentCaptor.capture());

        AccountEntity expectedAccountEntity = new AccountEntity(id, currencyId, "01", "accountTitle");
        assertEntityEquals(expectedAccountEntity, argumentCaptor.getValue());

        verifyNoMoreDaoInteractions();
    }

}
