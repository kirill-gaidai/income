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
            assertEquals("Id is null", e.getMessage());
        }

        verifyNoMoreDaoInteractions();
    }

    @Test
    public void testNotFound() throws Exception {
        Integer currencyId = 1;

        try {
            service.get(currencyId);
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Currency with id %d not found", currencyId), e.getMessage());
        }

        verify(currencyDao).get(currencyId);

        verifyNoMoreDaoInteractions();
    }

    @Test
    public void testGetOk() throws Exception {
        Integer currencyId = 1;

        CurrencyEntity entity = new CurrencyEntity(currencyId, "01", "category1", 2);
        CurrencyDto expected = new CurrencyDto(currencyId, "01", "category1", 2);

        doReturn(entity).when(currencyDao).get(currencyId);

        CurrencyDto actual = service.get(currencyId);
        assertEntityEquals(expected, actual);

        verify(currencyDao).get(currencyId);

        verifyNoMoreDaoInteractions();
    }

}
