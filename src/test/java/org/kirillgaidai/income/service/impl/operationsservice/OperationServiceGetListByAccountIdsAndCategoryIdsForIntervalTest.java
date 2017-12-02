package org.kirillgaidai.income.service.impl.operationsservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.service.dto.OperationDto;
import org.mockito.internal.util.collections.Sets;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class OperationServiceGetListByAccountIdsAndCategoryIdsForIntervalTest extends OperationServiceBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Integer accountId1 = 11;
        Integer accountId2 = 12;
        Integer categoryId1 = 21;
        Integer categoryId2 = 22;
        LocalDate day1 = LocalDate.of(2017, 4, 12);
        LocalDate day2 = day1.plusDays(2L);
        BigDecimal amount1 = new BigDecimal("100");
        BigDecimal amount2 = new BigDecimal("120");
        BigDecimal amount3 = new BigDecimal("140");
        Set<Integer> categoryIds = Sets.newSet(categoryId1, categoryId2);
        Set<Integer> accountIds = Sets.newSet(accountId1, accountId2);

        List<OperationEntity> operationEntityList = Arrays.asList(
                new OperationEntity(1, accountId1, categoryId1, day2, amount1, "note1"),
                new OperationEntity(2, accountId1, categoryId2, day1, amount2, "note2"),
                new OperationEntity(3, accountId2, categoryId1, day1, amount3, "note3")
        );
        List<CategoryEntity> categoryEntityList = Arrays.asList(
                new CategoryEntity(categoryId1, "01", "category1"),
                new CategoryEntity(categoryId2, "02", "category2")
        );
        List<AccountEntity> accountEntityList = Arrays.asList(
                new AccountEntity(accountId1, 31, "01", "account1"),
                new AccountEntity(accountId2, 31, "02", "account2")
        );

        doReturn(operationEntityList).when(operationDao).getList(accountIds, categoryIds, day1, day2);
        doReturn(categoryEntityList).when(categoryDao).getList(categoryIds);
        doReturn(accountEntityList).when(accountDao).getList(accountIds);

        List<OperationDto> expected = Arrays.asList(
                new OperationDto(1, accountId1, "account1", categoryId1, "category1", day2, amount1, "note1"),
                new OperationDto(2, accountId1, "account1", categoryId2, "category2", day1, amount2, "note2"),
                new OperationDto(3, accountId2, "account2", categoryId1, "category1", day1, amount3, "note3")
        );
        List<OperationDto> actual = service.getList(accountIds, categoryIds, day1, day2);
        assertEntityListEquals(expected, actual);

        verify(operationDao).getList(accountIds, categoryIds, day1, day2);
        verify(categoryDao).getList(categoryIds);
        verify(accountDao).getList(accountIds);

        verifyNoMoreDaoInteractions();
    }

}
