package org.kirillgaidai.income.service.exception.optimistic;

public class IncomeServiceOptimisticCreateException extends IncomeServiceOptimisticException {

    public IncomeServiceOptimisticCreateException() {
        super("Entity create exception");
    }

    public IncomeServiceOptimisticCreateException(String message) {
        super(message);
    }

}
