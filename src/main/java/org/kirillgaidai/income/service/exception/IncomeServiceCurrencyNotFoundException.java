package org.kirillgaidai.income.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Currency not found")
public class IncomeServiceCurrencyNotFoundException extends IncomeServiceNotFoundException {

    public IncomeServiceCurrencyNotFoundException() {
        super("Currency not found");
    }

    public IncomeServiceCurrencyNotFoundException(final Integer id) {
        super(String.format("Currency with id %d not found", id));
    }

}
