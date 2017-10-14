package org.kirillgaidai.income.rest.mappers;

import org.kirillgaidai.income.rest.dto.category.CategoryCreateRestDto;
import org.kirillgaidai.income.rest.dto.category.CategoryGetRestDto;
import org.kirillgaidai.income.rest.dto.category.CategoryUpdateRestDto;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.springframework.stereotype.Component;

@Component
public class CategoryRestDtoMapper implements
        IGenericRestDtoMapper<CategoryGetRestDto, CategoryCreateRestDto, CategoryUpdateRestDto, CategoryDto> {

    @Override
    public CategoryDto toDto(CategoryCreateRestDto newRestDto) {
        return newRestDto == null ? null : new CategoryDto(null, newRestDto.getSort(), newRestDto.getTitle());
    }

    @Override
    public CategoryDto toDto(CategoryUpdateRestDto restDto) {
        return restDto == null ? null : new CategoryDto(restDto.getId(), restDto.getSort(), restDto.getTitle());
    }

    @Override
    public CategoryGetRestDto toRestDto(CategoryDto dto) {
        return dto == null ? null : new CategoryGetRestDto(dto.getId(), dto.getSort(), dto.getTitle());
    }

}
