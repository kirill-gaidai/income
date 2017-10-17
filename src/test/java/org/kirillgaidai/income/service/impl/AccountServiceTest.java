package org.kirillgaidai.income.service.impl;

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
import org.kirillgaidai.income.service.intf.IAccountService;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertAccountDtoEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertAccountDtoListEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class AccountServiceTest {

    final private IAccountDao accountDao = mock(AccountDao.class);
    final private ICurrencyDao currencyDao = mock(CurrencyDao.class);
    final private IGenericConverter<AccountEntity, AccountDto> accountConverter = mock(AccountConverter.class);
    final private IAccountService accountService = new AccountService(accountDao, currencyDao, accountConverter);

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

        doReturn(accountEntityList).when(accountDao).getEntityList();
        doReturn(currencyEntityList).when(currencyDao).getEntityList(currencyIds);
        for (int index = 0; index < accountDtoList.size(); index++) {
            doReturn(accountDtoList.get(index)).when(accountConverter).convertToDto(accountEntityList.get(index));
        }

        try {
            accountService.getList();
        } catch (IncomeServiceCurrencyNotFoundException e) {
            assertEquals("Currency with id 12 not found", e.getMessage());
        }

        verify(accountDao).getEntityList();
        verify(currencyDao).getEntityList(currencyIds);
        for (AccountEntity anAccountEntityList : accountEntityList) {
            verify(accountConverter).convertToDto(anAccountEntityList);
        }
        verifyNoMoreInteractions(accountDao, currencyDao, accountConverter);
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

        doReturn(accountEntityList).when(accountDao).getEntityList();
        doReturn(currencyEntityList).when(currencyDao).getEntityList(currencyIds);
        for (int index = 0; index < accountDtoList.size(); index++) {
            doReturn(accountDtoList.get(index)).when(accountConverter).convertToDto(accountEntityList.get(index));
        }

        List<AccountDto> actual = accountService.getList();
        assertAccountDtoListEquals(expected, actual);

        verify(accountDao).getEntityList();
        verify(currencyDao).getEntityList(currencyIds);
        for (AccountEntity anAccountEntityList : accountEntityList) {
            verify(accountConverter).convertToDto(anAccountEntityList);
        }
        verifyNoMoreInteractions(accountDao, currencyDao, accountConverter);
    }

    @Test
    public void testGetDtoList_AllOkEmpty() throws Exception {
        doReturn(Collections.emptyList()).when(accountDao).getEntityList();
        List<AccountDto> expected = Collections.emptyList();
        List<AccountDto> actual = accountService.getList();
        assertAccountDtoListEquals(expected, actual);
        verify(accountDao).getEntityList();
        verifyNoMoreInteractions(accountDao, currencyDao, accountConverter);
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

        doReturn(accountEntityList).when(accountDao).getEntityList(accountIds);
        doReturn(currencyEntityList).when(currencyDao).getEntityList(currencyIds);
        for (int index = 0; index < accountEntityList.size(); index++) {
            doReturn(accountDtoList.get(index)).when(accountConverter).convertToDto(accountEntityList.get(index));
        }

        try {
            accountService.getList(accountIds);
        } catch (IncomeServiceCurrencyNotFoundException e) {
            assertEquals("Currency with id 12 not found", e.getMessage());
        }

        verify(accountDao).getEntityList(accountIds);
        verify(currencyDao).getEntityList(currencyIds);
        for (AccountEntity anAccountEntityList : accountEntityList) {
            verify(accountConverter).convertToDto(anAccountEntityList);
        }
        verifyNoMoreInteractions(accountDao, currencyDao, accountConverter);
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

        doReturn(accountEntityList).when(accountDao).getEntityList(accountIds);
        doReturn(currencyEntityList).when(currencyDao).getEntityList(currencyIds);
        for (int index = 0; index < accountDtoList.size(); index++) {
            doReturn(accountDtoList.get(index)).when(accountConverter).convertToDto(accountEntityList.get(index));
        }

        List<AccountDto> actual = accountService.getList(accountIds);
        assertAccountDtoListEquals(expected, actual);

        verify(accountDao).getEntityList(accountIds);
        verify(currencyDao).getEntityList(currencyIds);
        for (AccountEntity anAccountEntityList : accountEntityList) {
            verify(accountConverter).convertToDto(anAccountEntityList);
        }
        verifyNoMoreInteractions(accountDao, currencyDao, accountConverter);
    }

    @Test
    public void testGetDtoList_IdsEmpty() throws Exception {
        Set<Integer> accountIds = Sets.newSet(1, 2);
        doReturn(Collections.emptyList()).when(accountDao).getEntityList(accountIds);
        List<AccountDto> expected = Collections.emptyList();
        List<AccountDto> actual = accountService.getList();
        assertAccountDtoListEquals(expected, actual);
        verify(accountDao).getEntityList();
        verifyNoMoreInteractions(accountDao, currencyDao, accountConverter);
    }

    @Test
    public void testGetDto_Null() throws Exception {
        try {
            accountService.get(null);
        } catch (IncomeServiceAccountNotFoundException e) {
            assertEquals("Account not found", e.getMessage());
        }
        verifyNoMoreInteractions(accountDao, currencyDao, accountConverter);
    }

    @Test
    public void testGetDto_AccountEntityNotFound() throws Exception {
        try {
            accountService.get(1);
        } catch (IncomeServiceAccountNotFoundException e) {
            assertEquals("Account with id 1 not found", e.getMessage());
        }
        verify(accountDao).getEntity(1);
        verifyNoMoreInteractions(accountDao, currencyDao, accountConverter);
    }

    @Test
    public void testGetDto_CurrencyIdIsNull() throws Exception {
        AccountEntity accountEntity = new AccountEntity(1, null, "01", "account1");
        doReturn(accountEntity).when(accountDao).getEntity(1);
        try {
            accountService.get(1);
        } catch (IncomeServiceCurrencyNotFoundException e) {
            assertEquals("Currency not found", e.getMessage());
        }
        verify(accountDao).getEntity(1);
        verifyNoMoreInteractions(accountDao, currencyDao, accountConverter);
    }

    @Test
    public void testGetDtoById_CurrencyEntityNotFound() throws Exception {
        AccountEntity accountEntity = new AccountEntity(1, 11, "01", "account1");
        doReturn(accountEntity).when(accountDao).getEntity(1);
        try {
            accountService.get(1);
        } catch (IncomeServiceCurrencyNotFoundException e) {
            assertEquals("Currency with id 11 not found", e.getMessage());
        }
        verify(accountDao).getEntity(1);
        verify(currencyDao).getEntity(11);
        verifyNoMoreInteractions(accountDao, currencyDao, accountConverter);
    }

    @Test
    public void testGetDtoById() throws Exception {
        AccountEntity accountEntity = new AccountEntity(1, 11, "01", "account1");
        CurrencyEntity currencyEntity = new CurrencyEntity(11, "cc1", "currency1", 2);
        AccountDto accountDto = new AccountDto(1, 11, null, null, "01", "account1");
        doReturn(accountEntity).when(accountDao).getEntity(1);
        doReturn(currencyEntity).when(currencyDao).getEntity(11);
        doReturn(accountDto).when(accountConverter).convertToDto(accountEntity);
        AccountDto expected = new AccountDto(1, 11, "cc1", "currency1", "01", "account1");
        AccountDto actual = accountService.get(1);
        assertAccountDtoEquals(expected, actual);
        verify(accountDao).getEntity(1);
        verify(currencyDao).getEntity(11);
        verify(accountConverter).convertToDto(accountEntity);
        verifyNoMoreInteractions(accountDao, currencyDao, accountConverter);
    }

    @Test
    public void testSaveDto_Null() throws Exception {
        try {
            accountService.save(null);
        } catch (IncomeServiceAccountNotFoundException e) {
            assertEquals("Account not found", e.getMessage());
        }
        verifyNoMoreInteractions(accountDao, currencyDao, accountConverter);
    }

    @Test
    public void testSaveDto_Insert() throws Exception {
        AccountDto accountDto = new AccountDto(null, 2, "cc1", "currency1", "01", "account1");
        AccountEntity accountEntity = new AccountEntity(null, 2, "01", "account1");
        doReturn(accountEntity).when(accountConverter).convertToEntity(accountDto);
        doReturn(1).when(accountDao).insertEntity(accountEntity);
        accountService.save(accountDto);
        verify(accountConverter).convertToEntity(accountDto);
        verify(accountDao).insertEntity(accountEntity);
        verifyNoMoreInteractions(accountDao, currencyDao, accountConverter);
    }

    @Test
    public void testSaveDto_Update() throws Exception {
        AccountDto accountDto = new AccountDto(1, 2, "cc1", "currency1", "01", "account1");
        AccountEntity accountEntity = new AccountEntity(1, 2, "01", "account1");
        doReturn(accountEntity).when(accountConverter).convertToEntity(accountDto);
        doReturn(1).when(accountDao).updateEntity(accountEntity);
        accountService.save(accountDto);
        verify(accountConverter).convertToEntity(accountDto);
        verify(accountDao).updateEntity(accountEntity);
        verifyNoMoreInteractions(accountDao, currencyDao, accountConverter);
    }

    @Test
    public void testSaveDto_UpdateNotFound() throws Exception {
        AccountDto accountDto = new AccountDto(1, 2, "cc1", "currency1", "01", "account1");
        AccountEntity accountEntity = new AccountEntity(1, 2, "01", "account1");
        doReturn(accountEntity).when(accountConverter).convertToEntity(accountDto);
        doReturn(0).when(accountDao).updateEntity(accountEntity);
        try {
            accountService.save(accountDto);
        } catch (IncomeServiceAccountNotFoundException e) {
            assertEquals("Account with id 1 not found", e.getMessage());
        }
        verify(accountConverter).convertToEntity(accountDto);
        verify(accountDao).updateEntity(accountEntity);
        verifyNoMoreInteractions(accountDao, currencyDao, accountConverter);
    }

    @Test
    public void testDeleteDto_Null() throws Exception {
        try {
            accountService.delete(null);
        } catch (IncomeServiceAccountNotFoundException e) {
            assertEquals("Account not found", e.getMessage());
        }
        verifyNoMoreInteractions(accountDao, currencyDao, accountConverter);
    }

    @Test
    public void testDeleteDto_NotFound() throws Exception {
        doReturn(0).when(accountDao).deleteEntity(1);
        try {
            accountService.delete(1);
        } catch (IncomeServiceAccountNotFoundException e) {
            assertEquals("Account with id 1 not found", e.getMessage());
        }
        verify(accountDao).deleteEntity(1);
        verifyNoMoreInteractions(accountDao, currencyDao, accountConverter);
    }

    @Test
    public void testDeleteDto_Ok() throws Exception {
        doReturn(1).when(accountDao).deleteEntity(1);
        accountService.delete(1);
        verify(accountDao).deleteEntity(1);
        verifyNoMoreInteractions(accountDao, currencyDao, accountConverter);
    }

}
