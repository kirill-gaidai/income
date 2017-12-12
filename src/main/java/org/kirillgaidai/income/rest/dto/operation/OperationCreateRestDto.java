package org.kirillgaidai.income.rest.dto.operation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.kirillgaidai.income.rest.dto.IGenericCreateRestDto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public class OperationCreateRestDto implements IGenericCreateRestDto {

    @JsonProperty
    @NotNull
    @Min(1)
    private Integer accountId;
    @JsonProperty
    @NotNull
    @Min(1)
    private Integer categoryId;
    @JsonProperty
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate day;
    @JsonProperty
    @NotNull
    @Digits(integer = 10, fraction = 4)
    private BigDecimal amount;
    @JsonProperty
    @NotNull
    @Size(max = 250)
    private String note;

    public OperationCreateRestDto() {
    }

    public OperationCreateRestDto(
            Integer accountId, Integer categoryId, LocalDate day, BigDecimal amount, String note) {
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.day = day;
        this.amount = amount;
        this.note = note;
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
