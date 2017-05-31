package org.kirillgaidai.income.service.converter;

import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.service.dto.BalanceDto;
import org.springframework.stereotype.Component;

@Component
public class BalanceConverter implements IGenericConverter<BalanceEntity, BalanceDto> {

    @Override
    public BalanceDto convertToDto(BalanceEntity entity) {
        if (entity == null) {
            return null;
        }
        return new BalanceDto(entity.getAccountId(), null, entity.getDay(), entity.getAmount(), entity.getManual());
    }

    @Override
    public BalanceEntity convertToEntity(BalanceDto dto) {
        if (dto == null) {
            return null;
        }
        return new BalanceEntity(dto.getAccountId(), dto.getDay(), dto.getAmount(), dto.getManual());
    }

}
