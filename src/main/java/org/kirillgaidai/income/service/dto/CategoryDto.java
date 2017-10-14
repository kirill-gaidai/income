package org.kirillgaidai.income.service.dto;

public class CategoryDto implements IGenericDto {

    private Integer id;
    private String sort;
    private String title;

    public CategoryDto() {
    }

    public CategoryDto(Integer id, String sort, String title) {
        this.id = id;
        this.sort = sort;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
