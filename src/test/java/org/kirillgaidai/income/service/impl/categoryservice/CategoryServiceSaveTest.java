package org.kirillgaidai.income.service.impl.categoryservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.exception.IncomeServiceException;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class CategoryServiceSaveTest extends CategoryServiceBaseTest {

    @Test
    public void testNull() throws Exception {
        try {
            service.save(null);
        } catch (IllegalArgumentException e) {
            assertEquals("null", e.getMessage());
        }
        verifyNoMoreInteractions();
    }

    @Test
    public void testInsert() throws Exception {
        CategoryDto dto = new CategoryDto(null, "01", "category1");
        CategoryEntity entity = new CategoryEntity(null, "01", "category1");
        CategoryDto expected = new CategoryDto(1, "01", "category1");

        doReturn(entity).when(converter).convertToEntity(dto);
        doReturn(expected).when(converter).convertToDto(entity);
        doReturn(1).when(categoryDao).insert(entity);

        CategoryDto actual = service.save(dto);
        assertEntityEquals(expected, actual);

        verify(converter).convertToEntity(dto);
        verify(converter).convertToDto(entity);
        verify(categoryDao).insert(entity);
        verifyNoMoreInteractions();
    }

    @Test
    public void testUpdate() throws Exception {
        CategoryDto dto = new CategoryDto(1, "01", "category1");
        CategoryEntity entity = new CategoryEntity(1, "01", "category1");
        CategoryDto expected = new CategoryDto(1, "01", "category1");

        doReturn(entity).when(converter).convertToEntity(dto);
        doReturn(expected).when(converter).convertToDto(entity);
        doReturn(1).when(categoryDao).update(entity);

        CategoryDto actual = service.save(dto);
        assertEntityEquals(expected, actual);

        verify(converter).convertToEntity(dto);
        verify(converter).convertToDto(entity);
        verify(categoryDao).update(entity);
        verifyNoMoreInteractions();
    }

    @Test
    public void testUpdateNotFound() throws Exception {
        CategoryDto dto = new CategoryDto(1, "01", "category1");
        CategoryEntity entity = new CategoryEntity(1, "01", "category1");

        doReturn(entity).when(converter).convertToEntity(dto);
        doReturn(0).when(categoryDao).update(entity);
        doReturn(1).when(categoryDao).insert(entity);

        try {
            service.save(dto);
        } catch (IncomeServiceException e) {
            assertEquals("Dto isn't inserted or updated", e.getMessage());
        }

        verify(converter).convertToEntity(dto);
        verify(categoryDao).update(entity);
        verifyNoMoreInteractions();
    }

}
