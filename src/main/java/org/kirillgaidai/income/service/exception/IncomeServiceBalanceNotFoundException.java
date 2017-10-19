package org.kirillgaidai.income.service.exception;

import java.time.LocalDate;

public class IncomeServiceBalanceNotFoundException extends IncomeServiceNotFoundException {

    public IncomeServiceBalanceNotFoundException() {
        super("Balance not found");
    }

    public IncomeServiceBalanceNotFoundException(final String accountTitle, final LocalDate day) {
        super(String.format("Balance for account \"%s\" on %s not found", accountTitle, day.toString()));
    }

    public IncomeServiceBalanceNotFoundException(final Integer accountId, final LocalDate day) {
        super(String.format("Balance for account with id %d on %s not found", accountId, day.toString()));
    }

}
