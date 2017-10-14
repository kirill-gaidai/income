package org.kirillgaidai.income.rest.mappers;

import org.junit.Test;
import org.kirillgaidai.income.rest.dto.category.CategoryCreateRestDto;
import org.kirillgaidai.income.rest.dto.category.CategoryGetRestDto;
import org.kirillgaidai.income.rest.dto.category.CategoryUpdateRestDto;
import org.kirillgaidai.income.service.dto.CategoryDto;

import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;

public class CategoryRestDtoMapperTest {

    final private IGenericRestDtoMapper<CategoryGetRestDto, CategoryCreateRestDto, CategoryUpdateRestDto,
            CategoryDto> mapper = new CategoryRestDtoMapper();

    @Test
    public void testToDto_CreateNotNull() throws Exception {
        CategoryCreateRestDto dto = new CategoryCreateRestDto("01", "title1");
        CategoryDto expected = new CategoryDto(null, "01", "title1");
        CategoryDto actual = mapper.toDto(dto);
        assertEntityEquals(expected, actual);
    }

    @Test
    public void testToDto_CreateNull() throws Exception {
        assertNull(mapper.toDto((CategoryCreateRestDto) null));
    }

    @Test
    public void testToDto_UpdateNotNull() throws Exception {
        CategoryUpdateRestDto dto = new CategoryUpdateRestDto(1, "01", "title1");
        CategoryDto expected = new CategoryDto(1, "01", "title1");
        CategoryDto actual = mapper.toDto(dto);
        assertEntityEquals(expected, actual);
    }

    @Test
    public void testToDto_UpdateNull() throws Exception {
        assertNull(mapper.toDto(null));
    }

    @Test
    public void testToRestDto_NotNull() throws Exception {
        CategoryDto dto = new CategoryDto(1, "01", "title1");
        CategoryGetRestDto expected = new CategoryGetRestDto(1, "01", "title1");
        CategoryGetRestDto actual = mapper.toRestDto(dto);
        assertEntityEquals(expected, actual);
    }

    @Test
    public void testToRestDto_Null() throws Exception {
        assertNull(mapper.toRestDto(null));
    }

}
