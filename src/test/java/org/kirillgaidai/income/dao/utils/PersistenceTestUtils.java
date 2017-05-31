package org.kirillgaidai.income.dao.utils;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PersistenceTestUtils {

    public PersistenceTestUtils() {
        throw new UnsupportedOperationException();
    }

    public static void assertAccountEntityListEquals(List<AccountEntity> expected, List<AccountEntity> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertAccountEntityEquals(expected.get(index), actual.get(index));
        }
    }

    public static void assertAccountEntityEquals(AccountEntity expected, AccountEntity actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCurrencyId(), actual.getCurrencyId());
        assertEquals(expected.getSort(), actual.getSort());
        assertEquals(expected.getTitle(), actual.getTitle());
    }

    public static void assertCategoryEntityListEquals(List<CategoryEntity> expected, List<CategoryEntity> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertCategoryEntityEquals(expected.get(index), actual.get(index));
        }
    }

    public static void assertCategoryEntityEquals(CategoryEntity expected, CategoryEntity actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getSort(), actual.getSort());
        assertEquals(expected.getTitle(), actual.getTitle());
    }

    public static void assertCurrencyEntityListEquals(List<CurrencyEntity> expected, List<CurrencyEntity> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertCurrencyEntityEquals(expected.get(index), actual.get(index));
        }
    }

    public static void assertCurrencyEntityEquals(CurrencyEntity expected, CurrencyEntity actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCode(), actual.getCode());
        assertEquals(expected.getTitle(), actual.getTitle());
    }

    public static void assertOperationEntityListEquals(List<OperationEntity> expected, List<OperationEntity> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertOperationEntityEquals(expected.get(index), actual.get(index));
        }
    }

    public static void assertOperationEntityEquals(OperationEntity expected, OperationEntity actual) {
        assertNotNull(actual);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getAccountId(), actual.getAccountId());
        assertEquals(expected.getCategoryId(), actual.getCategoryId());
        assertEquals(expected.getDay(), actual.getDay());
        assertEquals(0, expected.getAmount().compareTo(actual.getAmount()));
        assertEquals(expected.getNote(), actual.getNote());
    }

    public static void assertBalanceEntityListEquals(List<BalanceEntity> expected, List<BalanceEntity> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertBalanceEntityEquals(expected.get(index), actual.get(index));
        }
    }

    public static void assertBalanceEntityEquals(BalanceEntity expected, BalanceEntity actual) {
        assertNotNull(actual);
        assertEquals(expected.getAccountId(), actual.getAccountId());
        assertEquals(expected.getDay(), actual.getDay());
        assertEquals(0, expected.getAmount().compareTo(actual.getAmount()));
        assertEquals(expected.getManual(), actual.getManual());
    }

}
