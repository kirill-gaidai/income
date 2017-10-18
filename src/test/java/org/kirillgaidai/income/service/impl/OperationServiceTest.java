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
            operationService.save(operationDto);
        } catch (IncomeServiceAccountNotFoundException e) {
            assertEquals(String.format("Account with id %d not found", accountId), e.getMessage());
        }

        verify(accountDao).get(accountId);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);
    }

    /**
     * Error if no balances for this account
     *
     * @throws Exception - exception
     */
    @Test
    public void testSaveDto_BothNotExist() throws Exception {
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "account1");
        OperationDto operationDto =
                new OperationDto(null, accountId, "account1", categoryId, "category1", thisDay, amount, "note1");
        OperationEntity operationEntity = new OperationEntity(null, accountId, categoryId, thisDay, amount, "note1");

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(operationEntity).when(operationConverter).convertToEntity(operationDto);
        doReturn(null).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(null).when(balanceDao).get(accountId, thisDay);

        try {
            operationService.save(operationDto);
        } catch (IncomeServiceBalanceNotFoundException e) {
            assertEquals(String.format("Balance for account \"%s\" on %s not found", "account1", thisDay.toString()),
                    e.getMessage());
        }

        verify(accountDao).get(accountId);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);
    }

    /**
     * If balance for this day does not exist, but exists for previous one, then adding balance for this day
     *
     * @throws Exception - exception
     */
    @Test
    public void testSaveDto_InsertThisNotExists() throws Exception {
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "account1");
        OperationDto operationDto =
                new OperationDto(null, accountId, "account1", categoryId, "category1", thisDay, amount, "note1");
        OperationEntity operationEntity = new OperationEntity(null, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(operationEntity).when(operationConverter).convertToEntity(operationDto);
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(null).when(balanceDao).get(accountId, thisDay);
        doReturn(1).when(balanceDao).insert(any(BalanceEntity.class));
        doReturn(1).when(operationDao).insert(operationEntity);

        operationService.save(operationDto);

        BalanceEntity expected = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), false);
        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).get(accountId);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(operationConverter).convertToEntity(operationDto);
        verify(balanceDao).insert(argumentCaptor.capture());
        verify(operationDao).insert(operationEntity);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);

        assertBalanceEntityEquals(expected, argumentCaptor.getValue());
    }

    /**
     * If balance for this day exists, but previous one does not exist, then adding balance for day before this
     *
     * @throws Exception - exception
     */
    @Test
    public void testSaveDto_InsertPrevNotExists() throws Exception {
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 5, 6);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "title");
        OperationDto operationDto =
                new OperationDto(null, accountId, "account1", categoryId, "category1", thisDay, amount, "note1");
        OperationEntity operationEntity = new OperationEntity(null, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(operationEntity).when(operationConverter).convertToEntity(operationDto);
        doReturn(null).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(1).when(balanceDao).insert(any(BalanceEntity.class));
        doReturn(1).when(operationDao).insert(operationEntity);

        operationService.save(operationDto);

        BalanceEntity expected = new BalanceEntity(accountId, prevDay, new BigDecimal("11.25"), false);
        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).get(accountId);
        verify(operationConverter).convertToEntity(operationDto);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).insert(argumentCaptor.capture());
        verify(operationDao).insert(operationEntity);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);

        assertBalanceEntityEquals(expected, argumentCaptor.getValue());
    }

    /**
     * Balances for this and previous days exist, but fixed. No balance update
     *
     * @throws Exception - exception
     */
    @Test
    public void testSaveDto_UpdateBeforeNotExistsPrevFixedThisFixedAfterNotExists() throws Exception {
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "account1");
        OperationDto operationDto =
                new OperationDto(null, accountId, "account1", categoryId, "category1", thisDay, amount, "note1");
        OperationEntity operationEntity = new OperationEntity(null, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), true);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(operationEntity).when(operationConverter).convertToEntity(operationDto);
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(1).when(operationDao).insert(operationEntity);

        operationService.save(operationDto);

        verify(accountDao).get(accountId);
        verify(operationConverter).convertToEntity(operationDto);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(operationDao).insert(operationEntity);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);
    }

    /**
     * If previous balance fixed and this balance not fixed, then recalculating this balance if no one after it
     *
     * @throws Exception - exception
     */
    @Test
    public void testSaveDto_UpdateBeforeNotExistsPrevFixedThisNotFixedAfterNotExists() throws Exception {
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "account1");
        OperationDto operationDto =
                new OperationDto(null, accountId, "account1", categoryId, "category1", thisDay, amount, "note1");
        OperationEntity operationEntity = new OperationEntity(null, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), true);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(operationEntity).when(operationConverter).convertToEntity(operationDto);
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(null).when(balanceDao).getEntityAfter(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class));
        doReturn(1).when(operationDao).insert(operationEntity);

        operationService.save(operationDto);

        BalanceEntity expected = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), false);
        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).get(accountId);
        verify(operationConverter).convertToEntity(operationDto);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getEntityAfter(accountId, thisDay);
        verify(balanceDao).update(argumentCaptor.capture());
        verify(operationDao).insert(operationEntity);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);

        assertBalanceEntityEquals(expected, argumentCaptor.getValue());
    }

    /**
     * If previous balance fixed and this balance not fixed, then no recalculation if balance after this exists
     *
     * @throws Exception - exception
     */
    @Test
    public void testSaveDto_UpdateBeforeNotExistsPrevFixedThisNotFixedAfterExists() throws Exception {
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        LocalDate afterDay = LocalDate.of(2017, 5, 9);
        BigDecimal amount = new BigDecimal("1.25");

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "account1");
        OperationDto operationDto =
                new OperationDto(null, accountId, "account1", categoryId, "category1", thisDay, amount, "note1");
        OperationEntity operationEntity = new OperationEntity(null, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), true);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), false);
        BalanceEntity afterBalanceEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("10.00"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(operationEntity).when(operationConverter).convertToEntity(operationDto);
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterBalanceEntity).when(balanceDao).getEntityAfter(accountId, thisDay);
        doReturn(1).when(operationDao).insert(operationEntity);

        operationService.save(operationDto);

        verify(accountDao).get(accountId);
        verify(operationConverter).convertToEntity(operationDto);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getEntityAfter(accountId, thisDay);
        verify(operationDao).insert(operationEntity);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);
    }

    /**
     * If this balance fixed and previous balance not fixed, then recalculating previous balance if no one before it
     *
     * @throws Exception - exception
     */
    @Test
    public void testSaveDto_UpdateBeforeNotExistsPrevNotFixedThisFixedAfterNotExists() throws Exception {
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "account1");
        OperationDto operationDto =
                new OperationDto(null, accountId, "account1", categoryId, "category1", thisDay, amount, "note1");
        OperationEntity operationEntity = new OperationEntity(null, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(operationEntity).when(operationConverter).convertToEntity(operationDto);
        doReturn(null).when(balanceDao).getEntityBefore(accountId, prevDay);
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class));
        doReturn(1).when(operationDao).insert(operationEntity);

        operationService.save(operationDto);

        BalanceEntity expected = new BalanceEntity(accountId, prevDay, new BigDecimal("11.25"), false);
        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).get(accountId);
        verify(operationConverter).convertToEntity(operationDto);
        verify(balanceDao).getEntityBefore(accountId, prevDay);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).update(argumentCaptor.capture());
        verify(operationDao).insert(operationEntity);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);

        assertBalanceEntityEquals(expected, argumentCaptor.getValue());
    }

    /**
     * If this balance fixed and prev balance not fixed, then no recalculation if balance before previous exists
     *
     * @throws Exception - exception
     */
    @Test
    public void testSaveDto_UpdateBeforeExistsPrevNotFixedThisFixedAfterNotExists() throws Exception {
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate beforeDay = LocalDate.of(2017, 5, 3);
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "account1");
        OperationDto operationDto =
                new OperationDto(null, accountId, "account1", categoryId, "category1", thisDay, amount, "note1");
        OperationEntity operationEntity = new OperationEntity(null, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity beforeBalanceEntity = new BalanceEntity(accountId, beforeDay, new BigDecimal("10.00"), false);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), true);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(operationEntity).when(operationConverter).convertToEntity(operationDto);
        doReturn(beforeBalanceEntity).when(balanceDao).getEntityBefore(accountId, prevDay);
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(1).when(operationDao).insert(operationEntity);

        operationService.save(operationDto);

        verify(accountDao).get(accountId);
        verify(operationConverter).convertToEntity(operationDto);
        verify(balanceDao).getEntityBefore(accountId, prevDay);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(operationDao).insert(operationEntity);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);
    }

    /**
     * If if this balance exists and previous exists, but no balances before and after, then recalculating this balance
     *
     * @throws Exception - exception
     */
    @Test
    public void testSaveDto_UpdateBeforeNotExistsPrevNotFixedThisNotFixedAfterNotExists() throws Exception {
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "account1");
        OperationDto operationDto =
                new OperationDto(null, accountId, "account1", categoryId, "category1", thisDay, amount, "note1");
        OperationEntity operationEntity = new OperationEntity(null, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(operationEntity).when(operationConverter).convertToEntity(operationDto);
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class));
        doReturn(1).when(operationDao).insert(operationEntity);

        operationService.save(operationDto);

        BalanceEntity expected = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), false);
        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).get(accountId);
        verify(operationConverter).convertToEntity(operationDto);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getEntityAfter(accountId, thisDay);
        verify(balanceDao).update(argumentCaptor.capture());
        verify(operationDao).insert(operationEntity);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);

        assertBalanceEntityEquals(expected, argumentCaptor.getValue());
    }

    /**
     * If if this balance, previous and before exist, but no balance after this, then recalculating this balance
     *
     * @throws Exception - exception
     */
    @Test
    public void testSaveDto_UpdateBeforeExistsPrevNotFixedThisNotFixedAfterNotExists() throws Exception {
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate beforeDay = LocalDate.of(2017, 5, 3);
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "account1");
        OperationDto operationDto =
                new OperationDto(null, accountId, "account1", categoryId, "category1", thisDay, amount, "note1");
        OperationEntity operationEntity = new OperationEntity(null, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity beforeBalanceEntity = new BalanceEntity(accountId, beforeDay, new BigDecimal("10.00"), false);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(operationEntity).when(operationConverter).convertToEntity(operationDto);
        doReturn(beforeBalanceEntity).when(balanceDao).getEntityBefore(accountId, prevDay);
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class));
        doReturn(1).when(operationDao).insert(operationEntity);

        operationService.save(operationDto);

        BalanceEntity expected = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), false);
        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).get(accountId);
        verify(operationConverter).convertToEntity(operationDto);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getEntityAfter(accountId, thisDay);
        verify(balanceDao).update(argumentCaptor.capture());
        verify(operationDao).insert(operationEntity);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);

        assertBalanceEntityEquals(expected, argumentCaptor.getValue());
    }

    /**
     * If if this balance, previous and after exist, but no balance before, then recalculating previous balance
     *
     * @throws Exception - exception
     */
    @Test
    public void testSaveDto_UpdateBeforeNotExistsPrevNotFixedThisNotFixedAfterExists() throws Exception {
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        LocalDate afterDay = LocalDate.of(2017, 5, 9);
        BigDecimal amount = new BigDecimal("1.25");

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "account1");
        OperationDto operationDto =
                new OperationDto(null, accountId, "account1", categoryId, "category1", thisDay, amount, "note1");
        OperationEntity operationEntity = new OperationEntity(null, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), false);
        BalanceEntity afterBalanceEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("10.00"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(operationEntity).when(operationConverter).convertToEntity(operationDto);
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterBalanceEntity).when(balanceDao).getEntityAfter(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class));
        doReturn(1).when(operationDao).insert(operationEntity);

        operationService.save(operationDto);

        BalanceEntity expected = new BalanceEntity(accountId, prevDay, new BigDecimal("11.25"), false);
        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(accountDao).get(accountId);
        verify(operationConverter).convertToEntity(operationDto);
        verify(balanceDao).getEntityBefore(accountId, prevDay);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getEntityAfter(accountId, thisDay);
        verify(balanceDao).update(argumentCaptor.capture());
        verify(operationDao).insert(operationEntity);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);

        assertBalanceEntityEquals(expected, argumentCaptor.getValue());
    }

    /**
     * If this balance exists, and previous and after exist, and before exists then no balance update
     *
     * @throws Exception - exception
     */
    @Test
    public void testSaveDto_UpdateBeforeExistsPrevNotFixedThisNotFixedAfterExists() throws Exception {
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate beforeDay = LocalDate.of(2017, 5, 3);
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        LocalDate afterDay = LocalDate.of(2017, 5, 9);
        BigDecimal amount = new BigDecimal("1.25");

        AccountEntity accountEntity = new AccountEntity(accountId, 4, "01", "account1");
        OperationDto operationDto =
                new OperationDto(null, accountId, "account1", categoryId, "category1", thisDay, amount, "note1");
        OperationEntity operationEntity = new OperationEntity(null, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity beforeBalanceEntity = new BalanceEntity(accountId, beforeDay, new BigDecimal("10.00"), false);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), false);
        BalanceEntity afterBalanceEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("10.00"), false);

        doReturn(accountEntity).when(accountDao).get(accountId);
        doReturn(operationEntity).when(operationConverter).convertToEntity(operationDto);
        doReturn(beforeBalanceEntity).when(balanceDao).getEntityBefore(accountId, prevDay);
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterBalanceEntity).when(balanceDao).getEntityAfter(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class));
        doReturn(1).when(operationDao).insert(operationEntity);

        operationService.save(operationDto);

        verify(accountDao).get(accountId);
        verify(operationConverter).convertToEntity(operationDto);
        verify(balanceDao).getEntityBefore(accountId, prevDay);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getEntityAfter(accountId, thisDay);
        verify(operationDao).insert(operationEntity);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);
    }

    /**
     * Error is thrown when operation with specified id is not found
     *
     * @throws Exception - exception
     */
    @Test
    public void testDeleteById_OperationNotFound() throws Exception {
        Integer operationId = 1;

        try {
            operationService.delete(operationId);
        } catch (IncomeServiceOperationNotFoundException e) {
            assertEquals(String.format("Operation with id %d not found", operationId), e.getMessage());
        }

        verify(operationDao).get(operationId);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);
    }

    /**
     * Error is thrown when this balance not found
     *
     * @throws Exception - exception
     */
    @Test
    public void testDeleteById_ThisBalanceNotFound() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 5, 6);
        BigDecimal amount = new BigDecimal("1.25");
        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note1");

        doReturn(operationEntity).when(operationDao).get(operationId);

        try {
            operationService.delete(operationId);
        } catch (IncomeServiceBalanceNotFoundException e) {
            assertEquals(String.format("Balance for account with id %d on %s not found", accountId, thisDay),
                    e.getMessage());
        }

        verify(operationDao).get(operationEntity.getId());
        verify(balanceDao).get(operationEntity.getAccountId(), operationEntity.getDay());
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);
    }

    /**
     * Error is thrown when previous balance not found
     *
     * @throws Exception - exception
     */
    @Test
    public void testDeleteById_PrevBalanceNotFound() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), false);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);

        try {
            operationService.delete(operationId);
        } catch (IncomeServiceBalanceNotFoundException e) {
            assertEquals(String.format("Balance for account with id %d on %s not found", accountId, thisDay),
                    e.getMessage());
        }

        verify(operationDao).get(operationId);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);
    }

    /**
     * Operation is deleted, no balance updates, when:
     * - this balance exists and is fixed
     * - balance after this does not exist
     * - previous balance exists and is fixed
     * - balance before this does not exist
     *
     * @throws Exception - exception
     */
    @Test
    public void testDeleteDto_BeforeNotExistPrevFixedThisFixedAfterNotExist() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("11.25"), true);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), true);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(1).when(operationDao).delete(operationId);

        operationService.delete(operationId);

        verify(operationDao).get(operationId);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(operationDao).delete(operationId);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);
    }

    /**
     * Operation is deleted, this balance is updated, when:
     * - this balance exists and is not fixed
     * - balance after this does not exist
     * - previous balance exists and is fixed
     * - balance before this is not encountered
     *
     * @throws Exception - exception
     */
    @Test
    public void testDeleteDto_BeforeNotExistPrevFixedThisNotFixedAfterNotExist() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("11.25"), true);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), false);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class));
        doReturn(1).when(operationDao).delete(operationId);

        operationService.delete(operationId);

        BalanceEntity expected = new BalanceEntity(accountId, thisDay, new BigDecimal("11.25"), false);
        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(operationDao).get(operationId);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getEntityAfter(accountId, thisDay);
        verify(balanceDao).update(argumentCaptor.capture());
        verify(operationDao).delete(operationId);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);

        assertBalanceEntityEquals(expected, argumentCaptor.getValue());
    }

    /**
     * Operation is deleted, no balance is updated, when:
     * - this balance exists and is not fixed
     * - balance after this exists
     * - previous balance exists and not fixed
     * - balance before previous is not encountered
     *
     * @throws Exception - exception
     */
    @Test
    public void testDeleteDto_BeforeNotExistPrevFixedThisNotFixedAfterExist() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        LocalDate afterDay = LocalDate.of(2017, 5, 9);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("11.25"), true);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), false);
        BalanceEntity afterBalanceEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("10.00"), false);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterBalanceEntity).when(balanceDao).getEntityAfter(accountId, thisDay);
        doReturn(1).when(operationDao).delete(operationId);

        operationService.delete(operationId);

        verify(operationDao).get(operationId);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getEntityAfter(accountId, thisDay);
        verify(operationDao).delete(operationId);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);
    }

    /**
     * Operation is deleted, balance before this is updated, when:
     * - this balance exists and is fixed
     * - balance after this is not encountered
     * - previous balance exists and is not fixed
     * - no balance before previous previous
     *
     * @throws Exception - exception
     */
    @Test
    public void testDeleteDto_BeforeNotExistPrevNotFixedThisFixedAfterNotExist() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("11.25"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), true);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class));
        doReturn(1).when(operationDao).delete(operationId);

        operationService.delete(operationId);

        BalanceEntity expected = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), false);
        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(operationDao).get(operationId);
        verify(balanceDao).getEntityBefore(accountId, prevDay);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).update(argumentCaptor.capture());
        verify(operationDao).delete(operationId);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);

        assertBalanceEntityEquals(expected, argumentCaptor.getValue());
    }

    /**
     * Operation is deleted, no balance update, when:
     * - this balance exists and is fixed
     * - balance after this is not encountered
     * - previous balance exists and is not fixed
     * - balance before previous exists
     *
     * @throws Exception - exception
     */
    @Test
    public void testDeleteDto_BeforeExistPrevNotFixedThisFixedAfterNotExist() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate beforeDay = LocalDate.of(2017, 5, 3);
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity beforeBalanceEntity = new BalanceEntity(accountId, beforeDay, new BigDecimal("11.25"), false);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("11.25"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), true);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(beforeBalanceEntity).when(balanceDao).getEntityBefore(accountId, prevDay);
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(1).when(operationDao).delete(operationId);

        operationService.delete(operationId);

        verify(operationDao).get(operationId);
        verify(balanceDao).getEntityBefore(accountId, prevDay);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(operationDao).delete(operationId);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);
    }

    /**
     * Operation is deleted, this balance is updated, when:
     * - this balance exists and is not fixed
     * - no balance after this
     * - previous balance exists and is not fixed
     * - no balance before previous
     *
     * @throws Exception - exception
     */
    @Test
    public void testDeleteDto_BeforeNotExistPrevNotFixedThisNotFixedAfterNotExist() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), false);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class));
        doReturn(1).when(operationDao).delete(operationId);

        operationService.delete(operationId);

        BalanceEntity expected = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), false);
        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(operationDao).get(operationId);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getEntityAfter(accountId, thisDay);
        verify(balanceDao).update(argumentCaptor.capture());
        verify(operationDao).delete(operationId);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);

        assertBalanceEntityEquals(expected, argumentCaptor.getValue());
    }

    /**
     * Operation is deleted, this balance is updated, when:
     * - this balance exists and is not fixed
     * - no balance after this
     * - previous balance exists and is not fixed
     * - balance before previous exists
     *
     * @throws Exception - exception
     */
    @Test
    public void testDeleteDto_BeforeExistPrevNotFixedThisNotFixedAfterNotExist() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate beforeDay = LocalDate.of(2017, 5, 3);
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity beforeBalanceEntity = new BalanceEntity(accountId, beforeDay, new BigDecimal("10.00"), false);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), false);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(beforeBalanceEntity).when(balanceDao).getEntityBefore(accountId, prevDay);
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class));
        doReturn(1).when(operationDao).delete(operationId);

        operationService.delete(operationId);

        BalanceEntity expected = new BalanceEntity(accountId, thisDay, new BigDecimal("10.00"), false);
        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(operationDao).get(operationId);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getEntityAfter(accountId, thisDay);
        verify(balanceDao).update(argumentCaptor.capture());
        verify(operationDao).delete(operationId);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);

        assertBalanceEntityEquals(expected, argumentCaptor.getValue());
    }

    /**
     * Operation is deleted, previous balance is updated, when:
     * - this balance exists and not fixed
     * - balance after this exists
     * - previous balance exists and not fixed
     * - balance before previous does not exist
     *
     * @throws Exception - exception
     */
    @Test
    public void testDeleteDto_BeforeNotExistPrevNotFixedThisNotFixedAfterExist() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        LocalDate afterDay = LocalDate.of(2017, 5, 9);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), false);
        BalanceEntity afterBalanceEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("8.75"), false);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterBalanceEntity).when(balanceDao).getEntityAfter(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class));
        doReturn(1).when(operationDao).delete(operationId);

        operationService.delete(operationId);

        BalanceEntity expected = new BalanceEntity(accountId, prevDay, new BigDecimal("8.75"), false);
        ArgumentCaptor<BalanceEntity> argumentCaptor = ArgumentCaptor.forClass(BalanceEntity.class);

        verify(operationDao).get(operationId);
        verify(balanceDao).getEntityBefore(accountId, prevDay);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getEntityAfter(accountId, thisDay);
        verify(balanceDao).update(argumentCaptor.capture());
        verify(operationDao).delete(operationId);
        verifyNoMoreInteractions(accountDao, operationDao, balanceDao, categoryDao, operationConverter);

        assertBalanceEntityEquals(expected, argumentCaptor.getValue());
    }

    /**
     * Operation is deleted, no balance update, when:
     * - this balance exists and not fixed
     * - balance after this exists and not fixed
     * - prev balance exists and not fixed
     * - balance before prev exists and not fixed
     *
     * @throws Exception - exception
     */
    @Test
    public void testDeleteDto_BeforeExistPrevNotFixedThisNotFixedAfterExist() throws Exception {
        Integer operationId = 1;
        Integer accountId = 2;
        Integer categoryId = 3;
        LocalDate beforeDay = LocalDate.of(2017, 5, 3);
        LocalDate prevDay = LocalDate.of(2017, 5, 5);
        LocalDate thisDay = LocalDate.of(2017, 5, 7);
        LocalDate afterDay = LocalDate.of(2017, 5, 9);
        BigDecimal amount = new BigDecimal("1.25");

        OperationEntity operationEntity =
                new OperationEntity(operationId, accountId, categoryId, thisDay, amount, "note1");
        BalanceEntity beforeBalanceEntity = new BalanceEntity(accountId, beforeDay, new BigDecimal("10.00"), false);
        BalanceEntity prevBalanceEntity = new BalanceEntity(accountId, prevDay, new BigDecimal("10.00"), false);
        BalanceEntity thisBalanceEntity = new BalanceEntity(accountId, thisDay, new BigDecimal("8.75"), false);
        BalanceEntity afterBalanceEntity = new BalanceEntity(accountId, afterDay, new BigDecimal("8.75"), false);

        doReturn(operationEntity).when(operationDao).get(operationId);
        doReturn(beforeBalanceEntity).when(balanceDao).getEntityBefore(accountId, prevDay);
        doReturn(prevBalanceEntity).when(balanceDao).getEntityBefore(accountId, thisDay);
        doReturn(thisBalanceEntity).when(balanceDao).get(accountId, thisDay);
        doReturn(afterBalanceEntity).when(balanceDao).getEntityAfter(accountId, thisDay);
        doReturn(1).when(balanceDao).update(any(BalanceEntity.class));
        doReturn(1).when(operationDao).delete(operationId);

        operationService.delete(operationId);

        verify(operationDao).get(operationId);
        verify(balanceDao).getEntityBefore(accountId, prevDay);
        verify(balanceDao).getEntityBefore(accountId, thisDay);
        verify(balanceDao).get(accountId, thisDay);
        verify(balanceDao).getEntityAfter(accountId, thisDay);
        verify(operationDao).delete(operationId);
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
        doReturn(categoryEntityList).when(categoryDao).getList(categoryIds);
        doReturn(accountEntityList).when(accountDao).getList(accountIds);
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
        doReturn(accountEntity).when(accountDao).get(accountId);
        OperationDto expected = new OperationDto(null, accountId, "account1", null, null, day, BigDecimal.ZERO, null);
        OperationDto actual = operationService.getDto(Collections.singleton(accountId), null, day);
        assertOperationDtoEquals(expected, actual);
        verify(accountDao).get(accountId);
        verifyNoMoreInteractions(operationDao, accountDao, categoryDao, balanceDao, operationConverter);
    }

    @Test
    public void testGetDto_MultipleAccountIdsCategoryId() throws Exception {
        Integer categoryId = 1;
        LocalDate day = LocalDate.of(2017, 4, 12);
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "01", "category1");
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        OperationDto expected = new OperationDto(null, null, null, categoryId, "category1", day, BigDecimal.ZERO, null);
        OperationDto actual = operationService.getDto(Sets.newSet(1, 2), categoryId, day);
        assertOperationDtoEquals(expected, actual);
        verify(categoryDao).get(categoryId);
        verifyNoMoreInteractions(operationDao, accountDao, categoryDao, balanceDao, operationConverter);
    }

    @Test
    public void testGetDto_SingleAccountIdCategoryId() throws Exception {
        Integer accountId = 1;
        Integer categoryId = 2;
        LocalDate day = LocalDate.of(2017, 4, 12);
        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "account1");
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "01", "category1");
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        doReturn(accountEntity).when(accountDao).get(accountId);
        OperationDto expected = new OperationDto(null, accountId, "account1", categoryId, "category1", day,
                BigDecimal.ZERO, null);
        OperationDto actual = operationService.getDto(Collections.singleton(accountId), categoryId, day);
        assertOperationDtoEquals(expected, actual);
        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verifyNoMoreInteractions(operationDao, accountDao, categoryDao, balanceDao, operationConverter);
    }

}
