package org.kirillgaidai.income.service.impl.accountservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.exception.IncomeServiceCurrencyNotFoundException;
import org.kirillgaidai.income.service.exception.IncomeServiceIntegrityException;
import org.kirillgaidai.income.utils.TestUtils;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.kirillgaidai.income.utils.TestUtils.getSerialEntityInsertAnswer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class AccountServiceUpdateTest extends AccountServiceBaseTest {

    /**
     * Test null argument
     *
     * @throws Exception exception
     */
    @Test
    public void testNull() throws Exception {
        try {
            service.update(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Dto is null", e.getMessage());
        }

        verifyNoMoreInteractions();
    }

    /**
     * Test currency is null
     *
     * @throws Exception exception
     */
    @Test
    public void testCurrencyIsNull() throws Exception {
        AccountDto accountDto = new AccountDto(null, null, null, null, "01", "accountTitle");

        try {
            service.update(accountDto);
        } catch (IllegalArgumentException e) {
            assertEquals("Currency id is null", e.getMessage());
        }

        verifyNoMoreInteractions();
    }

    /**
     * @throws Exception exception
     */
    @Test
    public void testIdIsNull() throws Exception {
        AccountDto accountDto = new AccountDto(null, 2, null, null, "01", "accountTitle");

        try {
            service.update(accountDto);
        } catch (IllegalArgumentException e) {
            assertEquals("Id is null", e.getMessage());
        }

        verifyNoMoreInteractions();
    }

    /**
     * Test currency isn't found
     *
     * @throws Exception exception
     */
    @Test
    public void testCurrencyNotFound() throws Exception {
        Integer accountId = 1;
        Integer currencyId = 2;

        AccountDto accountDto = new AccountDto(accountId, currencyId, null, null, "01", "accountTitle");

        doReturn(null).when(currencyDao).get(currencyId);

        try {
            service.update(accountDto);
        } catch (IncomeServiceCurrencyNotFoundException e) {
            assertEquals(String.format("Currency with id %d not found", currencyId), e.getMessage());
        }

        verify(currencyDao).get(currencyId);
        verifyNoMoreInteractions();
    }

    /**
     * Test account isn't found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        Integer accountId = 1;
        Integer currencyId = 2;
        String accountSort = "01";
        String accountTitle = "accountTitle";
        String currencyCode = "CC1";
        String currencyTitle = "currencyTitle";

        AccountDto accountDto = new AccountDto(accountId, currencyId, null, null, accountSort, accountTitle);
        CurrencyEntity currencyEntity = new CurrencyEntity(currencyId, currencyCode, currencyTitle, 2);

        doReturn(currencyEntity).when(currencyDao).get(currencyId);
        doReturn(0).when(accountDao).update(any(AccountEntity.class));

        try {
            service.update(accountDto);
        } catch (IncomeServiceIntegrityException e) {
            assertEquals("Dto isn't updated", e.getMessage());
        }

        ArgumentCaptor<AccountEntity> argumentCaptor = ArgumentCaptor.forClass(AccountEntity.class);

        verify(currencyDao).get(currencyId);
        verify(accountDao).update(argumentCaptor.capture());
        verifyNoMoreInteractions();

        AccountEntity expectedAccountEntity = new AccountEntity(accountId, currencyId, accountSort, accountTitle);
        assertEntityEquals(expectedAccountEntity, argumentCaptor.getValue());
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Integer accountId = 1;
        Integer currencyId = 2;
        String accountSort = "01";
        String accountTitle = "accountTitle";
        String currencyCode = "CC1";
        String currencyTitle = "currencyTitle";

        AccountDto accountDto = new AccountDto(accountId, currencyId, null, null, accountSort, accountTitle);
        CurrencyEntity currencyEntity = new CurrencyEntity(currencyId, currencyCode, currencyTitle, 2);

        doReturn(currencyEntity).when(currencyDao).get(currencyId);
        doReturn(1).when(accountDao).update(any(AccountEntity.class));

        AccountDto expectedAccountDto =
                new AccountDto(accountId, currencyId, currencyCode, currencyTitle, accountSort, accountTitle);
        AccountDto actualAccountDto = service.update(accountDto);
        assertEntityEquals(expectedAccountDto, actualAccountDto);

        ArgumentCaptor<AccountEntity> argumentCaptor = ArgumentCaptor.forClass(AccountEntity.class);

        verify(currencyDao).get(currencyId);
        verify(accountDao).update(argumentCaptor.capture());
        verifyNoMoreInteractions();

        AccountEntity expectedAccountEntity = new AccountEntity(accountId, currencyId, accountSort, accountTitle);
        assertEntityEquals(expectedAccountEntity, argumentCaptor.getValue());
    }

}
