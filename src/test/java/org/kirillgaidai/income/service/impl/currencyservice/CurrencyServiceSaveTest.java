package org.kirillgaidai.income.service.impl.currencyservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.exception.IncomeException;
import org.kirillgaidai.income.service.dto.CurrencyDto;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class CurrencyServiceSaveTest extends CurrencyServiceBaseTest {

    @Test
    public void testNull() throws Exception {
        try {
            service.save(null);
        } catch (IllegalArgumentException e) {
            assertEquals("null", e.getMessage());
        }
        verifyNoMoreInteractions();
    }

    @Test
    public void testInsert() throws Exception {
        CurrencyDto dto = new CurrencyDto(null, "01", "category1", 2);
        CurrencyEntity entity = new CurrencyEntity(null, "01", "category1", 2);
        CurrencyDto expected = new CurrencyDto(1, "01", "category1", 2);

        doReturn(entity).when(converter).convertToEntity(dto);
        doReturn(expected).when(converter).convertToDto(entity);
        doReturn(1).when(currencyDao).insert(entity);

        CurrencyDto actual = service.save(dto);
        assertEntityEquals(expected, actual);

        verify(converter).convertToEntity(dto);
        verify(converter).convertToDto(entity);
        verify(currencyDao).insert(entity);
        verifyNoMoreInteractions();
    }

    @Test
    public void testUpdate() throws Exception {
        CurrencyDto dto = new CurrencyDto(1, "01", "category1", 2);
        CurrencyEntity entity = new CurrencyEntity(1, "01", "category1", 2);
        CurrencyDto expected =  new CurrencyDto(1, "01", "category1", 2);

        doReturn(entity).when(converter).convertToEntity(dto);
        doReturn(expected).when(converter).convertToDto(entity);
        doReturn(1).when(currencyDao).update(entity);

        CurrencyDto actual = service.save(dto);
        assertEntityEquals(expected, actual);

        verify(converter).convertToEntity(dto);
        verify(converter).convertToDto(entity);
        verify(currencyDao).update(entity);
        verifyNoMoreInteractions();
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        CurrencyDto dto = new CurrencyDto(1, "01", "category1", 2);
        CurrencyEntity entity = new CurrencyEntity(1, "01", "category1", 2);

        doReturn(entity).when(converter).convertToEntity(dto);
        doReturn(0).when(currencyDao).update(entity);

        try {
            service.save(dto);
        } catch (IncomeException e) {
            assertEquals("Dto isn't inserted or updated", e.getMessage());
        }

        verify(converter).convertToEntity(dto);
        verify(currencyDao).update(entity);
        verifyNoMoreInteractions();
    }

}
