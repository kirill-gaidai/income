package org.kirillgaidai.income.exception;

public class IncomeException extends RuntimeException {

    public IncomeException() {
        super();
    }

    public IncomeException(String message) {
        super(message);
    }

    public IncomeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncomeException(Throwable cause) {
        super(cause);
    }

    public IncomeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
