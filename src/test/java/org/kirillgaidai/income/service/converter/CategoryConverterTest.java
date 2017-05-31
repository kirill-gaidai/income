package org.kirillgaidai.income.service.converter;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.service.dto.CategoryDto;

import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertCategoryEntityEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertCategoryDtoEquals;

public class CategoryConverterTest {

    private IGenericConverter<CategoryEntity, CategoryDto> converter = new CategoryConverter();

    @Test
    public void testConvertToDto_Ok() throws Exception {
        CategoryEntity entity = new CategoryEntity(1, "01", "category1");
        CategoryDto expected = new CategoryDto(1, "01", "category1");
        CategoryDto actual = converter.convertToDto(entity);
        assertCategoryDtoEquals(expected, actual);
    }

    @Test
    public void testConvertToDtoNull() throws Exception {
        CategoryDto actual = converter.convertToDto(null);
        assertNull(actual);
    }

    @Test
    public void testConvertToEntity_Ok() throws Exception {
        CategoryDto dto = new CategoryDto(1, "01", "category1");
        CategoryEntity expected = new CategoryEntity(1, "01", "category1");
        CategoryEntity actual = converter.convertToEntity(dto);
        assertCategoryEntityEquals(expected, actual);
    }

    @Test
    public void testCovertToEntity_Null() throws Exception {
        CategoryEntity actual = converter.convertToEntity(null);
        assertNull(actual);
    }

}
