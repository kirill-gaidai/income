package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.intf.ICategoryService;
import org.kirillgaidai.income.service.util.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class CategoryService extends SerialService<CategoryDto, CategoryEntity> implements ICategoryService {

    final private static Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    public CategoryService(
            ICategoryDao dao,
            ServiceHelper serviceHelper,
            IGenericConverter<CategoryEntity, CategoryDto> converter) {
        super(dao, converter, serviceHelper);
    }

    @Override
    @Transactional
    public List<CategoryDto> getList() {
        LOGGER.debug("Entering method");
        return super.getList();
    }

    @Override
    @Transactional
    public List<CategoryDto> getList(Set<Integer> ids) {
        LOGGER.debug("Entering method");
        return super.getList(ids);
    }

    @Override
    @Transactional
    public CategoryDto get(Integer id) {
        LOGGER.debug("Entering method");
        validateId(id);
        return converter.convertToDto(serviceHelper.getCategoryEntity(id));
    }

    @Override
    @Transactional
    public CategoryDto create(CategoryDto dto) {
        LOGGER.debug("Entering method");
        validateDto(dto);
        CategoryEntity entity = converter.convertToEntity(dto);
        serviceHelper.createCategoryEntity(entity);
        return converter.convertToDto(entity);
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto dto) {
        LOGGER.debug("Entering method");
        validateDto(dto);
        Integer id = dto.getId();
        validateId(id);
        CategoryEntity oldEntity = serviceHelper.getCategoryEntity(id);
        CategoryEntity newEntity = converter.convertToEntity(dto);
        serviceHelper.updateCategoryEntity(newEntity, oldEntity);
        return converter.convertToDto(newEntity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        LOGGER.debug("Entering method");
        validateId(id);
        CategoryEntity entity = serviceHelper.getCategoryEntity(id);
        serviceHelper.checkCategoryDependentOperations(id);
        serviceHelper.deleteCategoryEntity(entity);
    }

}
