package org.kirillgaidai.income.service.exception;

public class IncomeServiceBalanceDependentOnAccountException extends IncomeServiceDependentException {

    public IncomeServiceBalanceDependentOnAccountException(Integer accountId) {
        super(String.format("Balance dependent on account with id %d exist", accountId));
    }

}
