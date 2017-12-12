package org.kirillgaidai.income.rest.dto.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.kirillgaidai.income.rest.dto.IGenericCreateRestDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CategoryCreateRestDto implements IGenericCreateRestDto {

    @JsonProperty
    @NotEmpty
    @Size(min = 1, max = 10)
    private String sort;
    @JsonProperty
    @NotEmpty
    @Size(min = 1, max = 250)
    private String title;

    public CategoryCreateRestDto() {
    }

    public CategoryCreateRestDto(String sort, String title) {
        this.sort = sort;
        this.title = title;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
