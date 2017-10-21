package org.kirillgaidai.income.service.impl.categoryservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.impl.CategoryDao;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.service.converter.CategoryConverter;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.exception.IncomeServiceCategoryNotFoundException;
import org.kirillgaidai.income.service.impl.CategoryService;
import org.kirillgaidai.income.service.intf.ICategoryService;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertCategoryDtoEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class CategoryServiceGetTest {

    final private ICategoryDao dao = mock(CategoryDao.class);
    final private IGenericConverter<CategoryEntity, CategoryDto> converter = mock(CategoryConverter.class);
    final private ICategoryService service = new CategoryService(dao, converter);

    @Test
    public void testNull() throws Exception {
        try {
            service.get(null);
        } catch (IllegalArgumentException e) {
            assertEquals("null", e.getMessage());
        }
        verifyNoMoreInteractions(dao, converter);
    }

    @Test
    public void testNotFound() throws Exception {
        try {
            service.get(1);
        } catch (IncomeServiceCategoryNotFoundException e) {
            assertEquals("Category with id 1 not found", e.getMessage());
        }
        verify(dao).get(1);
        verifyNoMoreInteractions(dao, converter);
    }

    @Test
    public void testOk() throws Exception {
        CategoryEntity entity = new CategoryEntity(1, "01", "category1");
        CategoryDto expected = new CategoryDto(1, "01", "category1");

        doReturn(entity).when(dao).get(1);
        doReturn(expected).when(converter).convertToDto(entity);

        CategoryDto actual = service.get(1);
        assertCategoryDtoEquals(expected, actual);

        verify(dao).get(1);
        verify(converter).convertToDto(entity);
        verifyNoMoreInteractions(dao, converter);
    }

}
