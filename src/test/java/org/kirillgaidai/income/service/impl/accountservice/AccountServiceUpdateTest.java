package org.kirillgaidai.income.service.impl.accountservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.exception.IncomeServiceCurrencyNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class AccountServiceUpdateTest extends AccountServiceBaseTest {

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
            service.update(accountDto);
        } catch (IncomeServiceCurrencyNotFoundException e) {
            assertEquals(String.format("Currency with id %d not found", currencyId), e.getMessage());
        }

        verify(currencyDao).get(currencyId);
        verifyNoMoreInteractions(accountDao, currencyDao, converter);
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
        AccountEntity accountEntity = new AccountEntity(null, currencyId, accountSort, accountTitle);
        CurrencyEntity currencyEntity = new CurrencyEntity(currencyId, currencyCode, currencyTitle, 2);
        AccountDto newAccountDto = new AccountDto(accountId, currencyId, null, null, accountSort, accountTitle);

        doReturn(currencyEntity).when(currencyDao).get(currencyId);
        doReturn(accountEntity).when(converter).convertToEntity(accountDto);
        doReturn(newAccountDto).when(converter).convertToDto(accountEntity);
        doReturn(1).when(accountDao).update(accountEntity);

        AccountDto expected =
                new AccountDto(accountId, currencyId, currencyCode, currencyTitle, accountSort, accountTitle);
        AccountDto actual = service.create(accountDto);
        assertEntityEquals(expected, actual);

        verify(currencyDao).get(currencyId);
        verify(converter).convertToEntity(accountDto);
        verify(converter).convertToDto(accountEntity);
        verify(accountDao).insert(accountEntity);
        verifyNoMoreInteractions(accountDao, currencyDao, converter);
    }


}
