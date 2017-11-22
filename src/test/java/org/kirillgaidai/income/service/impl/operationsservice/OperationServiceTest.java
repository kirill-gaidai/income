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
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertOperationDtoEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertOperationDtoListEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class OperationServiceTest extends OperationServiceBaseTest {

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

        doReturn(operationEntityList).when(operationDao).getList(accountIds, day);
        doReturn(categoryEntityList).when(categoryDao).getList(categoryIds);
        doReturn(accountEntityList).when(accountDao).getList(accountIds);
        for (int index = 0; index < operationEntityList.size(); index++) {
            doReturn(operationDtoList.get(index)).when(converter).convertToDto(operationEntityList.get(index));
        }

        List<OperationDto> expected = Arrays.asList(
                new OperationDto(1, 11, "account1", 21, "category1", day, new BigDecimal("100"), "note1"),
                new OperationDto(2, 11, "account1", 22, "category2", day, new BigDecimal("120"), "note2"),
                new OperationDto(3, 12, "account2", 21, "category1", day, new BigDecimal("140"), "note3")
        );

        List<OperationDto> actual = service.getDtoList(accountIds, day);
        assertOperationDtoListEquals(expected, actual);
    }

    @Test
    public void testGetDto_MultipleAccountIdsNoCategoryId() throws Exception {
        LocalDate day = LocalDate.of(2017, 4, 12);
        OperationDto expected = new OperationDto(null, null, null, null, null, day, BigDecimal.ZERO, null);
        OperationDto actual = service.getDto(Sets.newSet(1, 2), null, day);
        assertOperationDtoEquals(expected, actual);
        verifyNoMoreInteractions();
    }

    @Test
    public void testGetDto_SingleAccountIdNoCategoryId() throws Exception {
        Integer accountId = 1;
        LocalDate day = LocalDate.of(2017, 4, 12);
        AccountEntity accountEntity = new AccountEntity(accountId, 2, "01", "account1");
        doReturn(accountEntity).when(accountDao).get(accountId);
        OperationDto expected = new OperationDto(null, accountId, "account1", null, null, day, BigDecimal.ZERO, null);
        OperationDto actual = service.getDto(Collections.singleton(accountId), null, day);
        assertOperationDtoEquals(expected, actual);
        verify(accountDao).get(accountId);
        verifyNoMoreInteractions();
    }

    @Test
    public void testGetDto_MultipleAccountIdsCategoryId() throws Exception {
        Integer categoryId = 1;
        LocalDate day = LocalDate.of(2017, 4, 12);
        CategoryEntity categoryEntity = new CategoryEntity(categoryId, "01", "category1");
        doReturn(categoryEntity).when(categoryDao).get(categoryId);
        OperationDto expected = new OperationDto(null, null, null, categoryId, "category1", day, BigDecimal.ZERO, null);
        OperationDto actual = service.getDto(Sets.newSet(1, 2), categoryId, day);
        assertOperationDtoEquals(expected, actual);
        verify(categoryDao).get(categoryId);
        verifyNoMoreInteractions();
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
        OperationDto actual = service.getDto(Collections.singleton(accountId), categoryId, day);
        assertOperationDtoEquals(expected, actual);
        verify(accountDao).get(accountId);
        verify(categoryDao).get(categoryId);
        verifyNoMoreInteractions();
    }

}
