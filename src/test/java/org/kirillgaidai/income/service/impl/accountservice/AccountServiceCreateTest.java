package org.kirillgaidai.income.service.impl.accountservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.exception.IncomeServiceCurrencyNotFoundException;
import org.kirillgaidai.income.service.exception.IncomeServiceIntegrityException;
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
     * Test null argument
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

        verifyNoMoreInteractions();
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

        verifyNoMoreInteractions();
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
        } catch (IncomeServiceCurrencyNotFoundException e) {
            assertEquals(String.format("Currency with id %d not found", currencyId), e.getMessage());
        }

        verify(currencyDao).get(currencyId);
        verifyNoMoreInteractions();
    }

    /**
     * Test entity not created
     *
     * @throws Exception exception
     */
    @Test
    public void testNotCreated() throws Exception {
        Integer currencyId = 2;
        String accountSort = "01";
        String accountTitle = "accountTitle";
        String currencyCode = "CC1";
        String currencyTitle = "currencyTitle";

        AccountDto accountDto = new AccountDto(null, currencyId, null, null, accountSort, accountTitle);
        CurrencyEntity currencyEntity = new CurrencyEntity(currencyId, currencyCode, currencyTitle, 2);

        doReturn(currencyEntity).when(currencyDao).get(currencyId);
        doReturn(0).when(accountDao).insert(any(AccountEntity.class));

        try {
            service.create(accountDto);
        } catch (IncomeServiceIntegrityException e) {
            assertEquals("Data integrity exception", e.getMessage());
        }

        ArgumentCaptor<AccountEntity> argumentCaptor = ArgumentCaptor.forClass(AccountEntity.class);

        verify(currencyDao).get(currencyId);
        verify(accountDao).insert(argumentCaptor.capture());
        verifyNoMoreInteractions();

        AccountEntity expectedAccountEntity = new AccountEntity(null, currencyId, accountSort, accountTitle);
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

        AccountDto accountDto = new AccountDto(null, currencyId, null, null, accountSort, accountTitle);
        CurrencyEntity currencyEntity = new CurrencyEntity(currencyId, currencyCode, currencyTitle, 2);

        doReturn(currencyEntity).when(currencyDao).get(currencyId);
        doAnswer(getSerialEntityInsertAnswer(accountId)).when(accountDao).insert(any(AccountEntity.class));

        AccountDto expectedAccountDto =
                new AccountDto(accountId, currencyId, currencyCode, currencyTitle, accountSort, accountTitle);
        AccountDto actual = service.create(accountDto);
        assertEntityEquals(expectedAccountDto, actual);

        ArgumentCaptor<AccountEntity> argumentCaptor = ArgumentCaptor.forClass(AccountEntity.class);

        verify(currencyDao).get(currencyId);
        verify(accountDao).insert(argumentCaptor.capture());
        verifyNoMoreInteractions();

        AccountEntity expectedAccountEntity = new AccountEntity(accountId, currencyId, accountSort, accountTitle);
        assertEntityEquals(expectedAccountEntity, argumentCaptor.getValue());
    }

}
