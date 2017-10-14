package org.kirillgaidai.income.service.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public class OperationDto implements IGenericDto {

    private Integer id;
    private Integer accountId;
    private String accountTitle;
    private Integer categoryId;
    private String categoryTitle;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate day;
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private BigDecimal amount;
    private String note;

    public OperationDto() {
    }

    public OperationDto(
            Integer id, Integer accountId, String accountTitle, Integer categoryId, String categoryTitle, LocalDate day,
            BigDecimal amount, String note) {
        this.id = id;
        this.accountId = accountId;
        this.accountTitle = accountTitle;
        this.categoryId = categoryId;
        this.categoryTitle = categoryTitle;
        this.day = day;
        this.amount = amount;
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
