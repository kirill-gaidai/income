package org.kirillgaidai.income.service.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BalanceDto {

    private Integer accountId;
    private String accountTitle;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate day;
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private BigDecimal amount;
    private Boolean manual;

    public BalanceDto() {
    }

    public BalanceDto(Integer accountId, String accountTitle, LocalDate day, BigDecimal amount, Boolean manual) {
        this.accountId = accountId;
        this.accountTitle = accountTitle;
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

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
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
