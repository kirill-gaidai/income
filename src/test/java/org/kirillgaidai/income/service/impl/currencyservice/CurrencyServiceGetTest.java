package org.kirillgaidai.income.service.impl.currencyservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.service.dto.CurrencyDto;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class CurrencyServiceGetTest extends CurrencyServiceBaseTest {

    @Test
    public void testNull() throws Exception {
        try {
            service.get(null);
        } catch (IllegalArgumentException e) {
            assertEquals("null", e.getMessage());
        }
        verifyNoMoreDaoInteractions();
    }

    @Test
    public void testNotFound() throws Exception {
        try {
            service.get(1);
        } catch (IncomeServiceNotFoundException e) {
            assertEquals("Currency with id 1 not found", e.getMessage());
        }
        verify(currencyDao).get(1);
        verifyNoMoreDaoInteractions();
    }

    @Test
    public void testGetOk() throws Exception {
        CurrencyEntity entity = new CurrencyEntity(1, "01", "category1", 2);
        CurrencyDto expected = new CurrencyDto(1, "01", "category1", 2);

        doReturn(entity).when(currencyDao).get(1);
        doReturn(expected).when(converter).convertToDto(entity);

        CurrencyDto actual = service.get(1);
        assertEntityEquals(expected, actual);

        verify(currencyDao).get(1);
        verify(converter).convertToDto(entity);
        verifyNoMoreDaoInteractions();
    }

}
