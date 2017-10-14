package org.kirillgaidai.income.rest.mappers;

import org.kirillgaidai.income.rest.dto.IGenericCreateRestDto;
import org.kirillgaidai.income.rest.dto.IGenericGetRestDto;
import org.kirillgaidai.income.rest.dto.IGenericUpdateRestDto;
import org.kirillgaidai.income.service.dto.IGenericDto;

public interface IGenericRestDtoMapper<GT extends IGenericGetRestDto, CT extends IGenericCreateRestDto,
        UT extends IGenericUpdateRestDto, ST extends IGenericDto> {

    ST toDto(CT newRestDto);

    ST toDto(UT restDto);

    GT toRestDto(ST dto);

}
