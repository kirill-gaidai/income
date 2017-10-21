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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class CurrencyServiceDeleteTest {

    final private ICurrencyDao dao = mock(CurrencyDao.class);
    final private IGenericConverter<CurrencyEntity, CurrencyDto> converter = mock(CurrencyConverter.class);
    final private ICurrencyService service = new CurrencyService(dao, converter);

    @Test
    public void testNull() throws Exception {
        try {
            service.delete(null);
        } catch (IllegalArgumentException e) {
            assertEquals("null", e.getMessage());
        }
        verifyNoMoreInteractions(dao, converter);
    }

    @Test
    public void testNotFound() throws Exception {
        doReturn(0).when(dao).delete(1);
        try {
            service.delete(1);
        } catch (IncomeServiceCurrencyNotFoundException e) {
            assertEquals("Currency with id 1 not found", e.getMessage());
        }
        verify(dao).delete(1);
        verifyNoMoreInteractions(dao, converter);
    }

    @Test
    public void testOk() throws Exception {
        doReturn(1).when(dao).delete(1);
        service.delete(1);
        verify(dao).delete(1);
        verifyNoMoreInteractions(dao, converter);
    }

}
