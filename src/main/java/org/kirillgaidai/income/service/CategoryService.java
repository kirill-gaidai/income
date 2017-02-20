package org.kirillgaidai.income.service;

import org.kirillgaidai.income.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getCategoryList();

    CategoryDto getCategoryById(final Integer id);

    void saveCategory(final CategoryDto categoryDto);

    void deleteCategory(final Integer id);

}
