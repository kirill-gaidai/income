package org.kirillgaidai.income.exception;

public class IncomeNotFoundDaoException extends IncomeDaoException {

    public IncomeNotFoundDaoException(final String message, final Object... args) {
        super(message, args);
    }

}
