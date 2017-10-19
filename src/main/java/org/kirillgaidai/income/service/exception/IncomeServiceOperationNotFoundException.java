package org.kirillgaidai.income.service.exception;

public class IncomeServiceOperationNotFoundException extends IncomeServiceNotFoundException {

    public IncomeServiceOperationNotFoundException(Integer id) {
        super(String.format("Operation with id %d not found", id));
    }

}
