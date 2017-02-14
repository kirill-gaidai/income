package org.kirillgaidai.income.dto.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.kirillgaidai.income.dto.CategoryDto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlRootElement(name = "categories")
@XmlType(propOrder = {"categoryDtoList"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(value = {"categories"})
public class CategoryListDto {

    private List<CategoryDto> categoryDtoList;

    @XmlElement(name = "category")
    @JsonProperty(value = "categories")
    public List<CategoryDto> getCategoryDtoList() {
        return categoryDtoList;
    }

    public void setCategoryDtoList(List<CategoryDto> categoryDtoList) {
        this.categoryDtoList = categoryDtoList;
    }

}
