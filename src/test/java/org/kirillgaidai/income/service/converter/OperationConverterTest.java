package org.kirillgaidai.income.service.converter;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.service.dto.OperationDto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertOperationEntityEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertOperationDtoEquals;

public class OperationConverterTest {

    final private IGenericConverter<OperationEntity, OperationDto> converter = new OperationConverter();

    @Test
    public void testConvertToDto_Ok() throws Exception {
        BigDecimal amount = new BigDecimal("12.34");
        LocalDate day = LocalDate.of(2017, 3, 8);
        OperationEntity entity = new OperationEntity(1, 2, 3, day, amount, "note1");
        OperationDto expected = new OperationDto(1, 2, null, 3, null, day, amount, "note1");
        OperationDto actual = converter.convertToDto(entity);
        assertOperationDtoEquals(expected, actual);
    }

    @Test
    public void testConvertToDto_Null() throws Exception {
        OperationDto actual = converter.convertToDto(null);
        assertNull(actual);
    }

    @Test
    public void testConvertToEntity_Ok() throws Exception {
        BigDecimal amount = new BigDecimal("12.34");
        LocalDate day = LocalDate.of(2017, 3, 8);
        OperationDto dto = new OperationDto(1, 2, "account1", 3, "category1", day, amount, "note1");
        OperationEntity expected = new OperationEntity(1, 2, 3, day, amount, "note1");
        OperationEntity actual = converter.convertToEntity(dto);
        assertOperationEntityEquals(expected, actual);
    }

    @Test
    public void testConvertToEntity_Null() throws Exception {
        OperationEntity actual = converter.convertToEntity(null);
        assertNull(actual);
    }

}
