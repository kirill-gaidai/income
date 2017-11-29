package org.kirillgaidai.income.service.impl.categoryservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;

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
            assertEquals("Id is null", e.getMessage());
        }

        verifyNoMoreDaoInteractions();
    }

    @Test
    public void testNotFound() throws Exception {
        Integer id = 1;

        try {
            service.get(id);
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Category with id %d not found", id), e.getMessage());
        }

        verify(categoryDao).get(id);

        verifyNoMoreDaoInteractions();
    }

    @Test
    public void testSuccessful() throws Exception {
        CategoryEntity entity = new CategoryEntity(1, "01", "category1");

        doReturn(entity).when(categoryDao).get(1);

        CategoryDto expectedCategoryDto = new CategoryDto(1, "01", "category1");
        CategoryDto actualCategoryDto = service.get(1);
        assertCategoryDtoEquals(expectedCategoryDto, actualCategoryDto);

        verify(categoryDao).get(1);

        verifyNoMoreDaoInteractions();
    }

}
