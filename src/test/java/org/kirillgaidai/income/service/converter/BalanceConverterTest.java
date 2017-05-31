package org.kirillgaidai.income.service.converter;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.service.dto.BalanceDto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertBalanceEntityEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertBalanceDtoEquals;

public class BalanceConverterTest {

    private IGenericConverter<BalanceEntity, BalanceDto> converter = new BalanceConverter();
    private LocalDate day = LocalDate.of(2017, 5, 12);
    private BigDecimal amount = new BigDecimal("25.60");

    @Test
    public void testConvertToDto_Ok() throws Exception {
        BalanceEntity entity = new BalanceEntity(1, day, amount, true);
        BalanceDto expected = new BalanceDto(1, null, day, amount, true);
        BalanceDto actual = converter.convertToDto(entity);
        assertBalanceDtoEquals(expected, actual);
    }

    @Test
    public void testConvertToDto_Null() throws Exception {
        BalanceDto actual = converter.convertToDto(null);
        assertNull(actual);
    }

    @Test
    public void testConvertToEntity_Ok() throws Exception {
        BalanceDto dto = new BalanceDto(1, "account1", day, amount, false);
        BalanceEntity expected = new BalanceEntity(1, day, amount, false);
        BalanceEntity actual = converter.convertToEntity(dto);
        assertBalanceEntityEquals(expected, actual);
    }

    @Test
    public void testConvertToEntity_Null() throws Exception {
        BalanceEntity actual = converter.convertToEntity(null);
        assertNull(actual);
    }

}
