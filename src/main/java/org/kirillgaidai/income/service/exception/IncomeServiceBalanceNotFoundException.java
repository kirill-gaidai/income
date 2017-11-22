package org.kirillgaidai.income.service.exception;

import java.time.LocalDate;

/**
 * Balance not found exception
 *
 * @author Kirill Gaidai
 */
public class IncomeServiceBalanceNotFoundException extends IncomeServiceNotFoundException {

    /**
     * Constructor
     *
     * @param accountId  account id
     * @param day        day
     * @param precedence before day (-1), on day (0), after day (1)
     */
    public IncomeServiceBalanceNotFoundException(Integer accountId, LocalDate day, int precedence) {
        super(String.format(
                precedence < 0 ? "Balance for account with id %d before %s not found" :
                precedence == 0 ? "Balance for account with id %d on %s not found" :
                "Balance for account with id %d after %s not found", accountId, day.toString()));
    }

}
