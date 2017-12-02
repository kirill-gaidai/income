package org.kirillgaidai.income.service.impl;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertSummaryDtoEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class SummaryServiceTest {

    final private IOperationDao operationDao = mock(IOperationDao.class);
    final private ICategoryDao categoryDao = mock(ICategoryDao.class);
    final private IAccountDao accountDao = mock(IAccountDao.class);
    final private IBalanceDao balanceDao = mock(IBalanceDao.class);
    final private ICurrencyDao currencyDao = mock(ICurrencyDao.class);
    final private IGenericConverter<AccountEntity, AccountDto> accountConverter = mock(AccountConverter.class);
    final private IGenericConverter<CategoryEntity, CategoryDto> categoryConverter = mock(CategoryConverter.class);
    final private SummaryService summaryService = new SummaryService(
            accountDao, categoryDao, operationDao, balanceDao, currencyDao, accountConverter, categoryConverter);

    /**
     * Account sum is correct
     *
     * @throws Exception - exception
     */
    @Test
    public void testGetSummaryDto_AccountSumIsCorrect() throws Exception {
        LocalDate day = LocalDate.of(2017, 4, 10);
        Integer[] accountIds = new Integer[]{10, 11};
        Integer currencyId = 31;

        Set<Integer> accountIdsSet = Sets.newSet(accountIds);
        List<BalanceEntity> initialBalanceEntityList = Arrays.asList(
                new BalanceEntity(accountIds[0], day.minusDays(1L), new BigDecimal("100"), false),
                new BalanceEntity(accountIds[1], day.minusDays(1L), new BigDecimal("200"), false)
        );
        List<BalanceEntity> balanceEntityList = Arrays.asList(
                new BalanceEntity(accountIds[0], day, new BigDecimal("100"), false),
                new BalanceEntity(accountIds[1], day, new BigDecimal("200"), false)
        );
        List<AccountEntity> accountEntityList = Arrays.asList(
                new AccountEntity(accountIds[0], currencyId, "01", "account1"),
                new AccountEntity(accountIds[1], currencyId, "02", "account2")
        );
        List<AccountDto> accountDtoList = Arrays.asList(
                new AccountDto(accountIds[0], currencyId, null, null, "01", "account1"),
                new AccountDto(accountIds[1], currencyId, null, null, "02", "account2")
        );
        CurrencyEntity currencyEntity = new CurrencyEntity(currencyId, "CC1", "currency1", 2);

        doReturn(accountEntityList).when(accountDao).getList(accountIdsSet);
        doReturn(Collections.emptyList()).when(categoryDao).getList(Collections.emptySet());
        doReturn(currencyEntity).when(currencyDao).get(currencyId);
        doReturn(Collections.emptyList()).when(operationDao).getList(accountIdsSet, Collections.emptySet(), day, day);
        doReturn(initialBalanceEntityList).when(balanceDao).getList(accountIdsSet, day.minusDays(1L));
        doReturn(balanceEntityList).when(balanceDao).getList(accountIdsSet, day, day);
        for (int index = 0; index < accountEntityList.size(); index++) {
            doReturn(accountDtoList.get(index)).when(accountConverter).convertToDto(accountEntityList.get(index));
        }

        SummaryDto expected = new SummaryDto(
                accountDtoList,
                Collections.emptyList(),
                Collections.singletonList(new SummaryDto.SummaryDtoRow(
                        day, BigDecimal.ZERO,
                        Arrays.asList(balanceEntityList.get(0).getAmount(), balanceEntityList.get(1).getAmount()),
                        new BigDecimal("300"),
                        Collections.emptyList(),
                        BigDecimal.ZERO
                )),
                Collections.emptyList(),
                BigDecimal.ZERO
        );

        SummaryDto actual = summaryService.getSummaryDto(accountIdsSet, day, day);

        assertSummaryDtoEquals(expected, actual);
    }

    /**
     * Operation sum is correct
     *
     * @throws Exception - exception
     */
    @Test
    public void testGetSummaryDto_OperationSumIsCorrect() throws Exception {
        LocalDate day = LocalDate.of(2017, 4, 10);
        Integer accountId = 10;
        Integer currencyId = 31;
        Integer[] categoryIds = new Integer[]{20, 21};

        Set<Integer> accountIdsSet = Collections.singleton(accountId);
        Set<Integer> categoryIdsSet = Sets.newSet(categoryIds);
        List<BalanceEntity> initialBalanceEntityList = Collections.singletonList(
                new BalanceEntity(accountId, day.minusDays(1L), new BigDecimal("100"), false)
        );
        List<BalanceEntity> balanceEntityList = Collections.singletonList(
                new BalanceEntity(accountId, day, new BigDecimal("50"), false)
        );
        List<OperationEntity> operationEntityList = Arrays.asList(
                new OperationEntity(1, accountId, categoryIds[0], day, new BigDecimal("20"), "note1"),
                new OperationEntity(2, accountId, categoryIds[1], day, new BigDecimal("30"), "note2")
        );
        List<AccountEntity> accountEntityList = Collections.singletonList(
                new AccountEntity(accountId, currencyId, "01", "account1")
        );
        List<AccountDto> accountDtoList = Collections.singletonList(
                new AccountDto(accountId, currencyId, null, null, "01", "account1")
        );
        List<CategoryEntity> categoryEntityList = Arrays.asList(
                new CategoryEntity(categoryIds[0], "01", "category1"),
                new CategoryEntity(categoryIds[1], "02", "category2")
        );
        List<CategoryDto> categoryDtoList = Arrays.asList(
                new CategoryDto(categoryIds[0], "01", "category1"),
                new CategoryDto(categoryIds[1], "02", "category2")
        );
        CurrencyEntity currencyEntity = new CurrencyEntity(currencyId, "CC1", "currency1", 2);

        doReturn(accountEntityList).when(accountDao).getList(accountIdsSet);
        doReturn(categoryEntityList).when(categoryDao).getList(categoryIdsSet);
        doReturn(currencyEntity).when(currencyDao).get(currencyId);
        doReturn(accountDtoList.get(0)).when(accountConverter).convertToDto(accountEntityList.get(0));
        doReturn(operationEntityList).when(operationDao)
                .getList(accountIdsSet, Collections.emptySet(), day, day);
        doReturn(initialBalanceEntityList).when(balanceDao).getList(accountIdsSet, day.minusDays(1L));
        doReturn(balanceEntityList).when(balanceDao).getList(accountIdsSet, day, day);
        for (int index = 0; index < categoryEntityList.size(); index++) {
            doReturn(categoryDtoList.get(index)).when(categoryConverter).convertToDto(categoryEntityList.get(index));
        }

        SummaryDto expected = new SummaryDto(
                accountDtoList,
                categoryDtoList,
                Collections.singletonList(new SummaryDto.SummaryDtoRow(
                        day, BigDecimal.ZERO,
                        Collections.singletonList(balanceEntityList.get(0).getAmount()),
                        new BigDecimal("50"),
                        Arrays.asList(operationEntityList.get(0).getAmount(), operationEntityList.get(1).getAmount()),
                        new BigDecimal("50")
                )),
                Arrays.asList(operationEntityList.get(0).getAmount(), operationEntityList.get(1).getAmount()),
                operationEntityList.get(0).getAmount().add(operationEntityList.get(1).getAmount())
        );

        SummaryDto actual = summaryService.getSummaryDto(accountIdsSet, day, day);

        assertSummaryDtoEquals(expected, actual);
    }

    /**
     * Accounts balance difference is correct (caused by balance)
     *
     * @throws Exception - exception
     */
    @Test
    public void testGetSummaryDto_DifferenceIsCorrectBalance() throws Exception {
        LocalDate[] days = new LocalDate[]{LocalDate.of(2017, 4, 10), LocalDate.of(2017, 4, 11)};
        Integer accountId = 10;
        Integer currencyId = 31;

        Set<Integer> accountIdsSet = Collections.singleton(accountId);
        List<BalanceEntity> initialBalanceEntityList = Collections.singletonList(
                new BalanceEntity(accountId, days[0].minusDays(1L), new BigDecimal("100"), false)
        );
        List<BalanceEntity> balanceEntityList = Arrays.asList(
                new BalanceEntity(accountId, days[0], new BigDecimal("100"), false),
                new BalanceEntity(accountId, days[1], new BigDecimal("150"), false)
        );
        List<AccountEntity> accountEntityList = Collections.singletonList(
                new AccountEntity(accountId, currencyId, "01", "account1")
        );
        List<AccountDto> accountDtoList = Collections.singletonList(
                new AccountDto(accountId, currencyId, null, null, "01", "account1")
        );
        CurrencyEntity currencyEntity = new CurrencyEntity(currencyId, "CC1", "currency1", 2);

        doReturn(currencyEntity).when(currencyDao).get(currencyId);
        doReturn(Collections.emptyList()).when(operationDao)
                .getList(accountIdsSet, Collections.emptySet(), days[0], days[1]);
        doReturn(initialBalanceEntityList).when(balanceDao).getList(accountIdsSet, days[0].minusDays(1L));
        doReturn(balanceEntityList).when(balanceDao).getList(accountIdsSet, days[0], days[1]);
        doReturn(accountEntityList).when(accountDao).getList(accountIdsSet);
        doReturn(Collections.emptyList()).when(categoryDao).getList(Collections.emptySet());
        doReturn(accountDtoList.get(0)).when(accountConverter).convertToDto(accountEntityList.get(0));

        SummaryDto expected = new SummaryDto(
                accountDtoList,
                Collections.emptyList(),
                Arrays.asList(
                        new SummaryDto.SummaryDtoRow(
                                days[0], BigDecimal.ZERO,
                                Collections.singletonList(balanceEntityList.get(0).getAmount()),
                                new BigDecimal("100"),
                                Collections.emptyList(),
                                BigDecimal.ZERO
                        ),
                        new SummaryDto.SummaryDtoRow(
                                days[1], new BigDecimal("50"),
                                Collections.singletonList(balanceEntityList.get(1).getAmount()),
                                new BigDecimal("150"),
                                Collections.emptyList(),
                                BigDecimal.ZERO
                        )
                ),
                Collections.emptyList(),
                BigDecimal.ZERO
        );

        SummaryDto actual = summaryService.getSummaryDto(accountIdsSet, days[0], days[1]);

        assertSummaryDtoEquals(expected, actual);
    }

    /**
     * Accounts balance difference is correct (caused by operation)
     *
     * @throws Exception - exception
     */
    @Test
    public void testGetSummaryDto_DifferenceIsCorrectOperation() throws Exception {
        LocalDate[] days = new LocalDate[]{LocalDate.of(2017, 4, 10), LocalDate.of(2017, 4, 11)};
        Integer accountId = 10;
        Integer categoryId = 20;
        Integer currencyId = 31;

        Set<Integer> accountIdsSet = Collections.singleton(accountId);
        Set<Integer> categoryIdsSet = Collections.singleton(categoryId);
        List<BalanceEntity> initialBalanceEntityList = Collections.singletonList(
                new BalanceEntity(accountId, days[0].minusDays(1L), new BigDecimal("100"), false)
        );
        List<BalanceEntity> balanceEntityList = Collections.singletonList(
                new BalanceEntity(accountId, days[1], new BigDecimal("100"), false)
        );
        List<OperationEntity> operationEntityList = Collections.singletonList(
                new OperationEntity(1, accountId, categoryId, days[1], new BigDecimal("30"), "note1")
        );
        List<AccountEntity> accountEntityList = Collections.singletonList(
                new AccountEntity(accountId, currencyId, "01", "account1")
        );
        List<AccountDto> accountDtoList = Collections.singletonList(
                new AccountDto(accountId, currencyId, null, null, "01", "account1")
        );
        List<CategoryEntity> categoryEntityList = Collections.singletonList(
                new CategoryEntity(categoryId, "01", "category1")
        );
        List<CategoryDto> categoryDtoList = Collections.singletonList(
                new CategoryDto(categoryId, "01", "category1")
        );
        CurrencyEntity currencyEntity = new CurrencyEntity(currencyId, "CC1", "currency1", 2);

        doReturn(accountEntityList).when(accountDao).getList(accountIdsSet);
        doReturn(categoryEntityList).when(categoryDao).getList(categoryIdsSet);
        doReturn(currencyEntity).when(currencyDao).get(currencyId);
        doReturn(accountDtoList.get(0)).when(accountConverter).convertToDto(accountEntityList.get(0));
        doReturn(categoryDtoList.get(0)).when(categoryConverter).convertToDto(categoryEntityList.get(0));
        doReturn(operationEntityList).when(operationDao)
                .getList(accountIdsSet, Collections.emptySet(), days[0], days[1]);
        doReturn(initialBalanceEntityList).when(balanceDao).getList(accountIdsSet, days[0].minusDays(1L));
        doReturn(balanceEntityList).when(balanceDao).getList(accountIdsSet, days[0], days[1]);

        SummaryDto expected = new SummaryDto(
                accountDtoList,
                categoryDtoList,
                Arrays.asList(
                        new SummaryDto.SummaryDtoRow(
                                days[0], BigDecimal.ZERO,
                                Collections.singletonList(new BigDecimal("100")), new BigDecimal("100"),
                                Collections.singletonList(BigDecimal.ZERO), BigDecimal.ZERO
                        ),
                        new SummaryDto.SummaryDtoRow(
                                days[1], new BigDecimal("30"),
                                Collections.singletonList(new BigDecimal("100")), new BigDecimal("100"),
                                Collections.singletonList(new BigDecimal("30")), new BigDecimal("30")
                        )
                ),
                Collections.singletonList(new BigDecimal("30")),
                new BigDecimal("30")
        );

        SummaryDto actual = summaryService.getSummaryDto(accountIdsSet, days[0], days[1]);

        assertSummaryDtoEquals(expected, actual);
    }

    /**
     * Complex test
     *
     * @throws Exception - exception
     */
    @Test
    public void testGetSummaryDto_Complex() throws Exception {
        Integer[] accountIds = new Integer[]{10, 11};
        Integer[] categoryIds = new Integer[]{20, 21, 22};
        LocalDate[] days = new LocalDate[]{
                LocalDate.of(2017, 4, 10), LocalDate.of(2017, 4, 11), LocalDate.of(2017, 4, 12),
                LocalDate.of(2017, 4, 13), LocalDate.of(2017, 4, 14), LocalDate.of(2017, 4, 15)
        };
        Integer currencyId = 31;

        List<OperationEntity> operationEntityList = Arrays.asList(
                new OperationEntity(1, accountIds[0], categoryIds[0], days[2], new BigDecimal("-100"), "note1"),
                new OperationEntity(2, accountIds[1], categoryIds[1], days[3], new BigDecimal("25"), "note2"),
                new OperationEntity(3, accountIds[0], categoryIds[2], days[3], new BigDecimal("25"), "note3"),
                new OperationEntity(4, accountIds[0], categoryIds[1], days[4], new BigDecimal("75"), "note4"),
                new OperationEntity(5, accountIds[1], categoryIds[1], days[4], new BigDecimal("25"), "note5")
        );
        List<BalanceEntity> initialBalanceEntityList = Arrays.asList(
                new BalanceEntity(accountIds[0], days[0], new BigDecimal("100"), false),
                new BalanceEntity(accountIds[1], days[0], new BigDecimal("150"), false)
        );
        List<BalanceEntity> balanceEntityList = Arrays.asList(
                new BalanceEntity(accountIds[0], days[1], new BigDecimal("100"), false),
                new BalanceEntity(accountIds[1], days[1], new BigDecimal("50"), true),
                new BalanceEntity(accountIds[0], days[2], new BigDecimal("200"), false),
                new BalanceEntity(accountIds[1], days[2], new BigDecimal("50"), false),
                new BalanceEntity(accountIds[0], days[3], new BigDecimal("125"), false),
                new BalanceEntity(accountIds[1], days[3], new BigDecimal("75"), false),
                new BalanceEntity(accountIds[0], days[4], new BigDecimal("50"), false),
                new BalanceEntity(accountIds[1], days[4], new BigDecimal("50"), false),
                new BalanceEntity(accountIds[0], days[5], new BigDecimal("50"), false),
                new BalanceEntity(accountIds[1], days[5], new BigDecimal("150"), true)
        );
        List<AccountEntity> accountEntityList = Arrays.asList(
                new AccountEntity(accountIds[0], currencyId, "01", "account1"),
                new AccountEntity(accountIds[1], currencyId, "02", "account2")
        );
        List<AccountDto> accountDtoList = Arrays.asList(
                new AccountDto(accountIds[0], currencyId, null, null, "01", "account1"),
                new AccountDto(accountIds[1], currencyId, null, null, "02", "account2")
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
        CurrencyEntity currencyEntity = new CurrencyEntity(currencyId, "CC1", "currency1", 2);

        Set<Integer> accountIdSet = Sets.newSet(accountIds);
        Set<Integer> categoryIdSet = Sets.newSet(categoryIds);

        doReturn(accountEntityList).when(accountDao).getList(accountIdSet);
        doReturn(categoryEntityList).when(categoryDao).getList(categoryIdSet);
        doReturn(currencyEntity).when(currencyDao).get(currencyId);
        doReturn(operationEntityList).when(operationDao)
                .getList(accountIdSet, Collections.emptySet(), days[1], days[5]);
        doReturn(initialBalanceEntityList).when(balanceDao).getList(accountIdSet, days[0]);
        doReturn(balanceEntityList).when(balanceDao).getList(accountIdSet, days[1], days[5]);
        for (int index = 0; index < accountEntityList.size(); index++) {
            doReturn(accountDtoList.get(index)).when(accountConverter).convertToDto(accountEntityList.get(index));
        }
        for (int index = 0; index < categoryEntityList.size(); index++) {
            doReturn(categoryDtoList.get(index)).when(categoryConverter).convertToDto(categoryEntityList.get(index));
        }

        SummaryDto expected = new SummaryDto(
                accountDtoList, categoryDtoList,
                Arrays.asList(
                        new SummaryDto.SummaryDtoRow(
                                days[1], new BigDecimal("-100"),
                                Arrays.asList(new BigDecimal("100"), new BigDecimal("50")),
                                new BigDecimal("150"),
                                Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO),
                                BigDecimal.ZERO
                        ),
                        new SummaryDto.SummaryDtoRow(
                                days[2], BigDecimal.ZERO,
                                Arrays.asList(new BigDecimal("200"), new BigDecimal("50")),
                                new BigDecimal("250"),
                                Arrays.asList(new BigDecimal("-100"), BigDecimal.ZERO, BigDecimal.ZERO),
                                new BigDecimal("-100")
                        ),
                        new SummaryDto.SummaryDtoRow(
                                days[3], BigDecimal.ZERO,
                                Arrays.asList(new BigDecimal("125"), new BigDecimal("75")),
                                new BigDecimal("200"),
                                Arrays.asList(BigDecimal.ZERO, new BigDecimal("25"), new BigDecimal("25")),
                                new BigDecimal("50")
                        ),
                        new SummaryDto.SummaryDtoRow(
                                days[4], BigDecimal.ZERO,
                                Arrays.asList(new BigDecimal("50"), new BigDecimal("50")),
                                new BigDecimal("100"),
                                Arrays.asList(BigDecimal.ZERO, new BigDecimal("100"), BigDecimal.ZERO),
                                new BigDecimal("100")
                        ),
                        new SummaryDto.SummaryDtoRow(
                                days[5], new BigDecimal("100"),
                                Arrays.asList(new BigDecimal("50"), new BigDecimal("150")),
                                new BigDecimal("200"),
                                Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO),
                                BigDecimal.ZERO
                        )
                ),
                Arrays.asList(new BigDecimal("-100"), new BigDecimal("125"), new BigDecimal("25")),
                new BigDecimal("50")
        );

        SummaryDto actual = summaryService.getSummaryDto(accountIdSet, days[1], days[5]);

        assertSummaryDtoEquals(expected, actual);
    }

    /**
     * Amounts of two operations with tho different categories in two different days are not summed
     *
     * @throws Exception - exception
     */
    @Test
    public void testGetAmounts_TwoOperationsWithTwoCategoriesInTwoDifferentDays() throws Exception {
        Integer accountId = 10;
        Integer categoryId1 = 20;
        Integer categoryId2 = 21;
        List<OperationEntity> operationEntityList = Arrays.asList(
                new OperationEntity(
                        1, accountId, categoryId1, LocalDate.of(2017, 6, 25), new BigDecimal("20.0"), "note1"),
                new OperationEntity(
                        2, accountId, categoryId2, LocalDate.of(2017, 6, 26), new BigDecimal("30.0"), "note2")
        );
        LocalDate firstDay = LocalDate.of(2017, 6, 24);
        LocalDate lastDay = LocalDate.of(2017, 6, 27);
        Map<Integer, Integer> categoryIndexes = new HashMap<>();
        categoryIndexes.put(categoryId1, 0);
        categoryIndexes.put(categoryId2, 1);

        List<List<BigDecimal>> expected = Arrays.asList(
                Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO),
                Arrays.asList(new BigDecimal("20.0"), BigDecimal.ZERO),
                Arrays.asList(BigDecimal.ZERO, new BigDecimal("30.0")),
                Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO)
        );

        List<List<BigDecimal>> actual = summaryService
                .getAmounts(operationEntityList, firstDay, lastDay, categoryIndexes);

        assertEquals(expected, actual);
    }

    /**
     * Amounts of two operations with tho different categories in one day are not summed
     *
     * @throws Exception - exception
     */
    @Test
    public void testGetAmounts_TwoOperationsWithTwoCategoriesInOneDay() throws Exception {
        Integer accountId = 10;
        Integer categoryId1 = 20;
        Integer categoryId2 = 21;
        List<OperationEntity> operationEntityList = Arrays.asList(
                new OperationEntity(
                        1, accountId, categoryId1, LocalDate.of(2017, 6, 25), new BigDecimal("20.0"), "note1"),
                new OperationEntity(
                        2, accountId, categoryId2, LocalDate.of(2017, 6, 25), new BigDecimal("30.0"), "note2")
        );
        LocalDate firstDay = LocalDate.of(2017, 6, 24);
        LocalDate lastDay = LocalDate.of(2017, 6, 26);
        Map<Integer, Integer> categoryIndexes = new HashMap<>();
        categoryIndexes.put(categoryId1, 0);
        categoryIndexes.put(categoryId2, 1);

        List<List<BigDecimal>> expected = Arrays.asList(
                Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO),
                Arrays.asList(new BigDecimal("20.0"), new BigDecimal("30.0")),
                Arrays.asList(BigDecimal.ZERO, BigDecimal.ZERO)
        );

        List<List<BigDecimal>> actual = summaryService
                .getAmounts(operationEntityList, firstDay, lastDay, categoryIndexes);

        assertEquals(expected, actual);
    }

    /**
     * Amounts of two operations with the same category in two different days are not summed
     *
     * @throws Exception - exception
     */
    @Test
    public void testGetAmounts_TwoOperationsWithOneCategoryInTwoDifferentDays() throws Exception {
        Integer accountId = 10;
        Integer categoryId1 = 20;
        List<OperationEntity> operationEntityList = Arrays.asList(
                new OperationEntity(
                        1, accountId, categoryId1, LocalDate.of(2017, 6, 25), new BigDecimal("20.0"), "note1"),
                new OperationEntity(
                        2, accountId, categoryId1, LocalDate.of(2017, 6, 26), new BigDecimal("30.0"), "note2")
        );
        LocalDate firstDay = LocalDate.of(2017, 6, 24);
        LocalDate lastDay = LocalDate.of(2017, 6, 27);
        Map<Integer, Integer> categoryIndexes = Collections.singletonMap(categoryId1, 0);

        List<List<BigDecimal>> expected = Arrays.asList(
                Collections.singletonList(BigDecimal.ZERO),
                Collections.singletonList(new BigDecimal("20.0")),
                Collections.singletonList(new BigDecimal("30.0")),
                Collections.singletonList(BigDecimal.ZERO)
        );

        List<List<BigDecimal>> actual = summaryService
                .getAmounts(operationEntityList, firstDay, lastDay, categoryIndexes);

        assertEquals(expected, actual);
    }

    /**
     * Amounts of two operations with the same category and day are summed
     *
     * @throws Exception - exception
     */
    @Test
    public void testGetAmounts_TwoOperationsWithOneCategoryInOneDay() throws Exception {
        Integer accountId = 10;
        Integer categoryId1 = 20;
        List<OperationEntity> operationEntityList = Arrays.asList(
                new OperationEntity(
                        1, accountId, categoryId1, LocalDate.of(2017, 6, 25), new BigDecimal("20.0"), "note1"),
                new OperationEntity(
                        2, accountId, categoryId1, LocalDate.of(2017, 6, 25), new BigDecimal("30.0"), "note2")
        );
        LocalDate firstDay = LocalDate.of(2017, 6, 24);
        LocalDate lastDay = LocalDate.of(2017, 6, 26);
        Map<Integer, Integer> categoryIndexes = Collections.singletonMap(categoryId1, 0);

        List<List<BigDecimal>> expected = Arrays.asList(
                Collections.singletonList(BigDecimal.ZERO),
                Collections.singletonList(new BigDecimal("50.0")),
                Collections.singletonList(BigDecimal.ZERO)
        );

        List<List<BigDecimal>> actual = summaryService
                .getAmounts(operationEntityList, firstDay, lastDay, categoryIndexes);

        assertEquals(expected, actual);
    }

    /**
     * No operations result in empty list
     *
     * @throws Exception - exception
     */
    @Test
    public void testGetAmounts_NoOperations() throws Exception {
        List<OperationEntity> operationEntityList = Collections.emptyList();
        LocalDate firstDay = LocalDate.of(2017, 6, 24);
        LocalDate lastDay = LocalDate.of(2017, 6, 26);
        Map<Integer, Integer> categoryIndexes = Collections.emptyMap();

        List<List<BigDecimal>> expected = Arrays.asList(
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        );

        List<List<BigDecimal>> actual = summaryService
                .getAmounts(operationEntityList, firstDay, lastDay, categoryIndexes);

        assertEquals(expected, actual);
    }

    /**
     * If no balance for the first day, but balance before first day exist, then that balance is taken for the first day
     *
     * @throws Exception - exception
     */
    @Test
    public void testGetBalances_BalanceBeforeFirstDayNoBalanceAtFirstDay() throws Exception {
        Integer accountId = 10;
        List<BalanceEntity> initialBalanceEntityList = Collections.singletonList(
                new BalanceEntity(accountId, LocalDate.of(2017, 6, 22), new BigDecimal("100.0"), false)
        );
        List<BalanceEntity> balanceEntityList = Arrays.asList(
                new BalanceEntity(accountId, LocalDate.of(2017, 6, 25), new BigDecimal("150.0"), false),
                new BalanceEntity(accountId, LocalDate.of(2017, 6, 27), new BigDecimal("50.0"), false)
        );
        LocalDate firstDay = LocalDate.of(2017, 6, 24);
        LocalDate lastDay = LocalDate.of(2017, 6, 28);
        Map<Integer, Integer> accountIndexes = Collections.singletonMap(accountId, 0);

        List<List<BigDecimal>> expected = Arrays.asList(
                Collections.singletonList(new BigDecimal("100.0")),
                Collections.singletonList(new BigDecimal("150.0")),
                Collections.singletonList(new BigDecimal("150.0")),
                Collections.singletonList(new BigDecimal("50.0")),
                Collections.singletonList(new BigDecimal("50.0"))
        );

        List<List<BigDecimal>> actual = summaryService
                .getBalances(initialBalanceEntityList, balanceEntityList, firstDay, lastDay, accountIndexes);

        assertEquals(expected, actual);
    }

    /**
     * If balance for the first day exists, then that balance is taken for the first day
     *
     * @throws Exception - exception
     */
    @Test
    public void testGetBalances_NoBalanceBeforeFirstDayBalanceAtFirstDay() throws Exception {
        Integer accountId = 1;
        List<BalanceEntity> initialBalanceEntityList = Collections.emptyList();
        List<BalanceEntity> balanceEntityList = Arrays.asList(
                new BalanceEntity(accountId, LocalDate.of(2017, 6, 24), new BigDecimal("200.0"), false),
                new BalanceEntity(accountId, LocalDate.of(2017, 6, 25), new BigDecimal("150.0"), false),
                new BalanceEntity(accountId, LocalDate.of(2017, 6, 27), new BigDecimal("50.0"), false)
        );
        LocalDate firstDay = LocalDate.of(2017, 6, 24);
        LocalDate lastDay = LocalDate.of(2017, 6, 28);
        Map<Integer, Integer> accountIndexes = Collections.singletonMap(accountId, 0);

        List<List<BigDecimal>> expected = Arrays.asList(
                Collections.singletonList(new BigDecimal("200.0")),
                Collections.singletonList(new BigDecimal("150.0")),
                Collections.singletonList(new BigDecimal("150.0")),
                Collections.singletonList(new BigDecimal("50.0")),
                Collections.singletonList(new BigDecimal("50.0"))
        );

        List<List<BigDecimal>> actual = summaryService
                .getBalances(initialBalanceEntityList, balanceEntityList, firstDay, lastDay, accountIndexes);

        assertEquals(expected, actual);
    }

    /**
     * If no balance for the first day and no balance before, then zero is taken for the first day
     *
     * @throws Exception - exception
     */
    @Test
    public void testGetBalances_NoBalanceBeforeFirstDayNoBalanceAtFirstDay() throws Exception {
        Integer accountId = 1;
        List<BalanceEntity> initialBalanceEntityList = Collections.emptyList();
        List<BalanceEntity> balanceEntityList = Arrays.asList(
                new BalanceEntity(accountId, LocalDate.of(2017, 6, 25), new BigDecimal("150.0"), false),
                new BalanceEntity(accountId, LocalDate.of(2017, 6, 27), new BigDecimal("50.0"), false)
        );
        LocalDate firstDay = LocalDate.of(2017, 6, 24);
        LocalDate lastDay = LocalDate.of(2017, 6, 28);
        Map<Integer, Integer> accountIndexes = Collections.singletonMap(accountId, 0);

        List<List<BigDecimal>> expected = Arrays.asList(
                Collections.singletonList(BigDecimal.ZERO),
                Collections.singletonList(new BigDecimal("150.0")),
                Collections.singletonList(new BigDecimal("150.0")),
                Collections.singletonList(new BigDecimal("50.0")),
                Collections.singletonList(new BigDecimal("50.0"))
        );

        List<List<BigDecimal>> actual = summaryService
                .getBalances(initialBalanceEntityList, balanceEntityList, firstDay, lastDay, accountIndexes);

        assertEquals(expected, actual);
    }

    /**
     * Setting accuracy is valid
     *
     * @throws Exception - exception
     */
    @Test
    public void testSetAccuracy() throws Exception {
        List<SummaryDto.SummaryDtoRow> expectedSummaryDtoRowList = Arrays.asList(
                new SummaryDto.SummaryDtoRow(
                        LocalDate.of(2017, 7, 1), new BigDecimal("10.0"),
                        Arrays.asList(new BigDecimal("10.5"), new BigDecimal("10.4")), new BigDecimal("20.9"),
                        Arrays.asList(new BigDecimal("0.0"), new BigDecimal("9.0")), new BigDecimal("9.0")
                ),
                new SummaryDto.SummaryDtoRow(
                        LocalDate.of(2017, 7, 2), new BigDecimal("0.0"),
                        Arrays.asList(new BigDecimal("10.2"), new BigDecimal("10.4")), new BigDecimal("20.9"),
                        Arrays.asList(new BigDecimal("0.3"), new BigDecimal("0.0")), new BigDecimal("0.3")
                )
        );
        List<BigDecimal> expectedTotalAmounts = Arrays.asList(new BigDecimal("0.3"), new BigDecimal("9.0"));
        BigDecimal expectedTotalAmountsSummary = new BigDecimal("9.3");
        List<SummaryDto.SummaryDtoRow> actualSummaryDtoRowList = Arrays.asList(
                new SummaryDto.SummaryDtoRow(
                        LocalDate.of(2017, 7, 1), new BigDecimal("10"),
                        Arrays.asList(new BigDecimal("10.45"), new BigDecimal("10.44")), new BigDecimal("20.89"),
                        Arrays.asList(BigDecimal.ZERO, new BigDecimal("9")), new BigDecimal("9")
                ),
                new SummaryDto.SummaryDtoRow(
                        LocalDate.of(2017, 7, 2), BigDecimal.ZERO,
                        Arrays.asList(new BigDecimal("10.15"), new BigDecimal("10.44")), new BigDecimal("20.89"),
                        Arrays.asList(new BigDecimal("0.3"), BigDecimal.ZERO), new BigDecimal("0.3")
                )
        );
        List<BigDecimal> actualTotalAmounts = Arrays.asList(new BigDecimal("0.3"), new BigDecimal("9"));
        SummaryDto actual =
                new SummaryDto(null, null, actualSummaryDtoRowList, actualTotalAmounts, new BigDecimal("9.3"));
        summaryService.setAccuracy(actual, 1);
        for (int index = 0; index < expectedSummaryDtoRowList.size(); index++) {
            SummaryDto.SummaryDtoRow expectedSummaryDtoRow = expectedSummaryDtoRowList.get(index);
            SummaryDto.SummaryDtoRow actualSummaryDtoRow = actualSummaryDtoRowList.get(index);
            // Using assertEquals for BigDecimal to verify accuracy
            assertEquals(expectedSummaryDtoRow.getDifference(), actualSummaryDtoRow.getDifference());
            assertEquals(expectedSummaryDtoRow.getBalances(), actualSummaryDtoRow.getBalances());
            assertEquals(expectedSummaryDtoRow.getBalancesSummary(), actualSummaryDtoRow.getBalancesSummary());
            assertEquals(expectedSummaryDtoRow.getAmounts(), actualSummaryDtoRow.getAmounts());
            assertEquals(expectedSummaryDtoRow.getAmountsSummary(), actualSummaryDtoRow.getAmountsSummary());
        }
        assertEquals(expectedTotalAmounts, actualTotalAmounts);
        assertEquals(expectedTotalAmountsSummary, actual.getTotalAmountsSummary());
    }

}
