package org.kirillgaidai.income.service.impl;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.dao.intf.IOperationDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.converter.OperationConverter;
import org.kirillgaidai.income.service.dto.OperationDto;
import org.kirillgaidai.income.service.exception.IncomeServiceAccountNotFoundException;
import org.kirillgaidai.income.service.exception.IncomeServiceBalanceNotFoundException;
import org.kirillgaidai.income.service.exception.IncomeServiceOperationNotFoundException;
import org.kirillgaidai.income.service.intf.IOperationService;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.util.collections.Sets;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertBalanceEntityEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertOperationDtoEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertOperationDtoListEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class OperationServiceTest {

    final private IOperationDao operationDao = mock(IOperationDao.class);
    final private IBalanceDao balanceDao = mock(IBalanceDao.class);
    final private IAccountDao accountDao = mock(IAccountDao.class);
    final private ICategoryDao categoryDao = mock(ICategoryDao.class);
    final private IGenericConverter<OperationEntity, OperationDto> operationConverter =
            mock(OperationConverter.class);
    final private IOperationService operationService =
            spy(new OperationService(accountDao, operationDao, balanceDao, categoryDao, operationConverter));

    @Test
    public void testSaveDto_AccountNotFound() throws Exception {
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 5, 6);
        BigDecimal amount = new BigDecimal("1.23");

        OperationDto operationDto = new OperationDto(null, accountId, "account1", categoryId, "category1", thisDay,
                amount, "note1");

        try {
            operationService.saveDto(operationDto);
        } catch (IncomeServiceAccountNotFoundException e) {
            assertEquals(String.format("Account with id %d not found", accountId), e.getMessage());
        }

        verify(accountDao).getEntity(accountId);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);
    }

    @Test
    public void testSaveDto_BalanceNotFound() throws Exception {
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 5, 6);
        LocalDate prevDay = thisDay.minusDays(1L);
        BigDecimal amount = new BigDecimal("1.23");
        String accountTitle = "account1";

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", accountTitle);
        OperationDto operationDto = new OperationDto(null, accountId, accountTitle, categoryId, "category1", thisDay,
                amount, "note1");

        doReturn(accountEntity).when(accountDao).getEntity(accountId);

        try {
            operationService.saveDto(operationDto);
        } catch (IncomeServiceBalanceNotFoundException e) {
            assertEquals(String.format("Balance for account \"%s\" on %s not found", accountTitle, prevDay.toString()),
                    e.getMessage());
        }

        verify(accountDao).getEntity(accountId);
        verify(balanceDao).getEntity(accountId, thisDay);
        verify(balanceDao).getEntity(accountId, prevDay);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);
    }

    @Test
    public void testSaveDto_InsertThisDayBalance() throws Exception {
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 5, 6);
        LocalDate prevDay = thisDay.minusDays(1L);
        BigDecimal amount = new BigDecimal("1.25");

        OperationDto operationDto = new OperationDto(null, accountId, "account1", categoryId, "category1", thisDay,
                amount, "note1");
        OperationEntity operationEntity = new OperationEntity(null, accountId, categoryId, thisDay, amount, "note1");
        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "account1");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), true);

        doReturn(accountEntity).when(accountDao).getEntity(accountId);
        doReturn(prevBalanceEntity).when(balanceDao).getEntity(accountId, prevDay);
        doReturn(operationEntity).when(operationConverter).convertToEntity(operationDto);
        doReturn(1).when(balanceDao).insertEntity(any(BalanceEntity.class));
        doReturn(1).when(operationDao).insertEntity(operationEntity);

        operationService.saveDto(operationDto);

        BalanceEntity expected = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), false);
        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).getEntity(accountId);
        verify(balanceDao).getEntity(accountId, thisDay);
        verify(balanceDao).getEntity(accountId, prevDay);
        verify(operationConverter).convertToEntity(operationDto);
        verify(balanceDao).insertEntity(argumentCaptor.capture());
        verify(operationDao).insertEntity(operationEntity);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);

        assertBalanceEntityEquals(expected, argumentCaptor.getValue());
    }

    @Test
    public void testSaveDto_UpdateThisDayBalance() throws Exception {
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 5, 6);
        LocalDate prevDay = thisDay.minusDays(1L);
        BigDecimal amount = new BigDecimal("1.25");

        OperationDto operationDto = new OperationDto(null, accountId, "account1", categoryId, "category1", thisDay,
                amount, "note1");
        OperationEntity operationEntity = new OperationEntity(null, accountId, categoryId, thisDay, amount, "note1");
        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "account1");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), true);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), false);

        doReturn(accountEntity).when(accountDao).getEntity(accountId);
        doReturn(thisBalanceEntity).when(balanceDao).getEntity(accountId, thisDay);
        doReturn(prevBalanceEntity).when(balanceDao).getEntity(accountId, prevDay);
        doReturn(operationEntity).when(operationConverter).convertToEntity(operationDto);
        doReturn(1).when(balanceDao).updateEntity(any(BalanceEntity.class));
        doReturn(1).when(operationDao).insertEntity(operationEntity);

        operationService.saveDto(operationDto);

        BalanceEntity expected = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), false);
        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).getEntity(accountId);
        verify(balanceDao).getEntity(accountId, thisDay);
        verify(balanceDao).getEntity(accountId, prevDay);
        verify(operationConverter).convertToEntity(operationDto);
        verify(balanceDao).updateEntity(argumentCaptor.capture());
        verify(operationDao).insertEntity(operationEntity);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);

        assertBalanceEntityEquals(expected, argumentCaptor.getValue());
    }

    @Test
    public void testSaveDto_InsertPrevDayBalance() throws Exception {
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 5, 6);
        LocalDate prevDay = thisDay.minusDays(1L);
        BigDecimal amount = new BigDecimal("1.25");

        OperationDto operationDto = new OperationDto(null, accountId, "account1", categoryId, "category1", thisDay,
                amount, "note1");
        OperationEntity operationEntity = new OperationEntity(null, accountId, categoryId, thisDay, amount, "note1");
        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "title");
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), true);

        doReturn(accountEntity).when(accountDao).getEntity(accountId);
        doReturn(thisBalanceEntity).when(balanceDao).getEntity(accountId, thisDay);
        doReturn(operationEntity).when(operationConverter).convertToEntity(operationDto);
        doReturn(1).when(balanceDao).insertEntity(any(BalanceEntity.class));
        doReturn(1).when(operationDao).insertEntity(operationEntity);

        operationService.saveDto(operationDto);

        BalanceEntity expected = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), false);
        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).getEntity(accountId);
        verify(balanceDao).getEntity(accountId, thisDay);
        verify(balanceDao).getEntity(accountId, prevDay);
        verify(operationConverter).convertToEntity(operationDto);
        verify(balanceDao).insertEntity(argumentCaptor.capture());
        verify(operationDao).insertEntity(operationEntity);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);

        assertBalanceEntityEquals(expected, argumentCaptor.getValue());
    }

    @Test
    public void testSaveDto_UpdatePrevDayBalance() throws Exception {
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 5, 6);
        LocalDate prevDay = thisDay.minusDays(1L);
        BigDecimal amount = new BigDecimal("1.25");

        OperationDto operationDto = new OperationDto(null, accountId, "account1", categoryId, "category1", thisDay,
                amount, "note1");
        OperationEntity operationEntity = new OperationEntity(null, accountId, categoryId, thisDay, amount, "note1");
        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "account1");
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), true);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), false);

        doReturn(accountEntity).when(accountDao).getEntity(accountId);
        doReturn(thisBalanceEntity).when(balanceDao).getEntity(accountId, thisDay);
        doReturn(prevBalanceEntity).when(balanceDao).getEntity(accountId, prevDay);
        doReturn(operationEntity).when(operationConverter).convertToEntity(operationDto);
        doReturn(1).when(balanceDao).updateEntity(any(BalanceEntity.class));
        doReturn(1).when(operationDao).insertEntity(operationEntity);

        operationService.saveDto(operationDto);

        BalanceEntity expected = new BalanceEntity(accountId, prevDay, new BigDecimal("11.25"), false);
        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).getEntity(accountId);
        verify(balanceDao).getEntity(accountId, thisDay);
        verify(balanceDao).getEntity(accountId, prevDay);
        verify(operationConverter).convertToEntity(operationDto);
        verify(balanceDao).updateEntity(argumentCaptor.capture());
        verify(operationDao).insertEntity(operationEntity);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);

        assertBalanceEntityEquals(expected, argumentCaptor.getValue());
    }

    @Test
    public void testSaveDto_Ok() throws Exception {
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 5, 6);
        LocalDate prevDay = thisDay.minusDays(1L);
        BigDecimal amount = new BigDecimal("1.25");

        OperationDto operationDto = new OperationDto(null, accountId, "account1", categoryId, "category1", thisDay,
                amount, "note");
        OperationEntity operationEntity = new OperationEntity(null, accountId, categoryId, thisDay, amount, "note1");
        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "account1");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("11.25"), true);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), true);

        doReturn(accountEntity).when(accountDao).getEntity(accountId);
        doReturn(prevBalanceEntity).when(balanceDao).getEntity(accountId, prevDay);
        doReturn(thisBalanceEntity).when(balanceDao).getEntity(accountId, thisDay);
        doReturn(operationEntity).when(operationConverter).convertToEntity(operationDto);
        doReturn(1).when(operationDao).insertEntity(operationEntity);

        operationService.saveDto(operationDto);

        verify(accountDao).getEntity(accountId);
        verify(balanceDao).getEntity(accountId, thisDay);
        verify(balanceDao).getEntity(accountId, prevDay);
        verify(operationConverter).convertToEntity(operationDto);
        verify(operationDao).insertEntity(operationEntity);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);
    }

    @Test
    public void testDeleteById_OperationNotFound() throws Exception {
        Integer operationId = 1;

        try {
            operationService.deleteDto(operationId);
        } catch (IncomeServiceOperationNotFoundException e) {
            assertEquals(String.format("Operation with id %d not found", operationId), e.getMessage());
        }

        verify(operationDao).getEntity(operationId);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);
    }

    @Test
    public void testDeleteById_ThisBalanceNotFound() throws Exception {
        Integer id = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 5, 6);
        BigDecimal amount = new BigDecimal("1.25");
        OperationEntity operationEntity = new OperationEntity(id, accountId, categoryId, thisDay, amount, "note1");

        doReturn(operationEntity).when(operationDao).getEntity(id);

        try {
            operationService.deleteDto(id);
        } catch (IncomeServiceBalanceNotFoundException e) {
            assertEquals(String.format("Balance for account with id %d on %s not found", accountId, thisDay),
                    e.getMessage());
        }

        verify(operationDao).getEntity(operationEntity.getId());
        verify(balanceDao).getEntity(operationEntity.getAccountId(), operationEntity.getDay());
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);
    }

    @Test
    public void testDeleteById_PrevBalanceNotFound() throws Exception {
        Integer id = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 5, 6);
        LocalDate prevDay = thisDay.minusDays(1L);
        BigDecimal amount = new BigDecimal("1.25");
        OperationEntity operationEntity = new OperationEntity(id, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), false);

        doReturn(operationEntity).when(operationDao).getEntity(id);
        doReturn(thisBalanceEntity).when(balanceDao).getEntity(accountId, thisDay);

        try {
            operationService.deleteDto(id);
        } catch (IncomeServiceBalanceNotFoundException e) {
            assertEquals(String.format("Balance for account with id %d on %s not found", accountId, prevDay),
                    e.getMessage());
        }

        verify(operationDao).getEntity(id);
        verify(balanceDao).getEntity(accountId, thisDay);
        verify(balanceDao).getEntity(accountId, prevDay);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);
    }

    @Test
    public void testDeleteById_ThisBalanceNotManual() throws Exception {
        Integer id = 1;
        Integer accountId = 2;
        LocalDate thisDay = LocalDate.of(2017, 5, 6);
        LocalDate prevDay = thisDay.minusDays(1L);

        OperationEntity operationEntity = new OperationEntity(id, accountId, 3, thisDay, new BigDecimal("1.25"),
                "note");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("11.25"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), false);

        doReturn(operationEntity).when(operationDao).getEntity(id);
        doReturn(prevBalanceEntity).when(balanceDao).getEntity(accountId, prevDay);
        doReturn(thisBalanceEntity).when(balanceDao).getEntity(accountId, thisDay);
        doReturn(1).when(balanceDao).updateEntity(any(BalanceEntity.class));
        doReturn(1).when(operationDao).deleteEntity(id);

        operationService.deleteDto(id);

        BalanceEntity expected = new BalanceEntity(accountId, thisDay, new BigDecimal("11.25"), false);
        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(operationDao).getEntity(id);
        verify(balanceDao).getEntity(accountId, thisDay);
        verify(balanceDao).getEntity(accountId, prevDay);
        verify(balanceDao).updateEntity(argumentCaptor.capture());
        verify(operationDao).deleteEntity(id);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);

        assertBalanceEntityEquals(expected, argumentCaptor.getValue());
    }

    @Test
    public void testDeleteById_PrevBalanceNotManual() throws Exception {
        Integer id = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 5, 6);
        LocalDate prevDay = thisDay.minusDays(1L);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity = new OperationEntity(id, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("11.25"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), true);

        doReturn(operationEntity).when(operationDao).getEntity(id);
        doReturn(prevBalanceEntity).when(balanceDao).getEntity(accountId, prevDay);
        doReturn(thisBalanceEntity).when(balanceDao).getEntity(accountId, thisDay);
        doReturn(1).when(balanceDao).updateEntity(any(BalanceEntity.class));
        doReturn(1).when(operationDao).deleteEntity(id);

        operationService.deleteDto(id);

        BalanceEntity expected = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), false);
        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(operationDao).getEntity(id);
        verify(balanceDao).getEntity(accountId, thisDay);
        verify(balanceDao).getEntity(accountId, prevDay);
        verify(balanceDao).updateEntity(argumentCaptor.capture());
        verify(operationDao).deleteEntity(id);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);

        assertBalanceEntityEquals(expected, argumentCaptor.getValue());
    }

    @Test
    public void testDeleteDto_Ok() throws Exception {
        Integer id = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 5, 6);
        LocalDate prevDay = thisDay.minusDays(1L);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity = new OperationEntity(id, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("11.25"), true);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), true);

        doReturn(operationEntity).when(operationDao).getEntity(id);
        doReturn(prevBalanceEntity).when(balanceDao).getEntity(accountId, prevDay);
        doReturn(thisBalanceEntity).when(balanceDao).getEntity(accountId, thisDay);
        doReturn(1).when(operationDao).deleteEntity(id);

        operationService.deleteDto(id);

        verify(operationDao).getEntity(id);
        verify(balanceDao).getEntity(accountId, thisDay);
        verify(balanceDao).getEntity(accountId, prevDay);
        verify(operationDao).deleteEntity(id);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);
    }

    @Test
    public void testGetDtoList_SingleDay() throws Exception {
        LocalDate day = LocalDate.of(2017, 4, 12);
        List<OperationEntity> operationEntityList = Arrays.asList(
                new OperationEntity(1, 11, 21, day, new BigDecimal("100"), "note1"),
                new OperationEntity(2, 11, 22, day, new BigDecimal("120"), "note2"),
                new OperationEntity(3, 12, 21, day, new BigDecimal("140"), "note3")
        );
        List<OperationDto> operationDtoList = Arrays.asList(
                new OperationDto(1, 11, "account1", 21, "category1", day, new BigDecimal("100"), "note1"),
                new OperationDto(2, 11, "account1", 22, "category2", day, new BigDecimal("120"), "note2"),
                new OperationDto(3, 12, "account2", 21, "category1", day, new BigDecimal("140"), "note3")
        );
        List<CategoryEntity> categoryEntityList = Arrays.asList(
                new CategoryEntity(21, "01", "category1"),
                new CategoryEntity(22, "02", "category2")
        );
        List<AccountEntity> accountEntityList = Arrays.asList(
                new AccountEntity(11, 31, "01", "account1"),
                new AccountEntity(12, 31, "02", "account2")
        );
        Set<Integer> categoryIds = Sets.newSet(21, 22);
        Set<Integer> accountIds = Sets.newSet(11, 12);

        doReturn(operationEntityList).when(operationDao).getEntityList(accountIds, day);
        doReturn(categoryEntityList).when(categoryDao).getEntityList(categoryIds);
        doReturn(accountEntityList).when(accountDao).getEntityList(accountIds);
        for (int index = 0; index < operationEntityList.size(); index++) {
            doReturn(operationDtoList.get(index)).when(operationConverter).convertToDto(operationEntityList.get(index));
        }

        List<OperationDto> expected = Arrays.asList(
                new OperationDto(1, 11, "account1", 21, "category1", day, new BigDecimal("100"), "note1"),
                new OperationDto(2, 11, "account1", 22, "category2", day, new BigDecimal("120"), "note2"),
                new OperationDto(3, 12, "account2", 21, "category1", day, new BigDecimal("140"), "note3")
        );

        List<OperationDto> actual = operationService.getDtoList(accountIds, day);
        assertOperationDtoListEquals(expected, actual);
    }

    @Test
    public void testGetDto_MultipleAccountIdsNoCategoryId() throws Exception {
        LocalDate day = LocalDate.of(2017, 4, 12);
        OperationDto expected = new OperationDto(null, null, null, null, null, day, BigDecimal.ZERO, null);
        OperationDto actual = operationService.getDto(Sets.newSet(1, 2), null, day);
        assertOperationDtoEquals(expected, actual);
        verifyNoMoreInteractions(operationDao, accountDao, categoryDao, balanceDao, operationConverter);
    }

    @Test
    public void testGetDto_SingleAccountIdNoCategoryId() throws Exception {
        Integer accountId = 1;
        LocalDate day = LocalDate.of(2017, 4, 12);
        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "account1");
        doReturn(accountEntity).when(accountDao).getEntity(accountId);
        OperationDto expected = new OperationDto(null, accountId, "account1", null, null, day, BigDecimal.ZERO, null);
        OperationDto actual = operationService.getDto(Collections.singleton(accountId), null, day);
        assertOperationDtoEquals(expected, actual);
        verify(accountDao).getEntity(accountId);
        verifyNoMoreInteractions(operationDao, accountDao, categoryDao, balanceDao, operationConverter);
    }

    @Test
    public void testGetDto_MultipleAccountIdsCategoryId() throws Exception {
        Integer categoryId = 1;
        LocalDate day = LocalDate.of(2017, 4, 12);
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "01", "category1");
        doReturn(categoryEntity).when(categoryDao).getEntity(categoryId);
        OperationDto expected = new OperationDto(null, null, null, categoryId, "category1", day, BigDecimal.ZERO, null);
        OperationDto actual = operationService.getDto(Sets.newSet(1, 2), categoryId, day);
        assertOperationDtoEquals(expected, actual);
        verify(categoryDao).getEntity(categoryId);
        verifyNoMoreInteractions(operationDao, accountDao, categoryDao, balanceDao, operationConverter);
    }

    @Test
    public void testGetDto_SingleAccountIdCategoryId() throws Exception {
        Integer accountId = 1;
        Integer categoryId = 2;
        LocalDate day = LocalDate.of(2017, 4, 12);
        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "account1");
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "01", "category1");
        doReturn(categoryEntity).when(categoryDao).getEntity(categoryId);
        doReturn(accountEntity).when(accountDao).getEntity(accountId);
        OperationDto expected = new OperationDto(null, accountId, "account1", categoryId, "category1", day,
                BigDecimal.ZERO, null);
        OperationDto actual = operationService.getDto(Collections.singleton(accountId), categoryId, day);
        assertOperationDtoEquals(expected, actual);
        verify(accountDao).getEntity(accountId);
        verify(categoryDao).getEntity(categoryId);
        verifyNoMoreInteractions(operationDao, accountDao, categoryDao, balanceDao, operationConverter);
    }

}
