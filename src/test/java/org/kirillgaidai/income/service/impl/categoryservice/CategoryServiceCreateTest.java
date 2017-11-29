package org.kirillgaidai.income.service.impl.categoryservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticCreateException;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.kirillgaidai.income.utils.TestUtils.getSerialEntityInsertAnswer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class CategoryServiceCreateTest extends CategoryServiceBaseTest {

    /**
     * Test dto is null
     *
     * @throws Exception exception
     */
    @Test
    public void testNull() throws Exception {
        try {
            service.create(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Dto is null", e.getMessage());
        }

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test failure
     *
     * @throws Exception exception
     */
    @Test
    public void testFailure() throws Exception {
        CategoryDto categoryDto = new CategoryDto(null, "01", "title");

        doReturn(0).when(categoryDao).insert(any(CategoryEntity.class));

        try {
            service.create(categoryDto);
        } catch (IncomeServiceOptimisticCreateException e) {
            assertEquals("Category create failure", e.getMessage());
        }

        ArgumentCaptor<CategoryEntity> argumentCaptor = ArgumentCaptor.forClass(CategoryEntity.class);

        verify(categoryDao).insert(argumentCaptor.capture());

        CategoryEntity expectedCategoryEntity = new CategoryEntity(null, "01", "title");
        CategoryEntity actualCategoryEntity = argumentCaptor.getValue();
        assertEntityEquals(expectedCategoryEntity, actualCategoryEntity);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Integer id = 1;

        CategoryDto categoryDto = new CategoryDto(null, "sort", "title");

        doAnswer(getSerialEntityInsertAnswer(id)).when(categoryDao).insert(any(CategoryEntity.class));

        CategoryDto expectedCategoryDto = new CategoryDto(id, "sort", "title");
        CategoryDto actualCategoryDto = service.create(categoryDto);
        assertEntityEquals(expectedCategoryDto, actualCategoryDto);

        ArgumentCaptor<CategoryEntity> argumentCaptor = ArgumentCaptor.forClass(CategoryEntity.class);

        verify(categoryDao).insert(argumentCaptor.capture());

        CategoryEntity expectedCategoryEntity = new CategoryEntity(id, "sort", "title");
        CategoryEntity actualCategoryEntity = argumentCaptor.getValue();
        assertEntityEquals(expectedCategoryEntity, actualCategoryEntity);

        verifyNoMoreDaoInteractions();
    }

}
