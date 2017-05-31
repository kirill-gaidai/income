package org.kirillgaidai.income.service.converter;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.service.dto.AccountDto;

import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertAccountEntityEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertAccountDtoEquals;

public class AccountConverterTest {

    final private IGenericConverter<AccountEntity, AccountDto> converter = new AccountConverter();

    @Test
    public void testConvertToDto_Ok() throws Exception {
        AccountEntity entity = new AccountEntity(1, 2, "01", "account1");
        AccountDto expected = new AccountDto(1, 2, null, null, "01", "account1");
        AccountDto actual = converter.convertToDto(entity);
        assertAccountDtoEquals(expected, actual);
    }

    @Test
    public void testConvertToDto_Null() throws Exception {
        AccountDto actual = converter.convertToDto(null);
        assertNull(actual);
    }

    @Test
    public void testConvertToEntity_Ok() throws Exception {
        AccountDto dto = new AccountDto(1, 2, "cc1", "currency1", "01", "account1");
        AccountEntity expected = new AccountEntity(1, 2, "01", "account1");
        AccountEntity actual = converter.convertToEntity(dto);
        assertAccountEntityEquals(expected, actual);
    }

    @Test
    public void testConvertToEntity_Null() throws Exception {
        AccountEntity actual = converter.convertToEntity(null);
        assertNull(actual);
    }

}
