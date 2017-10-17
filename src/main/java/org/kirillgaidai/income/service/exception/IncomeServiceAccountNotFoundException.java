package org.kirillgaidai.income.service.exception;

public class IncomeServiceAccountNotFoundException extends IncomeServiceNotFoundException {

    public IncomeServiceAccountNotFoundException() {
        super("Account not found");
    }

    public IncomeServiceAccountNotFoundException(Integer id) {
        super(String.format("Account with id %d not found", id));
    }

}
