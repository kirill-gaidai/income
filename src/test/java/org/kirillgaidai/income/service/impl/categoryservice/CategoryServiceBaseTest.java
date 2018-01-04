package org.kirillgaidai.income.service.impl.categoryservice;

import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.service.converter.CategoryConverter;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.impl.CategoryService;
import org.kirillgaidai.income.service.impl.ServiceBaseTest;
import org.kirillgaidai.income.service.intf.ICategoryService;

public abstract class CategoryServiceBaseTest extends ServiceBaseTest {

    final protected IGenericConverter<CategoryEntity, CategoryDto> converter = new CategoryConverter();
    final protected ICategoryService service = new CategoryService(categoryDao, serviceHelper, converter);

}
