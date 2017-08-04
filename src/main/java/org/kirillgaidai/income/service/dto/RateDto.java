package org.kirillgaidai.income.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RateDto {

    private Integer fromCurrencyId;
    private String fromCurrencyCode;
    private String fromCurrencyTitle;
    private Integer toCurrencyId;
    private String toCurrencyCode;
    private String toCurrencyTitle;
    private LocalDate day;
    private BigDecimal value;

    public RateDto() {
    }

    public RateDto(
            Integer fromCurrencyId, String fromCurrencyCode, String fromCurrencyTitle, Integer toCurrencyId,
            String toCurrencyCode, String toCurrencyTitle, LocalDate day, BigDecimal value) {
        this.fromCurrencyId = fromCurrencyId;
        this.fromCurrencyCode = fromCurrencyCode;
        this.fromCurrencyTitle = fromCurrencyTitle;
        this.toCurrencyId = toCurrencyId;
        this.toCurrencyCode = toCurrencyCode;
        this.toCurrencyTitle = toCurrencyTitle;
        this.day = day;
        this.value = value;
    }

    public Integer getFromCurrencyId() {
        return fromCurrencyId;
    }

    public void setFromCurrencyId(Integer fromCurrencyId) {
        this.fromCurrencyId = fromCurrencyId;
    }

    public String getFromCurrencyCode() {
        return fromCurrencyCode;
    }

    public void setFromCurrencyCode(String fromCurrencyCode) {
        this.fromCurrencyCode = fromCurrencyCode;
    }

    public String getFromCurrencyTitle() {
        return fromCurrencyTitle;
    }

    public void setFromCurrencyTitle(String fromCurrencyTitle) {
        this.fromCurrencyTitle = fromCurrencyTitle;
    }

    public Integer getToCurrencyId() {
        return toCurrencyId;
    }

    public void setToCurrencyId(Integer toCurrencyId) {
        this.toCurrencyId = toCurrencyId;
    }

    public String getToCurrencyCode() {
        return toCurrencyCode;
    }

    public void setToCurrencyCode(String toCurrencyCode) {
        this.toCurrencyCode = toCurrencyCode;
    }

    public String getToCurrencyTitle() {
        return toCurrencyTitle;
    }

    public void setToCurrencyTitle(String toCurrencyTitle) {
        this.toCurrencyTitle = toCurrencyTitle;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

}
