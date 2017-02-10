package org.kirillgaidai.income.exception;

public class IncomeDaoException extends RuntimeException {

    public IncomeDaoException(final String message, final Object... args) {
        super(String.format(message, args));
    }

}
