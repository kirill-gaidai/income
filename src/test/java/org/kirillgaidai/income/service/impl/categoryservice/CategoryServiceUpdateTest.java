package org.kirillgaidai.income.service.impl.categoryservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticUpdateException;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class CategoryServiceUpdateTest extends CategoryServiceBaseTest {

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
     * Test id is null
     *
     * @throws Exception exception
     */
    @Test
    public void testIdIsNull() throws Exception {
        CategoryDto dto = new CategoryDto(null, "01", "title");

        try {
            service.update(dto);
        } catch (IllegalArgumentException e) {
            assertEquals("Id is null", e.getMessage());
        }

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        Integer id = 1;

        CategoryDto dto = new CategoryDto(id, "01", "title");

        doReturn(null).when(categoryDao).get(id);

        try {
            service.update(dto);
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Category with id %d not found", id), e.getMessage());
        }

        verify(categoryDao).get(id);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test failure
     *
     * @throws Exception exception
     */
    @Test
    public void testFailure() throws Exception {
        Integer id = 1;

        CategoryDto dto = new CategoryDto(id, "02", "newTitle");
        CategoryEntity oldEntity = new CategoryEntity(id, "01", "oldTitle");

        doReturn(oldEntity).when(categoryDao).get(id);
        doReturn(0).when(categoryDao).update(any(CategoryEntity.class), eq(oldEntity));

        try {
            service.update(dto);
        } catch (IncomeServiceOptimisticUpdateException e) {
            assertEquals(String.format("Category with id %d update failure", id), e.getMessage());
        }

        ArgumentCaptor<CategoryEntity> argumentCaptor = ArgumentCaptor.forClass(CategoryEntity.class);

        verify(categoryDao).get(id);
        verify(categoryDao).update(argumentCaptor.capture(), eq(oldEntity));

        CategoryEntity expectedEntity = new CategoryEntity(id, "02", "newTitle");
        CategoryEntity actualEntity = argumentCaptor.getValue();
        assertEntityEquals(expectedEntity, actualEntity);

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

        CategoryDto dto = new CategoryDto(id, "02", "newTitle");
        CategoryEntity oldEntity = new CategoryEntity(id, "01", "oldTitle");

        doReturn(oldEntity).when(categoryDao).get(id);
        doReturn(1).when(categoryDao).update(any(CategoryEntity.class), eq(oldEntity));

        CategoryDto expectedDto = new CategoryDto(id, "02", "newTitle");
        CategoryDto actualDto = service.update(dto);
        assertEntityEquals(expectedDto, actualDto);

        ArgumentCaptor<CategoryEntity> argumentCaptor = ArgumentCaptor.forClass(CategoryEntity.class);

        verify(categoryDao).get(id);
        verify(categoryDao).update(argumentCaptor.capture(), eq(oldEntity));

        CategoryEntity expectedEntity = new CategoryEntity(id, "02", "newTitle");
        CategoryEntity actualEntity = argumentCaptor.getValue();
        assertEntityEquals(expectedEntity, actualEntity);

        verifyNoMoreDaoInteractions();
    }

}
