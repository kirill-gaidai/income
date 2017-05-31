package org.kirillgaidai.income.service.utils;

import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.dto.BalanceDto;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.dto.CurrencyDto;
import org.kirillgaidai.income.service.dto.OperationDto;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
        assertEquals(expected.getAmount(), actual.getAmount());
        assertEquals(expected.getManual(), actual.getManual());
    }

    public static void assertBalanceDtoListEquals(List<BalanceDto> expected, List<BalanceDto> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertBalanceDtoEquals(expected.get(index), actual.get(index));
        }
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
        assertEquals(expected.getAmount(), actual.getAmount());
        assertEquals(expected.getNote(), actual.getNote());
    }

    public static void assertOperationDtoListEquals(List<OperationDto> expected, List<OperationDto> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertOperationDtoEquals(expected.get(index), actual.get(index));
        }
    }

}
