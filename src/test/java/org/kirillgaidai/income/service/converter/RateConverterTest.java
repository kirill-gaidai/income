package org.kirillgaidai.income.service.converter;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.RateEntity;
import org.kirillgaidai.income.service.dto.RateDto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertRateEntityEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertRateDtoEquals;

public class RateConverterTest {

    final private IGenericConverter<RateEntity, RateDto> converter = new RateConverter();

    @Test
    public void testConvertToDto_Ok() throws Exception {
        LocalDate day = LocalDate.of(2017, 8, 4);
        BigDecimal value = new BigDecimal("1.23");
        RateEntity entity = new RateEntity(1, 2, day, value);
        RateDto expected = new RateDto(1, null, null, 2, null, null, day, value);
        RateDto actual = converter.convertToDto(entity);
        assertRateDtoEquals(expected, actual);
    }

    @Test
    public void testConvertToDto_Null() throws Exception {
        RateDto actual = converter.convertToDto(null);
        assertNull(actual);
    }

    @Test
    public void testConvertToEntity_Ok() throws Exception {
        LocalDate day = LocalDate.of(2017, 8, 4);
        BigDecimal value = new BigDecimal("1.23");
        RateDto dto = new RateDto(1, "cc1", "currency1", 2, "cc2", "currency2", day, value);
        RateEntity expected = new RateEntity(1, 2, day, value);
        RateEntity actual = converter.convertToEntity(dto);
        assertRateEntityEquals(expected, actual);
    }

    @Test
    public void testConvertToEntity_Null() throws Exception {
        RateEntity actual = converter.convertToEntity(null);
        assertNull(actual);
    }

}
