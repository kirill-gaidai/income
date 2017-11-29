package org.kirillgaidai.income.service.exception.optimistic;

public class IncomeServiceOptimisticDeleteException extends IncomeServiceOptimisticException {

    public IncomeServiceOptimisticDeleteException() {
        super("Entity delete failure");
    }

    public IncomeServiceOptimisticDeleteException(Integer id) {
        super(String.format("Entity with id %d delete failure", id));
    }

    public IncomeServiceOptimisticDeleteException(String message) {
        super(message);
    }

}
