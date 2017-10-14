package org.kirillgaidai.income.rest.mappers;

import org.junit.Test;
import org.kirillgaidai.income.rest.dto.currency.CurrencyCreateRestDto;
import org.kirillgaidai.income.rest.dto.currency.CurrencyGetRestDto;
import org.kirillgaidai.income.rest.dto.currency.CurrencyUpdateRestDto;
import org.kirillgaidai.income.service.dto.CurrencyDto;

import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;

public class CurrencyRestDtoMapperTest {

    final private IGenericRestDtoMapper<CurrencyGetRestDto, CurrencyCreateRestDto, CurrencyUpdateRestDto,
            CurrencyDto> mapper = new CurrencyRestDtoMapper();

    @Test
    public void testToDto_CreateNotNull() throws Exception {
        CurrencyCreateRestDto dto = new CurrencyCreateRestDto("cc1", "title1", 2);
        CurrencyDto expected = new CurrencyDto(null, "cc1", "title1", 2);
        CurrencyDto actual = mapper.toDto(dto);
        assertEntityEquals(expected, actual);
    }

    @Test
    public void testToDto_CreateNull() throws Exception {
        assertNull(mapper.toDto((CurrencyCreateRestDto) null));
    }

    @Test
    public void testToDto_UpdateNotNull() throws Exception {
        CurrencyUpdateRestDto dto = new CurrencyUpdateRestDto(1, "cc1", "title1", 2);
        CurrencyDto expected = new CurrencyDto(1, "cc1", "title1", 2);
        CurrencyDto actual = mapper.toDto(dto);
        assertEntityEquals(expected, actual);
    }

    @Test
    public void testToDto_UpdateNull() throws Exception {
        assertNull(mapper.toDto(null));
    }

    @Test
    public void testToRestDto_NotNull() throws Exception {
        CurrencyDto dto = new CurrencyDto(1, "cc1", "title1", 2);
        CurrencyGetRestDto expected = new CurrencyGetRestDto(1, "cc1", "title1", 2);
        CurrencyGetRestDto actual = mapper.toRestDto(dto);
        assertEntityEquals(expected, actual);
    }

    @Test
    public void testToRestDto_Null() throws Exception {
        assertNull(mapper.toRestDto(null));
    }

}
