package org.kirillgaidai.income.dao.entity;

/**
 * Category entity
 *
 * @author Kirill Gaidai
 */
public class CategoryEntity implements ISerialEntity {

    private Integer id;
    private String sort;
    private String title;

    public CategoryEntity() {
    }

    public CategoryEntity(Integer id, String sort, String title) {
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
