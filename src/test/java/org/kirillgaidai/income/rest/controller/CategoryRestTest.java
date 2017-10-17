package org.kirillgaidai.income.rest.controller;

import org.junit.Test;
import org.kirillgaidai.income.rest.dto.category.CategoryCreateRestDto;
import org.kirillgaidai.income.rest.dto.category.CategoryGetRestDto;
import org.kirillgaidai.income.rest.dto.category.CategoryUpdateRestDto;
import org.kirillgaidai.income.rest.mappers.CategoryRestDtoMapper;
import org.kirillgaidai.income.rest.mappers.IGenericRestDtoMapper;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.intf.ICategoryService;
import org.kirillgaidai.income.service.intf.IGenericService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class CategoryRestTest {

    final private IGenericRestDtoMapper<CategoryGetRestDto, CategoryCreateRestDto, CategoryUpdateRestDto,
            CategoryDto> mapper = mock(CategoryRestDtoMapper.class);
    final private IGenericService<CategoryDto> service = mock(ICategoryService.class);
    final private IGenericRest<CategoryGetRestDto, CategoryCreateRestDto, CategoryUpdateRestDto> rest =
            new CategoryRest(service, mapper);

    @Test
    public void testGetAll_NotEmpty() throws Exception {
        List<CategoryDto> categoryDtoList = Arrays.asList(
                new CategoryDto(1, "01", "title1"),
                new CategoryDto(2, "02", "title2"),
                new CategoryDto(3, "03", "title3")
        );
        List<CategoryGetRestDto> expected = Arrays.asList(
                new CategoryGetRestDto(1, "01", "title1"),
                new CategoryGetRestDto(2, "02", "title2"),
                new CategoryGetRestDto(3, "03", "title3")
        );

        doReturn(categoryDtoList).when(service).getList();
        for (int index = 0; index < categoryDtoList.size(); index++) {
            doReturn(expected.get(index)).when(mapper).toRestDto(categoryDtoList.get(index));
        }

        List<CategoryGetRestDto> actual = rest.getList();
        assertEntityListEquals(expected, actual);
        verify(service).getList();
        for (CategoryDto categoryDto : categoryDtoList) {
            verify(mapper).toRestDto(categoryDto);
        }
        verifyNoMoreInteractions(service, mapper);
    }

    @Test
    public void testGetAll_Empty() throws Exception {
        List<CategoryDto> categoryDtoList = Collections.emptyList();
        List<CategoryGetRestDto> expected = Collections.emptyList();
        doReturn(categoryDtoList).when(service).getList();
        List<CategoryGetRestDto> actual = rest.getList();
        assertEntityListEquals(expected, actual);
        verify(service).getList();
        verifyNoMoreInteractions(service, mapper);
    }

}
