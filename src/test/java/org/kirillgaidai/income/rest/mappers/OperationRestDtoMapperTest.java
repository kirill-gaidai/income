package org.kirillgaidai.income.rest.mappers;

import org.junit.Test;
import org.kirillgaidai.income.rest.dto.operation.OperationCreateRestDto;
import org.kirillgaidai.income.rest.dto.operation.OperationGetRestDto;
import org.kirillgaidai.income.rest.dto.operation.OperationUpdateRestDto;
import org.kirillgaidai.income.service.dto.OperationDto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;

public class OperationRestDtoMapperTest {

    final private LocalDate day = LocalDate.of(2017, 10, 9);
    final private BigDecimal amount = new BigDecimal("1.23");
    final private IGenericRestDtoMapper<OperationGetRestDto, OperationCreateRestDto, OperationUpdateRestDto,
            OperationDto> mapper = new OperationRestDtoMapper();

    @Test
    public void testToDto_CreateNotNull() throws Exception {
        OperationCreateRestDto dto = new OperationCreateRestDto(1, 2, day, amount, "note1");
        OperationDto expected = new OperationDto(null, 1, null, 2, null, day, amount, "note1");
        OperationDto actual = mapper.toDto(dto);
        assertEntityEquals(expected, actual);
    }

    @Test
    public void testToDto_CreateNull() throws Exception {
        assertNull(mapper.toDto((OperationCreateRestDto) null));
    }

    @Test
    public void testToDto_UpdateNotNull() throws Exception {
        OperationUpdateRestDto dto = new OperationUpdateRestDto(1, 3, amount, "note1");
        OperationDto expected = new OperationDto(1, null, null, 3, null, null, amount, "note1");
        OperationDto actual = mapper.toDto(dto);
        assertEntityEquals(expected, actual);
    }

    @Test
    public void testToDto_UpdateNull() throws Exception {
        assertNull(mapper.toDto((OperationUpdateRestDto) null));
    }

    @Test
    public void testToRestDto_NotNull() throws Exception {
        OperationDto dto = new OperationDto(1, 2, "account1", 3, "category1", day, amount, "note1");
        OperationGetRestDto expected = new OperationGetRestDto(1, 2, "account1", 3, "category1", day, amount, "note1");
        OperationGetRestDto actual = mapper.toRestDto(dto);
        assertEntityEquals(expected, actual);
    }

    @Test
    public void testToRestDto_Null() throws Exception {
        assertNull(mapper.toRestDto(null));
    }

}
