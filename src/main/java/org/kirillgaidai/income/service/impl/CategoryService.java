package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.BalanceDto;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.service.exception.IncomeServiceCategoryNotFoundException;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.kirillgaidai.income.service.intf.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryService extends SerialService<CategoryDto, CategoryEntity> implements ICategoryService {

    @Autowired
    public CategoryService(
            ICategoryDao dao,
            IGenericConverter<CategoryEntity, CategoryDto> converter) {
        super(converter, dao);
    }

    @Override
    protected void throwNotFoundException(Integer id) {
        throw new IncomeServiceCategoryNotFoundException(id);
    }

}
