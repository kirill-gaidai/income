package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.exception.IncomeServiceCategoryNotFoundException;
import org.kirillgaidai.income.service.intf.ICategoryService;
import org.kirillgaidai.income.service.util.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends SerialService<CategoryDto, CategoryEntity> implements ICategoryService {

    final private static Logger LOGGER = LoggerFactory.getLogger(CategoryService.class);

    final private ServiceHelper serviceHelper;

    @Autowired
    public CategoryService(
            ICategoryDao dao,
            ServiceHelper serviceHelper,
            IGenericConverter<CategoryEntity, CategoryDto> converter) {
        super(dao, converter);
        this.serviceHelper = serviceHelper;
    }

    @Override
    public CategoryDto create(CategoryDto dto) {
        LOGGER.debug("Entering method");
        validateDto(dto);
        return super.create(dto);
    }

    @Override
    public CategoryDto update(CategoryDto dto) {
        LOGGER.debug("Entering method");
        validateDtoWithId(dto);
        return super.update(dto);
    }

    @Override
    public CategoryDto save(CategoryDto dto) {
        LOGGER.debug("Entering method");
        validateDto(dto);
        return super.save(dto);
    }

    @Override
    public void delete(Integer id) {
        LOGGER.debug("Entering method");
        validateId(id);
        serviceHelper.checkCategoryDependentOperations(id);
        super.delete(id);
    }

    @Override
    protected void throwNotFoundException(Integer id) {
        LOGGER.debug("Entering method");
        throw new IncomeServiceCategoryNotFoundException(id);
    }

}
