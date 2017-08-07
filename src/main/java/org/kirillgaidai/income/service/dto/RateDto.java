package org.kirillgaidai.income.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RateDto {

    private Integer currencyIdFrom;
    private String currencyCodeFrom;
    private String currencyTitleFrom;
    private Integer currencyIdTo;
    private String currencyCodeTo;
    private String currencyTitleTo;
    private LocalDate day;
    private BigDecimal value;

    public RateDto() {
    }

    public RateDto(
            Integer currencyIdFrom, String currencyCodeFrom, String currencyTitleFrom, Integer currencyIdTo,
            String currencyCodeTo, String currencyTitleTo, LocalDate day, BigDecimal value) {
        this.currencyIdFrom = currencyIdFrom;
        this.currencyCodeFrom = currencyCodeFrom;
        this.currencyTitleFrom = currencyTitleFrom;
        this.currencyIdTo = currencyIdTo;
        this.currencyCodeTo = currencyCodeTo;
        this.currencyTitleTo = currencyTitleTo;
        this.day = day;
        this.value = value;
    }

    public Integer getCurrencyIdFrom() {
        return currencyIdFrom;
    }

    public void setCurrencyIdFrom(Integer currencyIdFrom) {
        this.currencyIdFrom = currencyIdFrom;
    }

    public String getCurrencyCodeFrom() {
        return currencyCodeFrom;
    }

    public void setCurrencyCodeFrom(String currencyCodeFrom) {
        this.currencyCodeFrom = currencyCodeFrom;
    }

    public String getCurrencyTitleFrom() {
        return currencyTitleFrom;
    }

    public void setCurrencyTitleFrom(String currencyTitleFrom) {
        this.currencyTitleFrom = currencyTitleFrom;
    }

    public Integer getCurrencyIdTo() {
        return currencyIdTo;
    }

    public void setCurrencyIdTo(Integer currencyIdTo) {
        this.currencyIdTo = currencyIdTo;
    }

    public String getCurrencyCodeTo() {
        return currencyCodeTo;
    }

    public void setCurrencyCodeTo(String currencyCodeTo) {
        this.currencyCodeTo = currencyCodeTo;
    }

    public String getCurrencyTitleTo() {
        return currencyTitleTo;
    }

    public void setCurrencyTitleTo(String currencyTitleTo) {
        this.currencyTitleTo = currencyTitleTo;
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
