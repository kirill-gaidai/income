package org.kirillgaidai.income.rest.dto.category;

import org.kirillgaidai.income.rest.dto.IGenericGetRestDto;

public class CategoryGetRestDto extends CategoryUpdateRestDto implements IGenericGetRestDto {

    public CategoryGetRestDto() {
    }

    public CategoryGetRestDto(Integer id, String sort, String title) {
        super(id, sort, title);
    }

}
