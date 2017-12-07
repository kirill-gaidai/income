package org.kirillgaidai.income.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.kirillgaidai.income.rest.dto.account.AccountGetRestDto;
import org.kirillgaidai.income.rest.dto.balance.BalanceGetRestDto;
import org.kirillgaidai.income.rest.dto.category.CategoryGetRestDto;
import org.kirillgaidai.income.rest.dto.currency.CurrencyGetRestDto;
import org.kirillgaidai.income.rest.dto.operation.OperationGetRestDto;

import java.util.List;

public class SummaryRestDto {

    @JsonProperty
    private CurrencyGetRestDto currency;
    @JsonProperty
    private List<AccountGetRestDto> accounts;
    @JsonProperty
    private List<CategoryGetRestDto> categories;
    @JsonProperty
    private List<OperationGetRestDto> operations;
    @JsonProperty
    private List<BalanceGetRestDto> balances;

    public SummaryRestDto() {
    }

    public SummaryRestDto(
            CurrencyGetRestDto currency, List<AccountGetRestDto> accounts, List<CategoryGetRestDto> categories,
            List<OperationGetRestDto> operations, List<BalanceGetRestDto> balances) {
        this.currency = currency;
        this.accounts = accounts;
        this.categories = categories;
        this.operations = operations;
        this.balances = balances;
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

    public List<OperationGetRestDto> getOperations() {
        return operations;
    }

    public void setOperations(List<OperationGetRestDto> operations) {
        this.operations = operations;
    }

    public List<BalanceGetRestDto> getBalances() {
        return balances;
    }

    public void setBalances(List<BalanceGetRestDto> balances) {
        this.balances = balances;
    }

}
