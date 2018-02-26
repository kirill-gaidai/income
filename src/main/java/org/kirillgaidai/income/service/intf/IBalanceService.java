package org.kirillgaidai.income.service.intf;

import org.kirillgaidai.income.service.dto.BalanceDto;

import java.time.LocalDate;

public interface IBalanceService extends IGenericService<BalanceDto> {

    BalanceDto get(Integer accountId, LocalDate day);

    void delete(Integer accountId, LocalDate day);

}
