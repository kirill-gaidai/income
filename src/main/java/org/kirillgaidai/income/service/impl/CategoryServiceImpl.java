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

    private CategoryDao categoryDao;

    @Autowired
    public CategoryServiceImpl(final CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public List<CategoryDto> getCategoryList() {
        return categoryDao.getCategoryList().stream().map(this::convertToCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(final Integer id) {
        if (id == null) {
            return null;
        }
        return convertToCategoryDto(categoryDao.getCategoryById(id));
    }

    @Override
    public void saveCategory(final CategoryDto categoryDto) {
        final CategoryEntity categoryEntity = convertToCategoryEntity(categoryDto);
        if (categoryEntity.getId() != null) {
            categoryDao.updateCategory(categoryEntity);
            return;
        }
        categoryDao.insertCategory(categoryEntity);
        categoryDto.setId(categoryEntity.getId());
    }

    @Override
    public void deleteCategory(final Integer id) {
        categoryDao.deleteCategory(id);
    }

    private CategoryDto convertToCategoryDto(final CategoryEntity categoryEntity) {
        if (categoryEntity == null) {
            return null;
        }
        final CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryEntity.getId());
        categoryDto.setTitle(categoryEntity.getTitle());
        return categoryDto;
    }

    private CategoryEntity convertToCategoryEntity(final CategoryDto categoryDto) {
        if (categoryDto == null) {
            return null;
        }
        final CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(categoryDto.getId());
        categoryEntity.setTitle(categoryDto.getTitle());
        return categoryEntity;
    }

}
