package org.kirillgaidai.income.rest.mappers;

import org.kirillgaidai.income.rest.dto.balance.BalanceGetRestDto;
import org.kirillgaidai.income.rest.dto.balance.BalanceUpdateRestDto;
import org.kirillgaidai.income.service.dto.BalanceDto;
import org.springframework.stereotype.Component;

@Component
public class BalanceRestDtoMapper
        implements IGenericRestDtoMapper<BalanceGetRestDto, BalanceUpdateRestDto, BalanceUpdateRestDto, BalanceDto> {

    public BalanceDto toDto(BalanceUpdateRestDto newRestDto) {
        return newRestDto == null ? null : new BalanceDto(newRestDto.getAccountId(), null, newRestDto.getDay(),
                newRestDto.getAmount(), newRestDto.getManual());
    }

    public BalanceGetRestDto toRestDto(BalanceDto dto) {
        return dto == null ? null : new BalanceGetRestDto(dto.getAccountId(), dto.getAccountTitle(), dto.getDay(),
                dto.getAmount(), dto.getManual());
    }

}
