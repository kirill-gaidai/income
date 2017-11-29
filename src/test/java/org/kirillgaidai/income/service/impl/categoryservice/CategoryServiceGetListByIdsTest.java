package org.kirillgaidai.income.service.impl.categoryservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class CategoryServiceGetListByIdsTest extends CategoryServiceBaseTest {

    /**
     * Test empty list
     *
     * @throws Exception exception
     */
    @Test
    public void testEmpty() throws Exception {
        Set<Integer> categoryIds = Sets.newSet(1, 2);

        List<CategoryEntity> entityList = Collections.emptyList();

        doReturn(entityList).when(categoryDao).getList(categoryIds);

        List<CategoryDto> expected = Collections.emptyList();
        List<CategoryDto> actual = service.getList(categoryIds);
        assertEntityListEquals(expected, actual);

        verify(categoryDao).getList(categoryIds);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test successful
     *
     * @throws Exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Set<Integer> categoryIds = Sets.newSet(1, 2);

        List<CategoryEntity> categoryEntityList = Arrays.asList(
                new CategoryEntity(1, "01", "category1"),
                new CategoryEntity(2, "02", "category2")
        );

        doReturn(categoryEntityList).when(categoryDao).getList(categoryIds);

        List<CategoryDto> expected = Arrays.asList(
                new CategoryDto(1, "01", "category1"),
                new CategoryDto(2, "02", "category2")
        );
        List<CategoryDto> actual = service.getList(categoryIds);
        assertEntityListEquals(expected, actual);

        verify(categoryDao).getList(categoryIds);

        verifyNoMoreDaoInteractions();
    }

}
