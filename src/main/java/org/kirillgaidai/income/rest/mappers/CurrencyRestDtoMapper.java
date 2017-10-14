package org.kirillgaidai.income.rest.mappers;

import org.kirillgaidai.income.rest.dto.currency.CurrencyCreateRestDto;
import org.kirillgaidai.income.rest.dto.currency.CurrencyGetRestDto;
import org.kirillgaidai.income.rest.dto.currency.CurrencyUpdateRestDto;
import org.kirillgaidai.income.service.dto.CurrencyDto;
import org.springframework.stereotype.Component;

@Component
public class CurrencyRestDtoMapper implements
        IGenericRestDtoMapper<CurrencyGetRestDto, CurrencyCreateRestDto, CurrencyUpdateRestDto, CurrencyDto> {

    @Override
    public CurrencyDto toDto(CurrencyCreateRestDto newRestDto) {
        return newRestDto == null ? null : new CurrencyDto(null, newRestDto.getCode(), newRestDto.getTitle(),
                newRestDto.getAccuracy());
    }

    @Override
    public CurrencyDto toDto(CurrencyUpdateRestDto restDto) {
        return restDto == null ? null : new CurrencyDto(restDto.getId(), restDto.getCode(), restDto.getTitle(),
                restDto.getAccuracy());
    }

    @Override
    public CurrencyGetRestDto toRestDto(CurrencyDto dto) {
        return dto == null ? null : new CurrencyGetRestDto(dto.getId(), dto.getCode(), dto.getTitle(),
                dto.getAccuracy());
    }

}
