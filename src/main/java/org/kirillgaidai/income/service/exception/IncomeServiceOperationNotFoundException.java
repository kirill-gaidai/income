package org.kirillgaidai.income.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Operation not found")
public class IncomeServiceOperationNotFoundException extends IncomeServiceNotFoundException {

    public IncomeServiceOperationNotFoundException(final Integer id) {
        super(String.format("Operation with id %d not found", id));
    }

}
