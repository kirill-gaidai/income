package org.kirillgaidai.income.dao.entity;

/**
 * Currency entity
 *
 * @author Kirill Gaidai
 */
public class CurrencyEntity implements ISerialEntity {

    private Integer id;
    private String code;
    private String title;
    private Integer accuracy;

    public CurrencyEntity() {
    }

    public CurrencyEntity(Integer id, String code, String title, Integer accuracy) {
        this.id = id;
        this.code = code;
        this.title = title;
        this.accuracy = accuracy;
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

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

}
