package org.kirillgaidai.income.service.dto;

public class AccountDto implements IGenericDto {

    private Integer id;
    private Integer currencyId;
    private String currencyCode;
    private String currencyTitle;
    private String sort;
    private String title;

    public AccountDto() {
    }

    public AccountDto(
            Integer id, Integer currencyId, String currencyCode, String currencyTitle, String sort, String title) {
        this.id = id;
        this.currencyId = currencyId;
        this.currencyCode = currencyCode;
        this.currencyTitle = currencyTitle;
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

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyTitle() {
        return currencyTitle;
    }

    public void setCurrencyTitle(String currencyTitle) {
        this.currencyTitle = currencyTitle;
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
