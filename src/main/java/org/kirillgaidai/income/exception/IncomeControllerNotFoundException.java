package org.kirillgaidai.income.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class IncomeControllerNotFoundException extends IncomeControllerException {

    public IncomeControllerNotFoundException(final String message, final Object... args) {
        super(message, args);
    }

}
