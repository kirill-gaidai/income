package org.kirillgaidai.income.service.dto;

import java.time.LocalDate;
import java.util.List;

public class SummaryDto {

    private LocalDate firstDay;
    private LocalDate lastDay;
    private CurrencyDto currencyDto;
    private List<AccountDto> accountDtoList;
    private List<CategoryDto> categoryDtoList;
    private List<BalanceDto> initialBalanceDtoList;
    private List<BalanceDto> balanceDtoList;
    private List<OperationDto> operationDtoList;

    public SummaryDto() {
    }

    public SummaryDto(
            LocalDate firstDay, LocalDate lastDay, CurrencyDto currencyDto, List<AccountDto> accountDtoList,
            List<CategoryDto> categoryDtoList, List<BalanceDto> initialBalanceDtoList, List<BalanceDto> balanceDtoList,
            List<OperationDto> operationDtoList) {
        this.firstDay = firstDay;
        this.lastDay = lastDay;
        this.currencyDto = currencyDto;
        this.accountDtoList = accountDtoList;
        this.categoryDtoList = categoryDtoList;
        this.initialBalanceDtoList = initialBalanceDtoList;
        this.balanceDtoList = balanceDtoList;
        this.operationDtoList = operationDtoList;
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

    public CurrencyDto getCurrencyDto() {
        return currencyDto;
    }

    public void setCurrencyDto(CurrencyDto currencyDto) {
        this.currencyDto = currencyDto;
    }

    public List<AccountDto> getAccountDtoList() {
        return accountDtoList;
    }

    public void setAccountDtoList(List<AccountDto> accountDtoList) {
        this.accountDtoList = accountDtoList;
    }

    public List<CategoryDto> getCategoryDtoList() {
        return categoryDtoList;
    }

    public void setCategoryDtoList(List<CategoryDto> categoryDtoList) {
        this.categoryDtoList = categoryDtoList;
    }

    public List<BalanceDto> getInitialBalanceDtoList() {
        return initialBalanceDtoList;
    }

    public void setInitialBalanceDtoList(List<BalanceDto> initialBalanceDtoList) {
        this.initialBalanceDtoList = initialBalanceDtoList;
    }

    public List<BalanceDto> getBalanceDtoList() {
        return balanceDtoList;
    }

    public void setBalanceDtoList(List<BalanceDto> balanceDtoList) {
        this.balanceDtoList = balanceDtoList;
    }

    public List<OperationDto> getOperationDtoList() {
        return operationDtoList;
    }

    public void setOperationDtoList(List<OperationDto> operationDtoList) {
        this.operationDtoList = operationDtoList;
    }

}
