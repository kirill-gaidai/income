package org.kirillgaidai.income.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class SummaryDto {

    private List<AccountDto> accountDtoList;
    private List<CategoryDto> categoryDtoList;
    private List<SummaryDtoRow> summaryDtoRowList;
    private List<BigDecimal> totalAmounts;
    private BigDecimal totalAmountsSummary;

    public SummaryDto() {
    }

    public SummaryDto(
            List<AccountDto> accountDtoList,
            List<CategoryDto> categoryDtoList,
            List<SummaryDtoRow> summaryDtoRowList,
            List<BigDecimal> totalAmounts,
            BigDecimal totalAmountsSummary) {
        this.accountDtoList = accountDtoList;
        this.categoryDtoList = categoryDtoList;
        this.summaryDtoRowList = summaryDtoRowList;
        this.totalAmounts = totalAmounts;
        this.totalAmountsSummary = totalAmountsSummary;
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

    public List<SummaryDtoRow> getSummaryDtoRowList() {
        return summaryDtoRowList;
    }

    public void setSummaryDtoRowList(List<SummaryDtoRow> summaryDtoRowList) {
        this.summaryDtoRowList = summaryDtoRowList;
    }

    public List<BigDecimal> getTotalAmounts() {
        return totalAmounts;
    }

    public void setTotalAmounts(List<BigDecimal> totalAmounts) {
        this.totalAmounts = totalAmounts;
    }

    public BigDecimal getTotalAmountsSummary() {
        return totalAmountsSummary;
    }

    public void setTotalAmountsSummary(BigDecimal totalAmountsSummary) {
        this.totalAmountsSummary = totalAmountsSummary;
    }

    public static class SummaryDtoRow {

        private LocalDate day;
        private BigDecimal difference;
        private List<BigDecimal> balances;
        private BigDecimal balancesSummary;
        private List<BigDecimal> amounts;
        private BigDecimal amountsSummary;

        public SummaryDtoRow() {
        }

        public SummaryDtoRow(
                LocalDate day, BigDecimal difference, List<BigDecimal> balances, BigDecimal balancesSummary,
                List<BigDecimal> amounts, BigDecimal amountsSummary) {
            this.day = day;
            this.difference = difference;
            this.balances = balances;
            this.balancesSummary = balancesSummary;
            this.amounts = amounts;
            this.amountsSummary = amountsSummary;
        }

        public LocalDate getDay() {
            return day;
        }

        public void setDay(LocalDate day) {
            this.day = day;
        }

        public BigDecimal getDifference() {
            return difference;
        }

        public void setDifference(BigDecimal difference) {
            this.difference = difference;
        }

        public List<BigDecimal> getBalances() {
            return balances;
        }

        public void setBalances(List<BigDecimal> balances) {
            this.balances = balances;
        }

        public BigDecimal getBalancesSummary() {
            return balancesSummary;
        }

        public void setBalancesSummary(BigDecimal balancesSummary) {
            this.balancesSummary = balancesSummary;
        }

        public List<BigDecimal> getAmounts() {
            return amounts;
        }

        public void setAmounts(List<BigDecimal> amounts) {
            this.amounts = amounts;
        }

        public BigDecimal getAmountsSummary() {
            return amountsSummary;
        }

        public void setAmountsSummary(BigDecimal amountsSummary) {
            this.amountsSummary = amountsSummary;
        }

    }

}
