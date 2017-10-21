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
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class CategoryServiceDeleteTest {

    final private ICategoryDao dao = mock(CategoryDao.class);
    final private IGenericConverter<CategoryEntity, CategoryDto> converter = mock(CategoryConverter.class);
    final private ICategoryService service = new CategoryService(dao, converter);

    @Test
    public void testNull() throws Exception {
        try {
            service.delete(null);
        } catch (IllegalArgumentException e) {
            assertEquals("null", e.getMessage());
        }
        verifyNoMoreInteractions(dao, converter);
    }

    @Test
    public void testNotFound() throws Exception {
        doReturn(0).when(dao).delete(1);
        try {
            service.delete(1);
        } catch (IncomeServiceCategoryNotFoundException e) {
            assertEquals("Category with id 1 not found", e.getMessage());
        }
        verify(dao).delete(1);
        verifyNoMoreInteractions(dao, converter);
    }

    @Test
    public void testOk() throws Exception {
        doReturn(1).when(dao).delete(1);
        service.delete(1);
        verify(dao).delete(1);
        verifyNoMoreInteractions(dao, converter);
    }

}
