package org.kirillgaidai.income.service.impl.accountservice;

import org.junit.Ignore;
import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.impl.AccountDao;
import org.kirillgaidai.income.dao.impl.CurrencyDao;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.service.converter.AccountConverter;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.exception.IncomeServiceAccountNotFoundException;
import org.kirillgaidai.income.service.exception.IncomeServiceCurrencyNotFoundException;
import org.kirillgaidai.income.service.impl.AccountService;
import org.kirillgaidai.income.service.intf.IAccountService;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertAccountDtoEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertAccountDtoListEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class AccountServiceTest extends AccountServiceBaseTest {

    @Test
    public void testGetDtoList_AllCurrencyEntityNotFound() throws Exception {
        List<AccountEntity> accountEntityList = Arrays.asList(
                new AccountEntity(1, 11, "01", "account1"),
                new AccountEntity(2, 12, "02", "account2")
        );
        List<CurrencyEntity> currencyEntityList = Collections.singletonList(
                new CurrencyEntity(11, "cc1", "currency1", 2)
        );
        List<AccountDto> accountDtoList = Arrays.asList(
                new AccountDto(1, 11, null, null, "01", "account1"),
                new AccountDto(2, 12, null, null, "02", "account2")
        );
        Set<Integer> currencyIds = Sets.newSet(11, 12);

        doReturn(accountEntityList).when(accountDao).getList();
        doReturn(currencyEntityList).when(currencyDao).getList(currencyIds);
        for (int index = 0; index < accountDtoList.size(); index++) {
            doReturn(accountDtoList.get(index)).when(converter).convertToDto(accountEntityList.get(index));
        }

        try {
            service.getList();
        } catch (IncomeServiceCurrencyNotFoundException e) {
            assertEquals("Currency with id 12 not found", e.getMessage());
        }

        verify(accountDao).getList();
        verify(currencyDao).getList(currencyIds);
        for (AccountEntity anAccountEntityList : accountEntityList) {
            verify(converter).convertToDto(anAccountEntityList);
        }
        verifyNoMoreInteractions(accountDao, currencyDao, converter);
    }

    @Test
    public void testGetDtoList_AllOk() throws Exception {
        List<AccountEntity> accountEntityList = Arrays.asList(
                new AccountEntity(1, 11, "01", "account1"),
                new AccountEntity(2, 12, "02", "account2"),
                new AccountEntity(3, 11, "03", "account3")
        );
        List<CurrencyEntity> currencyEntityList = Arrays.asList(
                new CurrencyEntity(11, "cc1", "currency1", 0),
                new CurrencyEntity(12, "cc2", "currency2", 2)
        );
        List<AccountDto> accountDtoList = Arrays.asList(
                new AccountDto(1, 11, null, null, "01", "account1"),
                new AccountDto(2, 12, null, null, "02", "account2"),
                new AccountDto(3, 11, null, null, "03", "account3")
        );
        Set<Integer> currencyIds = Sets.newSet(11, 12);
        List<AccountDto> expected = Arrays.asList(
                new AccountDto(1, 11, "cc1", "currency1", "01", "account1"),
                new AccountDto(2, 12, "cc2", "currency2", "02", "account2"),
                new AccountDto(3, 11, "cc1", "currency1", "03", "account3")
        );

        doReturn(accountEntityList).when(accountDao).getList();
        doReturn(currencyEntityList).when(currencyDao).getList(currencyIds);
        for (int index = 0; index < accountDtoList.size(); index++) {
            doReturn(accountDtoList.get(index)).when(converter).convertToDto(accountEntityList.get(index));
        }

        List<AccountDto> actual = service.getList();
        assertAccountDtoListEquals(expected, actual);

        verify(accountDao).getList();
        verify(currencyDao).getList(currencyIds);
        for (AccountEntity anAccountEntityList : accountEntityList) {
            verify(converter).convertToDto(anAccountEntityList);
        }
        verifyNoMoreInteractions(accountDao, currencyDao, converter);
    }

    @Test
    public void testGetDtoList_AllOkEmpty() throws Exception {
        doReturn(Collections.emptyList()).when(accountDao).getList();
        List<AccountDto> expected = Collections.emptyList();
        List<AccountDto> actual = service.getList();
        assertAccountDtoListEquals(expected, actual);
        verify(accountDao).getList();
        verifyNoMoreInteractions(accountDao, currencyDao, converter);
    }

    @Test
    public void testGetDtoList_IdsCurrencyEntityNotFound() throws Exception {
        Set<Integer> accountIds = Sets.newSet(1, 2);
        List<AccountEntity> accountEntityList = Arrays.asList(
                new AccountEntity(1, 11, "01", "account1"),
                new AccountEntity(2, 12, "02", "account2")
        );
        List<CurrencyEntity> currencyEntityList = Collections.singletonList(
                new CurrencyEntity(11, "cc1", "currency1", 2)
        );
        List<AccountDto> accountDtoList = Arrays.asList(
                new AccountDto(1, 11, null, null, "01", "Account 1"),
                new AccountDto(2, 12, null, null, "02", "Account 2")
        );
        Set<Integer> currencyIds = Sets.newSet(11, 12);

        doReturn(accountEntityList).when(accountDao).getList(accountIds);
        doReturn(currencyEntityList).when(currencyDao).getList(currencyIds);
        for (int index = 0; index < accountEntityList.size(); index++) {
            doReturn(accountDtoList.get(index)).when(converter).convertToDto(accountEntityList.get(index));
        }

        try {
            service.getList(accountIds);
        } catch (IncomeServiceCurrencyNotFoundException e) {
            assertEquals("Currency with id 12 not found", e.getMessage());
        }

        verify(accountDao).getList(accountIds);
        verify(currencyDao).getList(currencyIds);
        for (AccountEntity anAccountEntityList : accountEntityList) {
            verify(converter).convertToDto(anAccountEntityList);
        }
        verifyNoMoreInteractions(accountDao, currencyDao, converter);
    }

    @Test
    public void testGetDtoList_IdsOk() throws Exception {
        Set<Integer> accountIds = Sets.newSet(1, 2);
        List<AccountEntity> accountEntityList = Arrays.asList(
                new AccountEntity(1, 11, "01", "account1"),
                new AccountEntity(2, 12, "02", "account2")
        );
        List<CurrencyEntity> currencyEntityList = Arrays.asList(
                new CurrencyEntity(11, "cc1", "currency1", 0),
                new CurrencyEntity(12, "cc2", "currency2", 2)
        );
        List<AccountDto> accountDtoList = Arrays.asList(
                new AccountDto(1, 11, null, null, "01", "account1"),
                new AccountDto(2, 12, null, null, "02", "account2")
        );
        Set<Integer> currencyIds = Sets.newSet(11, 12);
        List<AccountDto> expected = Arrays.asList(
                new AccountDto(1, 11, "cc1", "currency1", "01", "account1"),
                new AccountDto(2, 12, "cc2", "currency2", "02", "account2")
        );

        doReturn(accountEntityList).when(accountDao).getList(accountIds);
        doReturn(currencyEntityList).when(currencyDao).getList(currencyIds);
        for (int index = 0; index < accountDtoList.size(); index++) {
            doReturn(accountDtoList.get(index)).when(converter).convertToDto(accountEntityList.get(index));
        }

        List<AccountDto> actual = service.getList(accountIds);
        assertAccountDtoListEquals(expected, actual);

        verify(accountDao).getList(accountIds);
        verify(currencyDao).getList(currencyIds);
        for (AccountEntity anAccountEntityList : accountEntityList) {
            verify(converter).convertToDto(anAccountEntityList);
        }
        verifyNoMoreInteractions(accountDao, currencyDao, converter);
    }

    @Test
    public void testGetDtoList_IdsEmpty() throws Exception {
        Set<Integer> accountIds = Sets.newSet(1, 2);
        doReturn(Collections.emptyList()).when(accountDao).getList(accountIds);
        List<AccountDto> expected = Collections.emptyList();
        List<AccountDto> actual = service.getList();
        assertAccountDtoListEquals(expected, actual);
        verify(accountDao).getList();
        verifyNoMoreInteractions(accountDao, currencyDao, converter);
    }

    @Test
    public void testGetDto_Null() throws Exception {
        try {
            service.get(null);
        } catch (IllegalArgumentException e) {
            assertEquals("null", e.getMessage());
        }
        verifyNoMoreInteractions(accountDao, currencyDao, converter);
    }

    @Test
    public void testGetDto_AccountEntityNotFound() throws Exception {
        try {
            service.get(1);
        } catch (IncomeServiceAccountNotFoundException e) {
            assertEquals("Account with id 1 not found", e.getMessage());
        }
        verify(accountDao).get(1);
        verifyNoMoreInteractions(accountDao, currencyDao, converter);
    }

    @Test
    public void testGetDto_CurrencyIdIsNull() throws Exception {
        AccountEntity accountEntity = new AccountEntity(1, null, "01", "account1");
        AccountDto accountDto = new AccountDto(1, null, null, null, "01", "account1");

        doReturn(accountEntity).when(accountDao).get(1);
        doReturn(accountDto).when(converter).convertToDto(accountEntity);

        try {
            service.get(1);
        } catch (IllegalArgumentException e) {
            assertEquals("null", e.getMessage());
        }

        verify(accountDao).get(1);
        verify(converter).convertToDto(accountEntity);
        verifyNoMoreInteractions(accountDao, currencyDao, converter);
    }

    @Test
    public void testGetDtoById_CurrencyEntityNotFound() throws Exception {
        AccountEntity accountEntity = new AccountEntity(1, 11, "01", "account1");
        AccountDto accountDto = new AccountDto(1, 11, null, null, "01", "account1");

        doReturn(accountEntity).when(accountDao).get(1);
        doReturn(accountDto).when(converter).convertToDto(accountEntity);
        try {
            service.get(1);
        } catch (IncomeServiceCurrencyNotFoundException e) {
            assertEquals("Currency with id 11 not found", e.getMessage());
        }
        verify(accountDao).get(1);
        verify(converter).convertToDto(accountEntity);
        verify(currencyDao).get(11);
        verifyNoMoreInteractions(accountDao, currencyDao, converter);
    }

    @Test
    public void testGetDtoById() throws Exception {
        AccountEntity accountEntity = new AccountEntity(1, 11, "01", "account1");
        CurrencyEntity currencyEntity = new CurrencyEntity(11, "cc1", "currency1", 2);
        AccountDto accountDto = new AccountDto(1, 11, null, null, "01", "account1");
        doReturn(accountEntity).when(accountDao).get(1);
        doReturn(currencyEntity).when(currencyDao).get(11);
        doReturn(accountDto).when(converter).convertToDto(accountEntity);
        AccountDto expected = new AccountDto(1, 11, "cc1", "currency1", "01", "account1");
        AccountDto actual = service.get(1);
        assertAccountDtoEquals(expected, actual);
        verify(accountDao).get(1);
        verify(currencyDao).get(11);
        verify(converter).convertToDto(accountEntity);
        verifyNoMoreInteractions(accountDao, currencyDao, converter);
    }

    @Test
    public void testDeleteDto_Null() throws Exception {
        try {
            service.delete(null);
        } catch (IllegalArgumentException e) {
            assertEquals("null", e.getMessage());
        }
        verifyNoMoreInteractions(accountDao, currencyDao, converter);
    }

    @Test
    public void testDeleteDto_NotFound() throws Exception {
        doReturn(0).when(accountDao).delete(1);
        try {
            service.delete(1);
        } catch (IncomeServiceAccountNotFoundException e) {
            assertEquals("Account with id 1 not found", e.getMessage());
        }
        verify(accountDao).delete(1);
        verifyNoMoreInteractions(accountDao, currencyDao, converter);
    }

    @Test
    public void testDeleteDto_Ok() throws Exception {
        doReturn(1).when(accountDao).delete(1);
        service.delete(1);
        verify(accountDao).delete(1);
        verifyNoMoreInteractions(accountDao, currencyDao, converter);
    }

}
