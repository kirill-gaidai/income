package org.kirillgaidai.income.service.exception;

public class IncomeServiceAccountDependentOnCurrencyException extends IncomeServiceDependentException {

    public IncomeServiceAccountDependentOnCurrencyException(Integer currencyId) {
        super(String.format("Account dependent on сгккутсн with id %d exist", currencyId));
    }

}
