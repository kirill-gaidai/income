package org.kirillgaidai.income.service.dto;

public class CurrencyDto implements ISerialDto {

    private Integer id;
    private String code;
    private String title;
    private Integer accuracy;

    public CurrencyDto() {
    }

    public CurrencyDto(Integer id, String code, String title, Integer accuracy) {
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
