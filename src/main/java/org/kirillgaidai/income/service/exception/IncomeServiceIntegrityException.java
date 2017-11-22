package org.kirillgaidai.income.service.exception;

public class IncomeServiceIntegrityException extends IncomeServiceException {

    public IncomeServiceIntegrityException() {
        super("Data integrity exception");
    }

}
