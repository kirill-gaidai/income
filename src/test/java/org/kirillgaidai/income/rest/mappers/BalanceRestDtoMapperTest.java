package org.kirillgaidai.income.rest.mappers;

import org.junit.Test;
import org.kirillgaidai.income.rest.dto.balance.BalanceGetRestDto;
import org.kirillgaidai.income.rest.dto.balance.BalanceUpdateRestDto;
import org.kirillgaidai.income.service.dto.BalanceDto;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;

public class BalanceRestDtoMapperTest {

    final private LocalDate day = LocalDate.of(2017, 10, 9);
    final private BigDecimal amount = new BigDecimal("1.23");
    final private BalanceRestDtoMapper mapper = new BalanceRestDtoMapper();

    @Test
    public void testToDto_NotNull() throws Exception {
        BalanceUpdateRestDto dto = new BalanceUpdateRestDto(1, day, amount, true);
        BalanceDto expected = new BalanceDto(1, null, day, amount, true);
        BalanceDto actual = mapper.toDto(dto);
        assertEntityEquals(expected, actual);
    }

    @Test
    public void testToDto_Null() throws Exception {
        assertNull(mapper.toDto(null));
    }

    @Test
    public void testToRestDto_NotNull() throws Exception {
        BalanceDto dto = new BalanceDto(1, "account1", day, amount, true);
        BalanceGetRestDto expected = new BalanceGetRestDto(1, "account1", day, amount, true);
        BalanceGetRestDto actual = mapper.toRestDto(dto);
        assertEntityEquals(expected, actual);
    }

    @Test
    public void testToRestDto_Null() throws Exception {
        assertNull(mapper.toRestDto(null));
    }

}
