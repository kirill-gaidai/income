package org.kirillgaidai.income.rest.mappers;

import org.kirillgaidai.income.rest.dto.operation.OperationCreateRestDto;
import org.kirillgaidai.income.rest.dto.operation.OperationGetRestDto;
import org.kirillgaidai.income.rest.dto.operation.OperationUpdateRestDto;
import org.kirillgaidai.income.service.dto.OperationDto;
import org.springframework.stereotype.Component;

@Component
public class OperationRestDtoMapper implements
        IGenericRestDtoMapper<OperationGetRestDto, OperationCreateRestDto, OperationUpdateRestDto, OperationDto> {

    @Override
    public OperationDto toDto(OperationCreateRestDto newRestDto) {
        return newRestDto == null ? null : new OperationDto(null, newRestDto.getAccountId(), null,
                newRestDto.getCategoryId(), null, newRestDto.getDay(), newRestDto.getAmount(), newRestDto.getNote());
    }

    @Override
    public OperationDto toDto(OperationUpdateRestDto restDto) {
        return restDto == null ? null : new OperationDto(restDto.getId(), restDto.getAccountId(), null,
                restDto.getCategoryId(), null, restDto.getDay(), restDto.getAmount(), restDto.getNote());
    }

    @Override
    public OperationGetRestDto toRestDto(OperationDto dto) {
        return dto == null ? null : new OperationGetRestDto(dto.getId(), dto.getAccountId(), dto.getAccountTitle(),
                dto.getCategoryId(), dto.getCategoryTitle(), dto.getDay(), dto.getAmount(), dto.getNote());
    }

}
