package org.kirillgaidai.income.rest.dto.balance;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.kirillgaidai.income.rest.dto.IGenericCreateRestDto;
import org.kirillgaidai.income.rest.dto.IGenericUpdateRestDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BalanceUpdateRestDto implements IGenericCreateRestDto, IGenericUpdateRestDto {

    @JsonProperty
    @NotNull
    @Min(1)
    private Integer accountId;
    @JsonProperty
    @NotNull
    private LocalDate day;
    @JsonProperty
    @NotNull
    private BigDecimal amount;
    @JsonProperty
    @NotNull
    private Boolean manual;

    public BalanceUpdateRestDto() {
    }

    public BalanceUpdateRestDto(Integer accountId, LocalDate day, BigDecimal amount, Boolean manual) {
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
