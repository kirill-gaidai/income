package org.kirillgaidai.income.service.exception.optimistic;

import org.kirillgaidai.income.service.exception.IncomeServiceException;

public class IncomeServiceOptimisticException extends IncomeServiceException {

    public IncomeServiceOptimisticException(String message) {
        super(message);
    }

}
