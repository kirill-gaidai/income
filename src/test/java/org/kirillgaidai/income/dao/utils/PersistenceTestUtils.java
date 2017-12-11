package org.kirillgaidai.income.dao.utils;

import org.kirillgaidai.income.dao.entity.RateEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PersistenceTestUtils {

    public PersistenceTestUtils() {
        throw new UnsupportedOperationException();
    }

    public static void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual) {
        int expectedScale = expected.scale();
        int actualScale = actual.scale();
        if (expectedScale < actualScale) {
            expected = expected.setScale(actualScale, RoundingMode.HALF_UP);
        } else if (actualScale < expectedScale) {
            actual = actual.setScale(expectedScale, RoundingMode.HALF_UP);
        }
        assertEquals(expected, actual);
    }

    public static void assertRateEntityListEquals(List<RateEntity> expected, List<RateEntity> actual) {
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        for (int index = 0; index < expected.size(); index++) {
            assertRateEntityEquals(expected.get(index), actual.get(index));
        }
    }

    public static void assertRateEntityEquals(RateEntity expected, RateEntity actual) {
        assertNotNull(actual);
        assertEquals(expected.getCurrencyIdFrom(), actual.getCurrencyIdFrom());
        assertEquals(expected.getCurrencyIdTo(), actual.getCurrencyIdTo());
        assertEquals(expected.getDay(), actual.getDay());
        assertBigDecimalEquals(expected.getValue(), actual.getValue());
    }

}
