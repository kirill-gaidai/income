package org.kirillgaidai.income.service.exception;

public class IncomeServiceOperationDependentOnCategoryException extends IncomeServiceDependentException {

    public IncomeServiceOperationDependentOnCategoryException(Integer categoryId) {
        super(String.format("Operation dependent on category with id %d exist", categoryId));
    }

}
