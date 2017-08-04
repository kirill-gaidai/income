package org.kirillgaidai.income.dao.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RateEntity {

    private Integer currencyIdFrom;
    private Integer currencyIdTo;
    private LocalDate day;
    private BigDecimal value;

    public RateEntity() {
    }

    public RateEntity(Integer currencyIdFrom, Integer currencyIdTo, LocalDate day, BigDecimal value) {
        this.currencyIdFrom = currencyIdFrom;
        this.currencyIdTo = currencyIdTo;
        this.day = day;
        this.value = value;
    }

    public Integer getCurrencyIdFrom() {
        return currencyIdFrom;
    }

    public void setCurrencyIdFrom(Integer currencyIdFrom) {
        this.currencyIdFrom = currencyIdFrom;
    }

    public Integer getCurrencyIdTo() {
        return currencyIdTo;
    }

    public void setCurrencyIdTo(Integer currencyIdTo) {
        this.currencyIdTo = currencyIdTo;
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
