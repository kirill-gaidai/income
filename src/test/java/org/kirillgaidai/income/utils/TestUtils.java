package org.kirillgaidai.income.utils;

import java.lang.reflect.Method;
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
            if (methodName.startsWith("get") && !"getClass".equals(methodName)) {
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

}
