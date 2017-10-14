package org.kirillgaidai.income.rest.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.kirillgaidai.income.rest.dto.IGenericUpdateRestDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CategoryUpdateRestDto extends CategoryCreateRestDto implements IGenericUpdateRestDto {

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
