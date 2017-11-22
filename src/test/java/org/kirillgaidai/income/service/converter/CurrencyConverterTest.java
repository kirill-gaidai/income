package org.kirillgaidai.income.service.converter;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.service.dto.CurrencyDto;

import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;

public class CurrencyConverterTest {

    final private IGenericConverter<CurrencyEntity, CurrencyDto> converter = new CurrencyConverter();

    @Test
    public void testConvertToDto_Ok() throws Exception {
        CurrencyEntity entity = new CurrencyEntity(1, "cc1", "currency1", 2);
        CurrencyDto expected = new CurrencyDto(1, "cc1", "currency1", 2);
        CurrencyDto actual = converter.convertToDto(entity);
        assertEntityEquals(expected, actual);
    }

    @Test
    public void testConvertToDto_Null() throws Exception {
        CurrencyDto actual = converter.convertToDto(null);
        assertNull(actual);
    }

    @Test
    public void testConvertToEntity_Ok() throws Exception {
        CurrencyDto dto = new CurrencyDto(1, "cc1", "currency1", 2);
        CurrencyEntity expected = new CurrencyEntity(1, "cc1", "currency1", 2);
        CurrencyEntity actual = converter.convertToEntity(dto);
        assertEntityEquals(expected, actual);
    }

    @Test
    public void testConvertToEntity_Null() throws Exception {
        CurrencyEntity actual = converter.convertToEntity(null);
        assertNull(actual);
    }

}
