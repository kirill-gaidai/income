package org.kirillgaidai.income.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class IncomeControllerNotFoundException extends IncomeControllerException {

    public IncomeControllerNotFoundException() {
        super();
    }

    public IncomeControllerNotFoundException(final String message) {
        super(message);
    }

    public IncomeControllerNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public IncomeControllerNotFoundException(final Throwable cause) {
        super(cause);
    }

    public IncomeControllerNotFoundException(
            final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
