package org.kirillgaidai.income.service.impl.currencyservice;

import org.junit.Test;
import org.kirillgaidai.income.service.exception.IncomeServiceCurrencyNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class CurrencyServiceDeleteTest extends CurrencyServiceBaseTest{

    @Test
    public void testNull() throws Exception {
        try {
            service.delete(null);
        } catch (IllegalArgumentException e) {
            assertEquals("null", e.getMessage());
        }
        verifyNoMoreInteractions();
    }

    @Test
    public void testNotFound() throws Exception {
        doReturn(0).when(currencyDao).delete(1);
        try {
            service.delete(1);
        } catch (IncomeServiceCurrencyNotFoundException e) {
            assertEquals("Currency with id 1 not found", e.getMessage());
        }
        verify(currencyDao).delete(1);
        verifyNoMoreInteractions();
    }

    @Test
    public void testOk() throws Exception {
        doReturn(1).when(currencyDao).delete(1);
        service.delete(1);
        verify(currencyDao).delete(1);
        verifyNoMoreInteractions();
    }

}
