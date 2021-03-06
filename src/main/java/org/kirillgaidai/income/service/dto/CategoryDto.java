package org.kirillgaidai.income.service.dto;

public class CategoryDto implements ISerialDto {

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

    @Override
    public Integer getId() {
        return id;
    }

    @Override
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
