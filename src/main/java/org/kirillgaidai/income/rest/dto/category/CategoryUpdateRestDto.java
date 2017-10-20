package org.kirillgaidai.income.rest.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.kirillgaidai.income.rest.dto.ISerialUpdateRestDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CategoryUpdateRestDto extends CategoryCreateRestDto implements ISerialUpdateRestDto {

    @JsonProperty
    @NotNull
    @Min(1)
    private Integer id;

    public CategoryUpdateRestDto() {
    }

    public CategoryUpdateRestDto(Integer id, String sort, String title) {
        super(sort, title);
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

}
