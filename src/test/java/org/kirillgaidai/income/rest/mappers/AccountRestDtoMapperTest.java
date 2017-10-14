package org.kirillgaidai.income.rest.mappers;

import org.junit.Test;
import org.kirillgaidai.income.rest.dto.account.AccountCreateRestDto;
import org.kirillgaidai.income.rest.dto.account.AccountGetRestDto;
import org.kirillgaidai.income.rest.dto.account.AccountUpdateRestDto;
import org.kirillgaidai.income.service.dto.AccountDto;

import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;

public class AccountRestDtoMapperTest {

    final private IGenericRestDtoMapper<AccountGetRestDto, AccountCreateRestDto, AccountUpdateRestDto,
            AccountDto> mapper = new AccountRestDtoMapper();

    @Test
    public void testToDto_CreateNotNull() throws Exception {
        AccountCreateRestDto dto = new AccountCreateRestDto(1, "01", "title");
        AccountDto expected = new AccountDto(null, 1, null, null, "01", "title");
        AccountDto actual = mapper.toDto(dto);
        assertEntityEquals(expected, actual);
    }

    @Test
    public void testToDto_CreateNull() throws Exception {
        assertNull(mapper.toDto((AccountCreateRestDto) null));
    }

    @Test
    public void testToDto_UpdateNotNull() throws Exception {
        AccountUpdateRestDto dto = new AccountUpdateRestDto(1, 2, "01", "title");
        AccountDto expected = new AccountDto(1, 2, null, null, "01", "title");
        AccountDto actual = mapper.toDto(dto);
        assertEntityEquals(expected, actual);
    }

    @Test
    public void testToDto_UpdateNull() throws Exception {
        assertNull(mapper.toDto(null));
    }

    @Test
    public void testRestToDto_NotNull() throws Exception {
        AccountDto dto = new AccountDto(1, 2, "cc1", "currency1", "01", "title");
        AccountGetRestDto expected = new AccountGetRestDto(1, 2, "cc1", "currency1", "01", "title");
        AccountGetRestDto actual = mapper.toRestDto(dto);
        assertEntityEquals(expected, actual);
    }

    @Test
    public void testRestToDto_Null() throws Exception {
        assertNull(mapper.toRestDto(null));
    }

}
