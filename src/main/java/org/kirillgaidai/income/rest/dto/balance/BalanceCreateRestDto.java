package org.kirillgaidai.income.rest.dto.balance;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.kirillgaidai.income.rest.dto.IGenericCreateRestDto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BalanceCreateRestDto implements IGenericCreateRestDto {

    @JsonProperty
    @NotNull
    @Min(1)
    private Integer accountId;
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
    private Boolean manual;

    public BalanceCreateRestDto() {
    }

    public BalanceCreateRestDto(Integer accountId, LocalDate day, BigDecimal amount, Boolean manual) {
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
