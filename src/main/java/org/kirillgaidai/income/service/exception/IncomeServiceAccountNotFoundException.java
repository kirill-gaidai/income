package org.kirillgaidai.income.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Account not found")
public class IncomeServiceAccountNotFoundException extends IncomeServiceNotFoundException {

    public IncomeServiceAccountNotFoundException() {
        super("Account not found");
    }

    public IncomeServiceAccountNotFoundException(final Integer id) {
        super(String.format("Account with id %d not found", id));
    }

}
