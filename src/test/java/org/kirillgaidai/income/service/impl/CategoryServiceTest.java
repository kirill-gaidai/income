package org.kirillgaidai.income.service.impl;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.impl.CategoryDao;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.service.converter.CategoryConverter;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.exception.IncomeServiceCategoryNotFoundException;
import org.kirillgaidai.income.service.intf.ICategoryService;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertCategoryDtoEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertCategoryDtoListEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class CategoryServiceTest {

    final private ICategoryDao categoryDao = mock(CategoryDao.class);
    final private IGenericConverter<CategoryEntity, CategoryDto> categoryConverter = mock(CategoryConverter.class);
    final private ICategoryService categoryService = new CategoryService(categoryDao, categoryConverter);

    @Test
    public void testGetDtoList_AllOk() throws Exception {
        List<CategoryEntity> categoryEntityList = Arrays.asList(
                new CategoryEntity(1, "01", "category1"),
                new CategoryEntity(2, "02", "category2"),
                new CategoryEntity(3, "03", "category3")
        );
        List<CategoryDto> expected = Arrays.asList(
                new CategoryDto(1, "01", "category1"),
                new CategoryDto(2, "02", "category2"),
                new CategoryDto(3, "03", "category3")
        );

        doReturn(categoryEntityList).when(categoryDao).getEntityList();
        for (int index = 0; index < categoryEntityList.size(); index++) {
            doReturn(expected.get(index)).when(categoryConverter).convertToDto(categoryEntityList.get(index));
        }

        List<CategoryDto> actual = categoryService.getList();

        assertCategoryDtoListEquals(expected, actual);
        verify(categoryDao).getEntityList();
        for (CategoryEntity categoryEntity : categoryEntityList) {
            verify(categoryConverter).convertToDto(categoryEntity);
        }
        verifyNoMoreInteractions(categoryDao, categoryConverter);
    }

    @Test
    public void testGetDtoList_AllEmpty() throws Exception {
        List<CategoryEntity> categoryEntityList = Collections.emptyList();
        List<CategoryDto> expected = Collections.emptyList();
        doReturn(categoryEntityList).when(categoryDao).getEntityList();
        List<CategoryDto> actual = categoryService.getList();
        assertCategoryDtoListEquals(expected, actual);
        verify(categoryDao).getEntityList();
        verifyNoMoreInteractions(categoryDao, categoryConverter);
    }

    @Test
    public void testGetDtoList_IdsOk() throws Exception {
        Set<Integer> categoryIds = Sets.newSet(1, 2);
        List<CategoryEntity> categoryEntityList = Arrays.asList(
                new CategoryEntity(1, "01", "category1"),
                new CategoryEntity(2, "02", "category2")
        );
        List<CategoryDto> expected = Arrays.asList(
                new CategoryDto(1, "01", "category1"),
                new CategoryDto(2, "02", "category2")
        );

        doReturn(categoryEntityList).when(categoryDao).getEntityList(categoryIds);
        for (int index = 0; index < categoryEntityList.size(); index++) {
            doReturn(expected.get(index)).when(categoryConverter).convertToDto(categoryEntityList.get(index));
        }

        List<CategoryDto> actual = categoryService.getList(categoryIds);

        assertCategoryDtoListEquals(expected, actual);
        verify(categoryDao).getEntityList(categoryIds);
        for (CategoryEntity categoryEntity : categoryEntityList) {
            verify(categoryConverter).convertToDto(categoryEntity);
        }
        verifyNoMoreInteractions(categoryDao, categoryConverter);
    }

    @Test
    public void testGetDtoList_IdsEmpty() throws Exception {
        Set<Integer> categoryIds = Sets.newSet(1, 2);
        List<CategoryEntity> categoryEntityList = Collections.emptyList();
        List<CategoryDto> expected = Collections.emptyList();
        doReturn(categoryEntityList).when(categoryDao).getEntityList(categoryIds);
        List<CategoryDto> actual = categoryService.getList(categoryIds);
        assertCategoryDtoListEquals(expected, actual);
        verify(categoryDao).getEntityList(categoryIds);
        verifyNoMoreInteractions(categoryDao, categoryConverter);
    }

    @Test
    public void testGetDto_Null() throws Exception {
        try {
            categoryService.get(null);
        } catch (IncomeServiceCategoryNotFoundException e) {
            assertEquals("Category not found", e.getMessage());
        }
        verifyNoMoreInteractions(categoryDao, categoryConverter);
    }

    @Test
    public void testGetDto_NotFound() throws Exception {
        try {
            categoryService.get(1);
        } catch (IncomeServiceCategoryNotFoundException e) {
            assertEquals("Category with id 1 not found", e.getMessage());
        }
        verify(categoryDao).getEntity(1);
        verifyNoMoreInteractions(categoryDao, categoryConverter);
    }

    @Test
    public void testGetDto_Ok() throws Exception {
        CategoryEntity categoryEntity = new CategoryEntity(1, "01", "category1");
        CategoryDto expected = new CategoryDto(1, "01", "category1");

        doReturn(categoryEntity).when(categoryDao).getEntity(1);
        doReturn(expected).when(categoryConverter).convertToDto(categoryEntity);

        CategoryDto actual = categoryService.get(1);

        assertCategoryDtoEquals(expected, actual);

        verify(categoryDao).getEntity(1);
        verify(categoryConverter).convertToDto(categoryEntity);
        verifyNoMoreInteractions(categoryDao, categoryConverter);
    }

    @Test
    public void testSaveDto_Null() throws Exception {
        try {
            categoryService.saveDto(null);
        } catch (IncomeServiceCategoryNotFoundException e) {
            assertEquals("Category not found", e.getMessage());
        }
        verifyNoMoreInteractions(categoryDao, categoryConverter);
    }

    @Test
    public void testSaveDto_Insert() throws Exception {
        CategoryDto categoryDto = new CategoryDto(null, "01", "category1");
        CategoryEntity categoryEntity = new CategoryEntity(null, "01", "category1");

        doReturn(categoryEntity).when(categoryConverter).convertToEntity(categoryDto);
        doReturn(1).when(categoryDao).insertEntity(categoryEntity);

        categoryService.saveDto(categoryDto);

        verify(categoryConverter).convertToEntity(categoryDto);
        verify(categoryDao).insertEntity(categoryEntity);
        verifyNoMoreInteractions(categoryDao, categoryConverter);
    }

    @Test
    public void testSaveDto_UpdateNotFound() throws Exception {
        CategoryDto categoryDto = new CategoryDto(1, "01", "category1");
        CategoryEntity categoryEntity = new CategoryEntity(1, "01", "category1");

        doReturn(categoryEntity).when(categoryConverter).convertToEntity(categoryDto);
        doReturn(0).when(categoryDao).updateEntity(categoryEntity);

        try {
            categoryService.saveDto(categoryDto);
        } catch (IncomeServiceCategoryNotFoundException e) {
            assertEquals("Category with id 1 not found", e.getMessage());
        }

        verify(categoryConverter).convertToEntity(categoryDto);
        verify(categoryDao).updateEntity(categoryEntity);
        verifyNoMoreInteractions(categoryDao, categoryConverter);
    }

    @Test
    public void testSaveDto_Update() throws Exception {
        CategoryDto categoryDto = new CategoryDto(1, "01", "category1");
        CategoryEntity categoryEntity = new CategoryEntity(1, "01", "category1");

        doReturn(categoryEntity).when(categoryConverter).convertToEntity(categoryDto);
        doReturn(1).when(categoryDao).updateEntity(categoryEntity);

        categoryService.saveDto(categoryDto);

        verify(categoryConverter).convertToEntity(categoryDto);
        verify(categoryDao).updateEntity(categoryEntity);
        verifyNoMoreInteractions(categoryDao, categoryConverter);
    }

    @Test
    public void testDeleteDto_Null() throws Exception {
        try {
            categoryService.deleteDto(null);
        } catch (IncomeServiceCategoryNotFoundException e) {
            assertEquals("Category not found", e.getMessage());
        }
        verifyNoMoreInteractions(categoryDao, categoryConverter);
    }

    @Test
    public void testDeleteDto_NotFound() throws Exception {
        doReturn(0).when(categoryDao).deleteEntity(1);
        try {
            categoryService.deleteDto(1);
        } catch (IncomeServiceCategoryNotFoundException e) {
            assertEquals("Category with id 1 not found", e.getMessage());
        }
        verify(categoryDao).deleteEntity(1);
        verifyNoMoreInteractions(categoryDao, categoryConverter);
    }

    @Test
    public void testDeleteDto_Ok() throws Exception {
        doReturn(1).when(categoryDao).deleteEntity(1);
        categoryService.deleteDto(1);
        verify(categoryDao).deleteEntity(1);
        verifyNoMoreInteractions(categoryDao, categoryConverter);
    }

}
