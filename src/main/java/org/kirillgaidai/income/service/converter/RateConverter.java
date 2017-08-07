package org.kirillgaidai.income.service.converter;

import org.kirillgaidai.income.dao.entity.RateEntity;
import org.kirillgaidai.income.service.dto.RateDto;
import org.springframework.stereotype.Component;

@Component
public class RateConverter implements IGenericConverter<RateEntity, RateDto> {

    @Override
    public RateDto convertToDto(RateEntity entity) {
        if (entity == null) {
            return null;
        }
        return new RateDto(entity.getCurrencyIdFrom(), null, null, entity.getCurrencyIdTo(), null, null,
                entity.getDay(), entity.getValue());
    }

    @Override
    public RateEntity convertToEntity(RateDto dto) {
        if (dto == null) {
            return null;
        }
        return new RateEntity(dto.getCurrencyIdFrom(), dto.getCurrencyIdTo(), dto.getDay(), dto.getValue());
    }

}
