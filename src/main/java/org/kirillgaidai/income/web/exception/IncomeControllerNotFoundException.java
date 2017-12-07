package org.kirillgaidai.income.web.exception;

public class IncomeControllerNotFoundException extends IncomeControllerException {

    public IncomeControllerNotFoundException() {
        super();
    }

    public IncomeControllerNotFoundException(String message) {
        super(message);
    }

    public IncomeControllerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public IncomeControllerNotFoundException(Throwable cause) {
        super(cause);
    }

    public IncomeControllerNotFoundException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
