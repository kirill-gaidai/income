package org.kirillgaidai.income.service.intf;

import org.kirillgaidai.income.service.dto.BalanceDto;

import java.time.LocalDate;
import java.util.List;

public interface IBalanceService {

    BalanceDto getDto(Integer accountId, LocalDate day);

    void saveDto(BalanceDto dto);

}
