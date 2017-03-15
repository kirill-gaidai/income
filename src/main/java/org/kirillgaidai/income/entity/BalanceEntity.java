package org.kirillgaidai.income.entity;

import java.math.BigDecimal;
import java.util.Date;

public class BalanceEntity {

    private Integer accountId;
    private Date day;
    private BigDecimal amount;
    private Boolean fixed;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean getFixed() {
        return fixed;
    }

    public void setFixed(Boolean fixed) {
        this.fixed = fixed;
    }

}
