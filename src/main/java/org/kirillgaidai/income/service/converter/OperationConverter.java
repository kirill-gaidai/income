package org.kirillgaidai.income.service.converter;

import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.service.dto.OperationDto;
import org.springframework.stereotype.Component;

@Component
public class OperationConverter implements IGenericConverter<OperationEntity, OperationDto> {

    @Override
    public OperationDto convertToDto(OperationEntity entity) {
        if (entity == null) {
            return null;
        }
        return new OperationDto(entity.getId(), entity.getAccountId(), null, entity.getCategoryId(), null,
                entity.getDay(), entity.getAmount(), entity.getNote());
    }

    @Override
    public OperationEntity convertToEntity(OperationDto dto) {
        if (dto == null) {
            return null;
        }
        return new OperationEntity(dto.getId(), dto.getAccountId(), dto.getCategoryId(), dto.getDay(), dto.getAmount(),
                dto.getNote());
    }

}
