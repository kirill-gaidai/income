package org.kirillgaidai.income.dao.entity;

public class AccountEntity implements IGenericEntity {

    private Integer id;
    private Integer currencyId;
    private String sort;
    private String title;

    public AccountEntity() {
    }

    public AccountEntity(Integer id, Integer currencyId, String sort, String title) {
        this.id = id;
        this.currencyId = currencyId;
        this.sort = sort;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
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
