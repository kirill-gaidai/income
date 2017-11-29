package org.kirillgaidai.income.service.impl.accountservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class AccountServiceGetListTest extends AccountServiceBaseTest {

    /**
     * Test currency not found
     *
     * @throws Exception exception
     */
    @Test
    public void testCurrencyNotFound() throws Exception {
        Set<Integer> currencyIds = Sets.newSet(11, 12);

        List<AccountEntity> entityList = Arrays.asList(
                new AccountEntity(1, 11, "01", "account1"),
                new AccountEntity(2, 12, "02", "account2")
        );
        List<CurrencyEntity> currencyEntityList = Collections.singletonList(
                new CurrencyEntity(11, "cc1", "currency1", 2)
        );

        doReturn(entityList).when(accountDao).getList();
        doReturn(currencyEntityList).when(currencyDao).getList(currencyIds);

        try {
            service.getList();
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Currency with id %d not found", 12), e.getMessage());
        }

        verify(accountDao).getList();
        verify(currencyDao).getList(currencyIds);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test empty
     *
     * @throws Exception exception
     */
    @Test
    public void testEmpty() throws Exception {
        List<AccountEntity> entityList = Collections.emptyList();

        doReturn(entityList).when(accountDao).getList();

        List<AccountDto> expected = Collections.emptyList();
        List<AccountDto> actual = service.getList();
        assertEntityListEquals(expected, actual);

        verify(accountDao).getList();

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Set<Integer> currencyIds = Sets.newSet(11, 12);

        List<AccountEntity> accountEntityList = Arrays.asList(
                new AccountEntity(1, 11, "01", "account1"),
                new AccountEntity(2, 12, "02", "account2"),
                new AccountEntity(3, 11, "03", "account3")
        );
        List<CurrencyEntity> currencyEntityList = Arrays.asList(
                new CurrencyEntity(11, "cc1", "currency1", 0),
                new CurrencyEntity(12, "cc2", "currency2", 2)
        );

        doReturn(accountEntityList).when(accountDao).getList();
        doReturn(currencyEntityList).when(currencyDao).getList(currencyIds);

        List<AccountDto> expected = Arrays.asList(
                new AccountDto(1, 11, "cc1", "currency1", "01", "account1"),
                new AccountDto(2, 12, "cc2", "currency2", "02", "account2"),
                new AccountDto(3, 11, "cc1", "currency1", "03", "account3")
        );
        List<AccountDto> actual = service.getList();
        assertEntityListEquals(expected, actual);

        verify(accountDao).getList();
        verify(currencyDao).getList(currencyIds);

        verifyNoMoreDaoInteractions();
    }

}
