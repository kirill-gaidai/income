package org.kirillgaidai.income.exception;

public class IncomeException extends RuntimeException {

    public IncomeException(final String message, final Object... args) {
        super(String.format(message, args));
    }

}
