package org.kirillgaidai.income.service.impl.categoryservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.exception.IncomeServiceCategoryNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertCategoryDtoEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class CategoryServiceGetTest extends CategoryServiceBaseTest {

    @Test
    public void testNull() throws Exception {
        try {
            service.get(null);
        } catch (IllegalArgumentException e) {
            assertEquals("null", e.getMessage());
        }
        verifyNoMoreInteractions();
    }

    @Test
    public void testNotFound() throws Exception {
        try {
            service.get(1);
        } catch (IncomeServiceCategoryNotFoundException e) {
            assertEquals("Category with id 1 not found", e.getMessage());
        }
        verify(categoryDao).get(1);
        verifyNoMoreInteractions();
    }

    @Test
    public void testOk() throws Exception {
        CategoryEntity entity = new CategoryEntity(1, "01", "category1");
        CategoryDto expected = new CategoryDto(1, "01", "category1");

        doReturn(entity).when(categoryDao).get(1);
        doReturn(expected).when(converter).convertToDto(entity);

        CategoryDto actual = service.get(1);
        assertCategoryDtoEquals(expected, actual);

        verify(categoryDao).get(1);
        verify(converter).convertToDto(entity);
        verifyNoMoreInteractions();
    }

}
