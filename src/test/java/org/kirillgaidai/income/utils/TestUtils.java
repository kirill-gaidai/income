package org.kirillgaidai.income.utils;

import junit.framework.AssertionFailedError;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TestUtils {

    public TestUtils() {
        throw new UnsupportedOperationException();
    }

    public static <T> void assertEntityEquals(T expected, T actual) throws Exception {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getClass(), actual.getClass());
        for (Method method : expected.getClass().getMethods()) {
            String methodName = method.getName();
            if (!methodName.startsWith("get") || "getClass".equals(methodName)) {
                continue;
            }
            if (BigDecimal.class.equals(method.getReturnType())) {
                assertBigDecimalEquals((BigDecimal) method.invoke(expected), (BigDecimal) method.invoke(actual));
            } else {
                assertEquals(method.invoke(expected), method.invoke(actual));
            }
        }
    }

    public static <T extends List<?>> void assertEntityListEquals(T expected, T actual) throws Exception {
        assertNotNull(expected);
        assertNotNull(actual);
        int size = expected.size();
        assertEquals(size, actual.size());
        for (int index = 0; index < size; index++) {
            assertEntityEquals(expected.get(index), actual.get(index));
        }
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

    public static void throwUnreachableException() {
        throw new AssertionFailedError("Unreachable point");
    }

}
