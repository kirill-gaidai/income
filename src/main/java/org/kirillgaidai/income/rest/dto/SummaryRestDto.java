package org.kirillgaidai.income.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.kirillgaidai.income.rest.dto.account.AccountGetRestDto;
import org.kirillgaidai.income.rest.dto.balance.BalanceGetRestDto;
import org.kirillgaidai.income.rest.dto.category.CategoryGetRestDto;
import org.kirillgaidai.income.rest.dto.currency.CurrencyGetRestDto;
import org.kirillgaidai.income.rest.dto.operation.OperationGetRestDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public class SummaryRestDto {

    @NotNull
    @JsonProperty
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate firstDay;
    @NotNull
    @JsonProperty
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastDay;
    @NotNull
    @JsonProperty
    @Valid
    private CurrencyGetRestDto currency;
    @NotNull
    @JsonProperty
    @Valid
    private List<AccountGetRestDto> accounts;
    @NotNull
    @JsonProperty
    @Valid
    private List<CategoryGetRestDto> categories;
    @NotNull
    @JsonProperty
    @Valid
    private List<BalanceGetRestDto> initialBalances;
    @NotNull
    @JsonProperty
    @Valid
    private List<BalanceGetRestDto> balances;
    @NotNull
    @JsonProperty
    @Valid
    private List<OperationGetRestDto> operations;

    public SummaryRestDto() {
    }

    public SummaryRestDto(
            LocalDate firstDay, LocalDate lastDay, CurrencyGetRestDto currency, List<AccountGetRestDto> accounts,
            List<CategoryGetRestDto> categories, List<BalanceGetRestDto> initialBalances,
            List<BalanceGetRestDto> balances, List<OperationGetRestDto> operations) {
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.currency = currency;
        this.accounts = accounts;
        this.categories = categories;
        this.initialBalances = initialBalances;
        this.balances = balances;
        this.operations = operations;
    }

    public LocalDate getFirstDay() {
        return firstDay;
    }

    public void setFirstDay(LocalDate firstDay) {
        this.firstDay = firstDay;
    }

    public LocalDate getLastDay() {
        return lastDay;
    }

    public void setLastDay(LocalDate lastDay) {
        this.lastDay = lastDay;
    }

    public CurrencyGetRestDto getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyGetRestDto currency) {
        this.currency = currency;
    }

    public List<AccountGetRestDto> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountGetRestDto> accounts) {
        this.accounts = accounts;
    }

    public List<CategoryGetRestDto> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryGetRestDto> categories) {
        this.categories = categories;
    }

    public List<BalanceGetRestDto> getInitialBalances() {
        return initialBalances;
    }

    public void setInitialBalances(List<BalanceGetRestDto> initialBalances) {
        this.initialBalances = initialBalances;
    }

    public List<BalanceGetRestDto> getBalances() {
        return balances;
    }

    public void setBalances(List<BalanceGetRestDto> balances) {
        this.balances = balances;
    }

    public List<OperationGetRestDto> getOperations() {
        return operations;
    }

    public void setOperations(List<OperationGetRestDto> operations) {
        this.operations = operations;
    }

}
