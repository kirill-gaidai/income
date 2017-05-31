package org.kirillgaidai.income.service.converter;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.service.dto.CurrencyDto;

import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertCurrencyEntityEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertCurrencyDtoEquals;

public class CurrencyConverterTest {

    private IGenericConverter<CurrencyEntity, CurrencyDto> converter = new CurrencyConverter();

    @Test
    public void testConvertToDto_Ok() throws Exception {
        CurrencyEntity entity = new CurrencyEntity(1, "cc1", "currency1");
        CurrencyDto expected = new CurrencyDto(1, "cc1", "currency1");
        CurrencyDto actual = converter.convertToDto(entity);
        assertCurrencyDtoEquals(expected, actual);
    }

    @Test
    public void testConvertToDto_Null() throws Exception {
        CurrencyDto actual = converter.convertToDto(null);
        assertNull(actual);
    }

    @Test
    public void testConvertToEntity_Ok() throws Exception {
        CurrencyDto dto = new CurrencyDto(1, "cc1", "currency1");
        CurrencyEntity expected = new CurrencyEntity(1, "cc1", "currency1");
        CurrencyEntity actual = converter.convertToEntity(dto);
        assertCurrencyEntityEquals(expected, actual);
    }

    @Test
    public void testConvertToEntity_Null() throws Exception {
        CurrencyEntity actual = converter.convertToEntity(null);
        assertNull(actual);
    }

}
