package org.kirillgaidai.income.service.converter;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.UserEntity;
import org.kirillgaidai.income.service.dto.UserDto;

import java.time.LocalDateTime;

import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;

public class UserConverterTest {

    final private IGenericConverter<UserEntity, UserDto> converter = new UserConverter();

    /**
     * Test convert not null to dto
     *
     * @throws Exception exception
     */
    @Test
    public void testConvertToDto_NotNull() throws Exception {
        UserEntity entity = new UserEntity(1, "admin", "s3cr3t!", true, false, "token1",
                LocalDateTime.of(2018, 1, 2, 8, 45, 30));
        UserDto expected = new UserDto(1, "admin", "s3cr3t!", true, false, "token1",
                LocalDateTime.of(2018, 1, 2, 8, 45, 30));
        UserDto actual = converter.convertToDto(entity);
        assertEntityEquals(expected, actual);
    }

    /**
     * Test convert null to dto
     *
     * @throws Exception exception
     */
    @Test
    public void testConvertToDto_Null() throws Exception {
        UserDto actual = converter.convertToDto(null);
        assertNull(actual);
    }

    /**
     * Test convert not null to entity
     *
     * @throws Exception exception
     */
    @Test
    public void testConvertToEntity_NotNull() throws Exception {
        UserDto dto = new UserDto(1, "admin", "s3cr3t!", true, false, "token1",
                LocalDateTime.of(2018, 1, 2, 8, 45, 30));
        UserEntity expected = new UserEntity(1, "admin", "s3cr3t!", true, false, "token1",
                LocalDateTime.of(2018, 1, 2, 8, 45, 30));
        UserEntity actual = converter.convertToEntity(dto);
        assertEntityEquals(expected, actual);
    }

    /**
     * Test convert null to entity
     *
     * @throws Exception exception
     */
    @Test
    public void testConvertToEntity_Null() throws Exception {
        UserEntity actual = converter.convertToEntity(null);
        assertNull(actual);
    }

}
