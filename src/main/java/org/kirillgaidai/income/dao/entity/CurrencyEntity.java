package org.kirillgaidai.income.dao.entity;

public class CurrencyEntity implements IGenericEntity {

    private Integer id;
    private String code;
    private String title;

    public CurrencyEntity() {
    }

    public CurrencyEntity(Integer id, String code, String title) {
        this.id = id;
        this.code = code;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
