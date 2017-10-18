package org.kirillgaidai.income.dao.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Operation entity
 *
 * @author Kirill Gaidai
 */
public class OperationEntity implements ISerialEntity {

    private Integer id;
    private Integer accountId;
    private Integer categoryId;
    private LocalDate day;
    private BigDecimal amount;
    private String note;

    public OperationEntity() {
    }

    public OperationEntity(
            Integer id, Integer accountId, Integer categoryId, LocalDate day, BigDecimal amount, String note) {
        this.id = id;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.day = day;
        this.amount = amount;
        this.note = note;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
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
