package org.kirillgaidai.income.dao.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BalanceEntity implements IGenericEntity {

    private Integer accountId;
    private LocalDate day;
    private BigDecimal amount;
    private Boolean manual;

    public BalanceEntity() {
    }

    public BalanceEntity(Integer accountId, LocalDate day, BigDecimal amount, Boolean manual) {
        this.accountId = accountId;
        this.day = day;
        this.amount = amount;
        this.manual = manual;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getManual() {
        return manual;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }

}
