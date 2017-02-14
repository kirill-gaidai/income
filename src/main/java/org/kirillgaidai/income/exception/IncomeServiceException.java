package org.kirillgaidai.income.exception;

public class IncomeServiceException extends RuntimeException {

    public IncomeServiceException(final String message, final Object... args) {
        super (String.format(message, args));
    }

}
