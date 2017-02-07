package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.CategoryDao;
import org.kirillgaidai.income.dto.CategoryDto;
import org.kirillgaidai.income.entity.CategoryEntity;
import org.kirillgaidai.income.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public List<CategoryDto> getCategoryList() {
        return categoryDao.getCategoryList().stream().map(this::convertToCategory).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(final Long id) {
        return convertToCategory(categoryDao.getCategoryById(id));
    }

    private CategoryDto convertToCategory(final CategoryEntity categoryEntity) {
        final CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryEntity.getId());
        categoryDto.setTitle(categoryEntity.getTitle());
        return categoryDto;
    }

}
