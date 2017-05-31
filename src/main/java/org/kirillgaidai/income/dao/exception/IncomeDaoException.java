package org.kirillgaidai.income.dao.exception;

import org.kirillgaidai.income.exception.IncomeException;

public class IncomeDaoException extends IncomeException {

    public IncomeDaoException() {
        super();
    }

    public IncomeDaoException(final String message) {
        super(message);
    }

    public IncomeDaoException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public IncomeDaoException(final Throwable cause) {
        super(cause);
    }

    public IncomeDaoException(
            final String message, final Throwable cause, final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
