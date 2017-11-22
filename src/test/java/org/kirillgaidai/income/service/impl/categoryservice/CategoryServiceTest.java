package org.kirillgaidai.income.service.impl.categoryservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertCategoryDtoListEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class CategoryServiceTest extends CategoryServiceBaseTest {

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

        doReturn(categoryEntityList).when(categoryDao).getList();
        for (int index = 0; index < categoryEntityList.size(); index++) {
            doReturn(expected.get(index)).when(converter).convertToDto(categoryEntityList.get(index));
        }

        List<CategoryDto> actual = service.getList();

        assertCategoryDtoListEquals(expected, actual);
        verify(categoryDao).getList();
        for (CategoryEntity categoryEntity : categoryEntityList) {
            verify(converter).convertToDto(categoryEntity);
        }
        verifyNoMoreInteractions();
    }

    @Test
    public void testGetDtoList_AllEmpty() throws Exception {
        List<CategoryEntity> categoryEntityList = Collections.emptyList();
        List<CategoryDto> expected = Collections.emptyList();
        doReturn(categoryEntityList).when(categoryDao).getList();
        List<CategoryDto> actual = service.getList();
        assertCategoryDtoListEquals(expected, actual);
        verify(categoryDao).getList();
        verifyNoMoreInteractions();
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

        doReturn(categoryEntityList).when(categoryDao).getList(categoryIds);
        for (int index = 0; index < categoryEntityList.size(); index++) {
            doReturn(expected.get(index)).when(converter).convertToDto(categoryEntityList.get(index));
        }

        List<CategoryDto> actual = service.getList(categoryIds);

        assertCategoryDtoListEquals(expected, actual);
        verify(categoryDao).getList(categoryIds);
        for (CategoryEntity categoryEntity : categoryEntityList) {
            verify(converter).convertToDto(categoryEntity);
        }
        verifyNoMoreInteractions();
    }

    @Test
    public void testGetDtoList_IdsEmpty() throws Exception {
        Set<Integer> categoryIds = Sets.newSet(1, 2);
        List<CategoryEntity> categoryEntityList = Collections.emptyList();
        List<CategoryDto> expected = Collections.emptyList();
        doReturn(categoryEntityList).when(categoryDao).getList(categoryIds);
        List<CategoryDto> actual = service.getList(categoryIds);
        assertCategoryDtoListEquals(expected, actual);
        verify(categoryDao).getList(categoryIds);
        verifyNoMoreInteractions();
    }

}
