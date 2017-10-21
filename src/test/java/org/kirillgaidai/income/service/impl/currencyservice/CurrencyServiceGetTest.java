package org.kirillgaidai.income.service.impl.currencyservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.impl.CurrencyDao;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.service.converter.CurrencyConverter;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.CurrencyDto;
import org.kirillgaidai.income.service.exception.IncomeServiceCurrencyNotFoundException;
import org.kirillgaidai.income.service.impl.CurrencyService;
import org.kirillgaidai.income.service.intf.ICurrencyService;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class CurrencyServiceGetTest {

    final private ICurrencyDao dao = mock(CurrencyDao.class);
    final private IGenericConverter<CurrencyEntity, CurrencyDto> converter = mock(CurrencyConverter.class);
    final private ICurrencyService service = new CurrencyService(dao, converter);

    @Test
    public void testNull() throws Exception {
        try {
            service.get(null);
        } catch (IllegalArgumentException e) {
            assertEquals("null", e.getMessage());
        }
        verifyNoMoreInteractions(dao, converter);
    }

    @Test
    public void testNotFound() throws Exception {
        try {
            service.get(1);
        } catch (IncomeServiceCurrencyNotFoundException e) {
            assertEquals("Currency with id 1 not found", e.getMessage());
        }
        verify(dao).get(1);
        verifyNoMoreInteractions(dao, converter);
    }

    @Test
    public void testGetOk() throws Exception {
        CurrencyEntity entity = new CurrencyEntity(1, "01", "category1", 2);
        CurrencyDto expected = new CurrencyDto(1, "01", "category1", 2);

        doReturn(entity).when(dao).get(1);
        doReturn(expected).when(converter).convertToDto(entity);

        CurrencyDto actual = service.get(1);
        assertEntityEquals(expected, actual);

        verify(dao).get(1);
        verify(converter).convertToDto(entity);
        verifyNoMoreInteractions(dao, converter);
    }

}
