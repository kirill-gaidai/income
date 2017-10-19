package org.kirillgaidai.income.web.exception;

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
