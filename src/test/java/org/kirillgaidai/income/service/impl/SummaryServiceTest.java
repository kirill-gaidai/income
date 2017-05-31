package org.kirillgaidai.income.service.impl;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.dao.impl.AccountDao;
import org.kirillgaidai.income.dao.impl.BalanceDao;
import org.kirillgaidai.income.dao.impl.CategoryDao;
import org.kirillgaidai.income.dao.impl.OperationDao;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.dao.intf.IOperationDao;
import org.kirillgaidai.income.service.converter.AccountConverter;
import org.kirillgaidai.income.service.converter.CategoryConverter;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.dto.SummaryDto;
import org.mockito.internal.util.collections.Sets;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertAccountDtoListEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertCategoryDtoListEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class SummaryServiceTest {

    final private IOperationDao operationDao = mock(OperationDao.class);
    final private ICategoryDao categoryDao = mock(CategoryDao.class);
    final private IAccountDao accountDao = mock(AccountDao.class);
    final private IBalanceDao balanceDao = mock(BalanceDao.class);
    final private IGenericConverter<AccountEntity, AccountDto> accountConverter = mock(AccountConverter.class);
    final private IGenericConverter<CategoryEntity, CategoryDto> categoryConverter = mock(CategoryConverter.class);
    final private SummaryService summaryService = new SummaryService(accountDao, categoryDao, operationDao, balanceDao,
            accountConverter, categoryConverter);

    @Test
    public void testGetSummaryDto() throws Exception {
        Integer[] accountIds = new Integer[]{11, 10};
        Integer[] categoryIds = new Integer[]{22, 21, 20};
        LocalDate[] days = new LocalDate[]{
                LocalDate.of(2017, 4, 10), LocalDate.of(2017, 4, 11), LocalDate.of(2017, 4, 12),
                LocalDate.of(2017, 4, 13), LocalDate.of(2017, 4, 14), LocalDate.of(2017, 4, 15)
        };

        List<OperationEntity> operationEntityList = Arrays.asList(
                new OperationEntity(1, accountIds[0], categoryIds[0], days[2], new BigDecimal("-100"), "note1"),
                new OperationEntity(2, accountIds[1], categoryIds[1], days[3], new BigDecimal("25"), "note2"),
                new OperationEntity(3, accountIds[0], categoryIds[2], days[3], new BigDecimal("25"), "note3"),
                new OperationEntity(4, accountIds[0], categoryIds[1], days[4], new BigDecimal("75"), "note4"),
                new OperationEntity(5, accountIds[1], categoryIds[1], days[4], new BigDecimal("25"), "note5")
        );
        List<BalanceEntity> balanceEntityList = Arrays.asList(
                new BalanceEntity(accountIds[1], days[0], new BigDecimal("150"), false),
                new BalanceEntity(accountIds[1], days[1], new BigDecimal("50"), true),
                new BalanceEntity(accountIds[1], days[2], new BigDecimal("50"), false),
                new BalanceEntity(accountIds[1], days[3], new BigDecimal("75"), false),
                new BalanceEntity(accountIds[1], days[4], new BigDecimal("50"), false),
                new BalanceEntity(accountIds[1], days[5], new BigDecimal("150"), true),
                new BalanceEntity(accountIds[0], days[0], new BigDecimal("100"), false),
                new BalanceEntity(accountIds[0], days[1], new BigDecimal("100"), false),
                new BalanceEntity(accountIds[0], days[2], new BigDecimal("200"), false),
                new BalanceEntity(accountIds[0], days[3], new BigDecimal("125"), false),
                new BalanceEntity(accountIds[0], days[4], new BigDecimal("50"), false),
                new BalanceEntity(accountIds[0], days[5], new BigDecimal("50"), false)
        );
        List<AccountEntity> accountEntityList = Arrays.asList(
                new AccountEntity(accountIds[0], 31, "01", "account1"),
                new AccountEntity(accountIds[1], 32, "02", "account2")
        );
        List<AccountDto> accountDtoList = Arrays.asList(
                new AccountDto(accountIds[0], 31, null, null, "01", "account1"),
                new AccountDto(accountIds[1], 32, null, null, "02", "account2")
        );
        List<CategoryEntity> categoryEntityList = Arrays.asList(
                new CategoryEntity(categoryIds[0], "01", "category1"),
                new CategoryEntity(categoryIds[1], "02", "category2"),
                new CategoryEntity(categoryIds[2], "03", "category3")
        );
        List<CategoryDto> categoryDtoList = Arrays.asList(
                new CategoryDto(categoryIds[0], "01", "category1"),
                new CategoryDto(categoryIds[1], "02", "category2"),
                new CategoryDto(categoryIds[2], "03", "category3")
        );

        Set<Integer> accountIdSet = Sets.newSet(accountIds);
        Set<Integer> categoryIdSet = Sets.newSet(categoryIds);

        doReturn(accountEntityList).when(accountDao).getEntityList(accountIdSet);
        doReturn(categoryEntityList).when(categoryDao).getEntityList(categoryIdSet);
        doReturn(operationEntityList).when(operationDao).getEntityList(accountIdSet, days[1], days[5]);
        doReturn(balanceEntityList).when(balanceDao).getEntityList(accountIdSet, days[0], days[5]);
        for (int index = 0; index < accountEntityList.size(); index++) {
            doReturn(accountDtoList.get(index)).when(accountConverter).convertToDto(accountEntityList.get(index));
        }
        for (int index = 0; index < categoryEntityList.size(); index++) {
            doReturn(categoryDtoList.get(index)).when(categoryConverter).convertToDto(categoryEntityList.get(index));
        }

        SummaryDto expected = new SummaryDto();
        expected.setAccountDtoList(Arrays.asList(
                new AccountDto(accountIds[0], 31, null, null, "01", "account1"),
                new AccountDto(accountIds[1], 32, null, null, "02", "account2")
        ));
        expected.setCategoryDtoList(Arrays.asList(
                new CategoryDto(categoryIds[0], "01", "category1"),
                new CategoryDto(categoryIds[1], "02", "category2"),
                new CategoryDto(categoryIds[2], "03", "category3")
        ));
        expected.setSummaryDtoRowList(Arrays.asList(
                new SummaryDto.SummaryDtoRow(
                        days[1], new BigDecimal("-100"),
                        Arrays.asList(new BigDecimal("100"), new BigDecimal("50")), new BigDecimal("150"),
                        Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO), BigDecimal.ZERO
                ),
                new SummaryDto.SummaryDtoRow(
                        days[2], BigDecimal.ZERO,
                        Arrays.asList(new BigDecimal("200"), new BigDecimal("50")), new BigDecimal("250"),
                        Arrays.asList(new BigDecimal("-100"), BigDecimal.ZERO, BigDecimal.ZERO), new BigDecimal("-100")
                ),
                new SummaryDto.SummaryDtoRow(
                        days[3], BigDecimal.ZERO,
                        Arrays.asList(new BigDecimal("125"), new BigDecimal("75")), new BigDecimal("200"),
                        Arrays.asList(BigDecimal.ZERO, new BigDecimal("25"), new BigDecimal("25")), new BigDecimal("50")
                ),
                new SummaryDto.SummaryDtoRow(
                        days[4], BigDecimal.ZERO,
                        Arrays.asList(new BigDecimal("50"), new BigDecimal("50")), new BigDecimal("100"),
                        Arrays.asList(BigDecimal.ZERO, new BigDecimal("100"), BigDecimal.ZERO), new BigDecimal("100")
                ),
                new SummaryDto.SummaryDtoRow(
                        days[5], new BigDecimal("100"),
                        Arrays.asList(new BigDecimal("50"), new BigDecimal("150")), new BigDecimal("200"),
                        Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO), BigDecimal.ZERO
                )
        ));

        SummaryDto actual = summaryService.getSummaryDto(accountIdSet, days[1], days[5]);

        assertAccountDtoListEquals(expected.getAccountDtoList(), actual.getAccountDtoList());
        assertCategoryDtoListEquals(expected.getCategoryDtoList(), actual.getCategoryDtoList());

        List<SummaryDto.SummaryDtoRow> expectedSummaryDtoRowList = expected.getSummaryDtoRowList();
        List<SummaryDto.SummaryDtoRow> actualSummaryDtoRowList = actual.getSummaryDtoRowList();

        for (int index = 0; index < expectedSummaryDtoRowList.size(); index++) {
            SummaryDto.SummaryDtoRow expectedSummaryDtoRow = expectedSummaryDtoRowList.get(index);
            SummaryDto.SummaryDtoRow actualSummaryDtoRow = actualSummaryDtoRowList.get(index);

            assertEquals(expectedSummaryDtoRow.getDay(), actualSummaryDtoRow.getDay());
            assertEquals(expectedSummaryDtoRow.getDifference(), actualSummaryDtoRow.getDifference());
            assertEquals(expectedSummaryDtoRow.getBalances(), actualSummaryDtoRow.getBalances());
            assertEquals(expectedSummaryDtoRow.getBalancesSummary(), actualSummaryDtoRow.getBalancesSummary());
            assertEquals(expectedSummaryDtoRow.getAmounts(), actualSummaryDtoRow.getAmounts());
            assertEquals(expectedSummaryDtoRow.getAmountsSummary(), actualSummaryDtoRow.getAmountsSummary());
        }
    }

}
