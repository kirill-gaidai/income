package org.kirillgaidai.income.dao.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RateEntity implements IGenericEntity {

    private Integer id;
    private Integer currencyIdFrom;
    private Integer currencyIdTo;
    private LocalDate day;
    private BigDecimal value;

    public RateEntity() {
    }

    public RateEntity(Integer id, Integer currencyIdFrom, Integer currencyIdTo, LocalDate day, BigDecimal value) {
        this.id = id;
        this.currencyIdFrom = currencyIdFrom;
        this.currencyIdTo = currencyIdTo;
        this.day = day;
        this.value = value;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
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
