package org.kirillgaidai.income.rest.dto.balance;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public class BalanceGetRestDto extends BalanceUpdateRestDto {

    @JsonProperty
    @NotNull
    @Size(min = 1, max = 250)
    private String accountTitle;

    public BalanceGetRestDto() {
    }

    public BalanceGetRestDto(Integer accountId, String accountTitle, LocalDate day, BigDecimal amount, Boolean manual) {
        super(accountId, day, amount, manual);
        this.accountTitle = accountTitle;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

}
