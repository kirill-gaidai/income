package org.kirillgaidai.income.service.impl.currencyservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.impl.CurrencyDao;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.exception.IncomeException;
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

public class CurrencyServiceSaveTest {

    final private ICurrencyDao dao = mock(CurrencyDao.class);
    final private IGenericConverter<CurrencyEntity, CurrencyDto> converter = mock(CurrencyConverter.class);
    final private ICurrencyService service = new CurrencyService(dao, converter);

    @Test
    public void testNull() throws Exception {
        try {
            service.save(null);
        } catch (IllegalArgumentException e) {
            assertEquals("null", e.getMessage());
        }
        verifyNoMoreInteractions(dao, converter);
    }

    @Test
    public void testInsert() throws Exception {
        CurrencyDto dto = new CurrencyDto(null, "01", "category1", 2);
        CurrencyEntity entity = new CurrencyEntity(null, "01", "category1", 2);
        CurrencyDto expected = new CurrencyDto(1, "01", "category1", 2);

        doReturn(entity).when(converter).convertToEntity(dto);
        doReturn(expected).when(converter).convertToDto(entity);
        doReturn(1).when(dao).insert(entity);

        CurrencyDto actual = service.save(dto);
        assertEntityEquals(expected, actual);

        verify(converter).convertToEntity(dto);
        verify(converter).convertToDto(entity);
        verify(dao).insert(entity);
        verifyNoMoreInteractions(dao, converter);
    }

    @Test
    public void testUpdate() throws Exception {
        CurrencyDto dto = new CurrencyDto(1, "01", "category1", 2);
        CurrencyEntity entity = new CurrencyEntity(1, "01", "category1", 2);
        CurrencyDto expected =  new CurrencyDto(1, "01", "category1", 2);

        doReturn(entity).when(converter).convertToEntity(dto);
        doReturn(expected).when(converter).convertToDto(entity);
        doReturn(1).when(dao).update(entity);

        CurrencyDto actual = service.save(dto);
        assertEntityEquals(expected, actual);

        verify(converter).convertToEntity(dto);
        verify(converter).convertToDto(entity);
        verify(dao).update(entity);
        verifyNoMoreInteractions(dao, converter);
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        CurrencyDto dto = new CurrencyDto(1, "01", "category1", 2);
        CurrencyEntity entity = new CurrencyEntity(1, "01", "category1", 2);

        doReturn(entity).when(converter).convertToEntity(dto);
        doReturn(0).when(dao).update(entity);

        try {
            service.save(dto);
        } catch (IncomeException e) {
            assertEquals("Dto isn't inserted or updated", e.getMessage());
        }

        verify(converter).convertToEntity(dto);
        verify(dao).update(entity);
        verifyNoMoreInteractions(dao, converter);
    }

}
