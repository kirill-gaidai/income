package org.kirillgaidai.income.service.converter;

import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter implements IGenericConverter<CategoryEntity, CategoryDto> {

    @Override
    public CategoryDto convertToDto(CategoryEntity entity) {
        if (entity == null) {
            return null;
        }
        return new CategoryDto(entity.getId(), entity.getSort(), entity.getTitle());
    }

    @Override
    public CategoryEntity convertToEntity(CategoryDto dto) {
        if (dto == null) {
            return null;
        }
        return new CategoryEntity(dto.getId(), dto.getSort(), dto.getTitle());
    }

}
