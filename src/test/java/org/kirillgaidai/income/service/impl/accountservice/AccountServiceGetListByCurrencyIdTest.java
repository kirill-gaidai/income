package org.kirillgaidai.income.service.impl.accountservice;

import org.junit.Test;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.kirillgaidai.income.utils.TestUtils;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.throwUnreachableException;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class AccountServiceGetListByCurrencyIdTest extends AccountServiceBaseTest {

    /**
     * Test currency id is null
     *
     * @throws Exception exception
     */
    @Test
    public void testNull() throws Exception {
        try {
            service.getList((Integer) null);
            throwUnreachableException();
        } catch (IllegalArgumentException e) {
            assertEquals("Currency id is null", e.getMessage());
        }

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test currency not found
     *
     * @throws Exception exception
     */
    @Test
    public void testCurrencyNotFound() throws Exception {
        Integer currencyId = 1;

        doReturn(null).when(currencyDao).get(currencyId);

        try {
            service.getList(currencyId);
            throwUnreachableException();
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Currency with id %d not found", currencyId), e.getMessage());
        }

        verify(currencyDao).get(currencyId);

        verifyNoMoreDaoInteractions();
    }

}
