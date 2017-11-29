package org.kirillgaidai.income.service.impl.categoryservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.service.dto.CategoryDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class CategoryServiceGetListTest extends CategoryServiceBaseTest {

    @Test
    public void testEmpty() throws Exception {
        List<CategoryEntity> entityList = Collections.emptyList();

        doReturn(entityList).when(categoryDao).getList();

        List<CategoryDto> expected = Collections.emptyList();
        List<CategoryDto> actual = service.getList();
        assertEntityListEquals(expected, actual);

        verify(categoryDao).getList();

        verifyNoMoreDaoInteractions();
    }

    @Test
    public void testSuccessful() throws Exception {
        List<CategoryEntity> entityList = Arrays.asList(
                new CategoryEntity(1, "01", "category1"),
                new CategoryEntity(2, "02", "category2"),
                new CategoryEntity(3, "03", "category3")
        );

        doReturn(entityList).when(categoryDao).getList();

        List<CategoryDto> expected = Arrays.asList(
                new CategoryDto(1, "01", "category1"),
                new CategoryDto(2, "02", "category2"),
                new CategoryDto(3, "03", "category3")
        );
        List<CategoryDto> actual = service.getList();
        assertEntityListEquals(expected, actual);

        verify(categoryDao).getList();

        verifyNoMoreDaoInteractions();
    }

}
