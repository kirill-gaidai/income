package org.kirillgaidai.income.service.utils;

import org.kirillgaidai.income.service.dto.RateDto;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertBigDecimalEquals;

public class ServiceTestUtils {

    public ServiceTestUtils() {
        throw new UnsupportedOperationException();
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
