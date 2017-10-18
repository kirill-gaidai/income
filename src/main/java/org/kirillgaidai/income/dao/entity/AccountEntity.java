package org.kirillgaidai.income.dao.entity;

/**
 * Account entity
 *
 * @author Kirill Gaidai
 */
public class AccountEntity implements ISerialEntity {

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

    @Override
    public Integer getId() {
        return id;
    }

    @Override
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
