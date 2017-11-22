package org.kirillgaidai.income.service.utils;

import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.dto.BalanceDto;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.dto.CurrencyDto;
import org.kirillgaidai.income.service.dto.OperationDto;
import org.kirillgaidai.income.service.dto.RateDto;
import org.kirillgaidai.income.service.dto.SummaryDto;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertBigDecimalEquals;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertBigDecimalListEquals;

public class ServiceTestUtils {

    public ServiceTestUtils() {
        throw new UnsupportedOperationException();
    }

    public static void assertAccountDtoEquals(AccountDto expected, AccountDto actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCurrencyId(), actual.getCurrencyId());
        assertEquals(expected.getCurrencyCode(), actual.getCurrencyCode());
        assertEquals(expected.getCurrencyTitle(), actual.getCurrencyTitle());
        assertEquals(expected.getSort(), actual.getSort());
        assertEquals(expected.getTitle(), actual.getTitle());
    }

    public static void assertAccountDtoListEquals(List<AccountDto> expected, List<AccountDto> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertAccountDtoEquals(expected.get(index), actual.get(index));
        }
    }

    public static void assertBalanceDtoEquals(BalanceDto expected, BalanceDto actual) {
        assertNotNull(actual);
        assertEquals(expected.getAccountId(), actual.getAccountId());
        assertEquals(expected.getAccountTitle(), actual.getAccountTitle());
        assertEquals(expected.getDay(), actual.getDay());
        assertBigDecimalEquals(expected.getAmount(), actual.getAmount());
        assertEquals(expected.getManual(), actual.getManual());
    }

    public static void assertCategoryDtoEquals(CategoryDto expected, CategoryDto actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getSort(), actual.getSort());
        assertEquals(expected.getTitle(), actual.getTitle());
    }

    public static void assertCategoryDtoListEquals(List<CategoryDto> expected, List<CategoryDto> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertCategoryDtoEquals(expected.get(index), actual.get(index));
        }
    }

    public static void assertCurrencyDtoEquals(CurrencyDto expected, CurrencyDto actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getAccuracy(), actual.getAccuracy());
    }

    public static void assertCurrencyDtoListEquals(List<CurrencyDto> expected, List<CurrencyDto> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertCurrencyDtoEquals(expected.get(index), actual.get(index));
        }
    }

    public static void assertOperationDtoEquals(OperationDto expected, OperationDto actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getAccountId(), actual.getAccountId());
        assertEquals(expected.getAccountTitle(), actual.getAccountTitle());
        assertEquals(expected.getCategoryId(), actual.getCategoryId());
        assertEquals(expected.getCategoryTitle(), actual.getCategoryTitle());
        assertEquals(expected.getDay(), actual.getDay());
        assertBigDecimalEquals(expected.getAmount(), actual.getAmount());
        assertEquals(expected.getNote(), actual.getNote());
    }

    public static void assertOperationDtoListEquals(List<OperationDto> expected, List<OperationDto> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertOperationDtoEquals(expected.get(index), actual.get(index));
        }
    }

    public static void assertSummaryDtoRowEquals(SummaryDto.SummaryDtoRow expected, SummaryDto.SummaryDtoRow actual) {
        assertEquals(expected.getDay(), actual.getDay());
        assertBigDecimalEquals(expected.getDifference(), actual.getDifference());
        assertBigDecimalEquals(expected.getBalancesSummary(), actual.getBalancesSummary());
        assertBigDecimalEquals(expected.getAmountsSummary(), actual.getAmountsSummary());
        assertBigDecimalListEquals(expected.getBalances(), actual.getBalances());
        assertBigDecimalListEquals(expected.getAmounts(), actual.getAmounts());
    }

    public static void assertSummaryDtoEquals(SummaryDto expected, SummaryDto actual) {
        assertAccountDtoListEquals(expected.getAccountDtoList(), actual.getAccountDtoList());
        assertCategoryDtoListEquals(expected.getCategoryDtoList(), actual.getCategoryDtoList());
        List<SummaryDto.SummaryDtoRow> expectedRows = expected.getSummaryDtoRowList();
        List<SummaryDto.SummaryDtoRow> actualRows = actual.getSummaryDtoRowList();
        for (int index = 0; index < expectedRows.size(); index++) {
            assertSummaryDtoRowEquals(expectedRows.get(index), actualRows.get(index));
        }
        assertBigDecimalListEquals(expected.getTotalAmounts(), actual.getTotalAmounts());
        assertBigDecimalEquals(expected.getTotalAmountsSummary(), actual.getTotalAmountsSummary());
    }

    public static void assertRateDtoEquals(RateDto expected, RateDto actual) {
        assertNotNull(actual);
        assertEquals(expected.getCurrencyIdFrom(), actual.getCurrencyIdFrom());
        assertEquals(expected.getCurrencyCodeFrom(), actual.getCurrencyCodeFrom());
        assertEquals(expected.getCurrencyTitleFrom(), actual.getCurrencyTitleFrom());
        assertEquals(expected.getCurrencyIdTo(), actual.getCurrencyIdTo());
        assertEquals(expected.getCurrencyCodeTo(), actual.getCurrencyCodeTo());
        assertEquals(expected.getCurrencyTitleTo(), actual.getCurrencyTitleTo());
        assertEquals(expected.getDay(), actual.getDay());
        assertBigDecimalEquals(expected.getValue(), actual.getValue());
    }

    public static void assertRateDtoListEquals(List<RateDto> expected, List<RateDto> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertRateDtoEquals(expected.get(index), actual.get(index));
        }
    }

}
