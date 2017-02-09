package org.kirillgaidai.income.service;

import org.kirillgaidai.income.dto.CategoryDto;
import org.kirillgaidai.income.dto.CategoryListDto;

import java.util.List;

public interface CategoryService {

    CategoryListDto getCategoryList();

    CategoryDto getCategoryById(final Long id);

}
