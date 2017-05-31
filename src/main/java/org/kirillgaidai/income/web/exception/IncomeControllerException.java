package org.kirillgaidai.income.web.exception;

import org.kirillgaidai.income.exception.IncomeException;

public class IncomeControllerException extends IncomeException {

    public IncomeControllerException() {
    }

    public IncomeControllerException(String message) {
        super(message);
    }

    public IncomeControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncomeControllerException(Throwable cause) {
        super(cause);
    }

    public IncomeControllerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
