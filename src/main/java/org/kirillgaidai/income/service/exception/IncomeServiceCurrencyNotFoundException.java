package org.kirillgaidai.income.service.exception;

public class IncomeServiceCurrencyNotFoundException extends IncomeServiceNotFoundException {

    public IncomeServiceCurrencyNotFoundException() {
        super("Currency not found");
    }

    public IncomeServiceCurrencyNotFoundException(Integer id) {
        super(String.format("Currency with id %d not found", id));
    }

}
