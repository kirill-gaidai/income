package org.kirillgaidai.income.rest.dto.category;

import org.kirillgaidai.income.rest.dto.ISerialGetRestDto;

public class CategoryGetRestDto extends CategoryUpdateRestDto implements ISerialGetRestDto {

    public CategoryGetRestDto() {
    }

    public CategoryGetRestDto(Integer id, String sort, String title) {
        super(id, sort, title);
    }

}
