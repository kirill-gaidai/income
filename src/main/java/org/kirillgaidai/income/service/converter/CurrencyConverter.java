package org.kirillgaidai.income.service.converter;

import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.service.dto.CurrencyDto;
import org.springframework.stereotype.Component;

@Component
public class CurrencyConverter implements IGenericConverter<CurrencyEntity, CurrencyDto> {

    @Override
    public CurrencyDto convertToDto(CurrencyEntity entity) {
        if (entity == null) {
            return null;
        }
        return new CurrencyDto(entity.getId(), entity.getCode(), entity.getTitle());
    }

    @Override
    public CurrencyEntity convertToEntity(CurrencyDto dto) {
        if (dto == null) {
            return null;
        }
        return new CurrencyEntity(dto.getId(), dto.getCode(), dto.getTitle());
    }

}
