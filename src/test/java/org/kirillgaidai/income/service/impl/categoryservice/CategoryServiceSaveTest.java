package org.kirillgaidai.income.service.impl.categoryservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.impl.CategoryDao;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.service.converter.CategoryConverter;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.exception.IncomeServiceCategoryNotFoundException;
import org.kirillgaidai.income.service.exception.IncomeServiceException;
import org.kirillgaidai.income.service.impl.CategoryService;
import org.kirillgaidai.income.service.intf.ICategoryService;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class CategoryServiceSaveTest {

    final private ICategoryDao dao = mock(CategoryDao.class);
    final private IGenericConverter<CategoryEntity, CategoryDto> converter = mock(CategoryConverter.class);
    final private ICategoryService service = new CategoryService(dao, converter);

    @Test
    public void testNull() throws Exception {
        try {
            service.save(null);
        } catch (IllegalArgumentException e) {
            assertEquals("null", e.getMessage());
        }
        verifyNoMoreInteractions(dao, converter);
    }

    @Test
    public void testInsert() throws Exception {
        CategoryDto dto = new CategoryDto(null, "01", "category1");
        CategoryEntity entity = new CategoryEntity(null, "01", "category1");
        CategoryDto expected = new CategoryDto(1, "01", "category1");

        doReturn(entity).when(converter).convertToEntity(dto);
        doReturn(expected).when(converter).convertToDto(entity);
        doReturn(1).when(dao).insert(entity);

        CategoryDto actual = service.save(dto);
        assertEntityEquals(expected, actual);

        verify(converter).convertToEntity(dto);
        verify(converter).convertToDto(entity);
        verify(dao).insert(entity);
        verifyNoMoreInteractions(dao, converter);
    }

    @Test
    public void testUpdate() throws Exception {
        CategoryDto dto = new CategoryDto(1, "01", "category1");
        CategoryEntity entity = new CategoryEntity(1, "01", "category1");
        CategoryDto expected = new CategoryDto(1, "01", "category1");

        doReturn(entity).when(converter).convertToEntity(dto);
        doReturn(expected).when(converter).convertToDto(entity);
        doReturn(1).when(dao).update(entity);

        CategoryDto actual = service.save(dto);
        assertEntityEquals(expected, actual);

        verify(converter).convertToEntity(dto);
        verify(converter).convertToDto(entity);
        verify(dao).update(entity);
        verifyNoMoreInteractions(dao, converter);
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        CategoryDto dto = new CategoryDto(1, "01", "category1");
        CategoryEntity entity = new CategoryEntity(1, "01", "category1");

        doReturn(entity).when(converter).convertToEntity(dto);
        doReturn(0).when(dao).update(entity);
        doReturn(1).when(dao).insert(entity);

        try {
            service.save(dto);
        } catch (IncomeServiceException e) {
            assertEquals("Dto isn't inserted or updated", e.getMessage());
        }

        verify(converter).convertToEntity(dto);
        verify(dao).update(entity);
        verifyNoMoreInteractions(dao, converter);
    }

}
