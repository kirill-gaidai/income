package org.kirillgaidai.income.service.exception.optimistic;

public class IncomeServiceOptimisticUpdateException extends IncomeServiceOptimisticException {

    public IncomeServiceOptimisticUpdateException() {
        super("Entity update failure");
    }

    public IncomeServiceOptimisticUpdateException(Integer id) {
        super(String.format("Entity with id %d update failure", id));
    }

    public IncomeServiceOptimisticUpdateException(String message) {
        super(message);
    }

}
