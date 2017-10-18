package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.service.exception.IncomeServiceCategoryNotFoundException;
import org.kirillgaidai.income.service.intf.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryService implements ICategoryService {

    final private ICategoryDao categoryDao;
    final private IGenericConverter<CategoryEntity, CategoryDto> categoryConverter;

    @Autowired
    public CategoryService(
            ICategoryDao categoryDao,
            IGenericConverter<CategoryEntity, CategoryDto> categoryConverter) {
        super();
        this.categoryDao = categoryDao;
        this.categoryConverter = categoryConverter;
    }

    @Override
    public List<CategoryDto> getList() {
        return categoryDao.getList().stream().map(categoryConverter::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<CategoryDto> getList(Set<Integer> ids) {
        return categoryDao.getList(ids).stream().map(categoryConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto get(Integer id) {
        if (id == null) {
            throw new IncomeServiceCategoryNotFoundException();
        }
        CategoryEntity categoryEntity = categoryDao.get(id);
        if (categoryEntity == null) {
            throw new IncomeServiceCategoryNotFoundException(id);
        }
        return categoryConverter.convertToDto(categoryEntity);
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        if (categoryDto == null) {
            throw new IncomeServiceCategoryNotFoundException();
        }
        CategoryEntity categoryEntity = categoryConverter.convertToEntity(categoryDto);
        if (categoryEntity.getId() == null) {
            categoryDao.insert(categoryEntity);
            categoryDto.setId(categoryEntity.getId());
            return null;
        }
        int affectedRows = categoryDao.update(categoryEntity);
        if (affectedRows != 1) {
            throw new IncomeServiceCategoryNotFoundException(categoryDto.getId());
        }
        return null;
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            throw new IncomeServiceCategoryNotFoundException();
        }
        int affectedRows = categoryDao.delete(id);
        if (affectedRows != 1) {
            throw new IncomeServiceCategoryNotFoundException(id);
        }
    }

}
