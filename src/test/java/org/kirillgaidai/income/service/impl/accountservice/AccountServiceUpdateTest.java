package org.kirillgaidai.income.service.impl.accountservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticUpdateException;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class AccountServiceUpdateTest extends AccountServiceBaseTest {

    /**
     * Test null
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

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test currency id is null
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

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test id is null
     *
     * @throws Exception exception
     */
    @Test
    public void testIdIsNull() throws Exception {
        Integer currencyId = 2;

        AccountDto dto = new AccountDto(null, currencyId, null, null, "01", "accountTitle");

        try {
            service.update(dto);
        } catch (IllegalArgumentException e) {
            assertEquals("Id is null", e.getMessage());
        }

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test currency not found
     *
     * @throws Exception exception
     */
    @Test
    public void testCurrencyNotFound() throws Exception {
        Integer id = 1;
        Integer currencyId = 2;

        AccountDto dto = new AccountDto(id, currencyId, null, null, "01", "accountTitle");

        doReturn(null).when(currencyDao).get(currencyId);

        try {
            service.update(dto);
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Currency with id %d not found", currencyId), e.getMessage());
        }

        verify(currencyDao).get(currencyId);

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
        Integer currencyId = 2;

        AccountDto dto = new AccountDto(id, currencyId, null, null, "01", "accountTitle");
        CurrencyEntity currencyEntity = new CurrencyEntity(currencyId, "CC1", "currencyTitle", 2);

        doReturn(currencyEntity).when(currencyDao).get(currencyId);
        doReturn(null).when(accountDao).get(id);

        try {
            service.update(dto);
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Account with id %d not found", id), e.getMessage());
        }

        verify(currencyDao).get(currencyId);
        verify(accountDao).get(id);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test failure
     *
     * @throws Exception exception
     */
    @Test
    public void testFailure() throws Exception {
        Integer id = 1;
        Integer currencyId = 2;

        AccountDto dto = new AccountDto(id, currencyId, null, null, "02", "newTitle");
        AccountEntity oldEntity = new AccountEntity(id, currencyId, "01", "oldTitle");
        CurrencyEntity currencyEntity = new CurrencyEntity(currencyId, "CC1", "currencyTitle", 2);

        doReturn(currencyEntity).when(currencyDao).get(currencyId);
        doReturn(oldEntity).when(accountDao).get(id);
        doReturn(0).when(accountDao).update(any(AccountEntity.class), eq(oldEntity));

        try {
            service.update(dto);
        } catch (IncomeServiceOptimisticUpdateException e) {
            assertEquals(String.format("Account with id %d update failure", id), e.getMessage());
        }

        ArgumentCaptor<AccountEntity> argumentCaptor = ArgumentCaptor.forClass(AccountEntity.class);

        verify(currencyDao).get(currencyId);
        verify(accountDao).get(id);
        verify(accountDao).update(argumentCaptor.capture(), eq(oldEntity));

        AccountEntity expectedEntity = new AccountEntity(id, currencyId, "02", "newTitle");
        AccountEntity actualEntity = argumentCaptor.getValue();
        assertEntityEquals(expectedEntity, actualEntity);

        verifyNoMoreDaoInteractions();
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

        AccountDto dto = new AccountDto(id, currencyId, null, null, "02", "newTitle");
        AccountEntity oldEntity = new AccountEntity(id, currencyId, "01", "oldTitle");
        CurrencyEntity currencyEntity = new CurrencyEntity(currencyId, "CC1", "currencyTitle", 2);

        doReturn(currencyEntity).when(currencyDao).get(currencyId);
        doReturn(oldEntity).when(accountDao).get(id);
        doReturn(1).when(accountDao).update(any(AccountEntity.class), eq(oldEntity));

        AccountDto expected = new AccountDto(id, currencyId, "CC1", "currencyTitle", "02", "newTitle");
        AccountDto actual = service.update(dto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<AccountEntity> argumentCaptor = ArgumentCaptor.forClass(AccountEntity.class);

        verify(currencyDao).get(currencyId);
        verify(accountDao).get(id);
        verify(accountDao).update(argumentCaptor.capture(), eq(oldEntity));

        AccountEntity expectedEntity = new AccountEntity(id, currencyId, "02", "newTitle");
        AccountEntity actualEntity = argumentCaptor.getValue();
        assertEntityEquals(expectedEntity, actualEntity);

        verifyNoMoreDaoInteractions();
    }

}
