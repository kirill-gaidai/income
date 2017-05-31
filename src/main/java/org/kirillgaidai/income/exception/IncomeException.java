package org.kirillgaidai.income.exception;

public class IncomeException extends RuntimeException {

    public IncomeException() {
        super();
    }

    public IncomeException(final String message) {
        super(message);
    }

    public IncomeException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public IncomeException(final Throwable cause) {
        super(cause);
    }

    public IncomeException(
            final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
