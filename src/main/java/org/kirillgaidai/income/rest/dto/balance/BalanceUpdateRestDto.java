package org.kirillgaidai.income.rest.dto.balance;

import org.kirillgaidai.income.rest.dto.IGenericUpdateRestDto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BalanceUpdateRestDto extends BalanceCreateRestDto implements IGenericUpdateRestDto {

    public BalanceUpdateRestDto() {
    }

    public BalanceUpdateRestDto(Integer accountId, LocalDate day, BigDecimal amount, Boolean manual) {
        super(accountId, day, amount, manual);
    }

}
