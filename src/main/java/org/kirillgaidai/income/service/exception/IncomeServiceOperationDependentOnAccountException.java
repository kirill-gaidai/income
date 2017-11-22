package org.kirillgaidai.income.service.exception;

public class IncomeServiceOperationDependentOnAccountException extends IncomeServiceDependentException {

    public IncomeServiceOperationDependentOnAccountException(Integer operationId) {
        super(String.format("Operation dependent on account with id %d exist", operationId));
    }

}
