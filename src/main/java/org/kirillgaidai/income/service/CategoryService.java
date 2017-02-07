package org.kirillgaidai.income.service;

import org.kirillgaidai.income.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    List<CategoryDto> getCategoryList();

    CategoryDto getCategoryById(final Long id);

}
