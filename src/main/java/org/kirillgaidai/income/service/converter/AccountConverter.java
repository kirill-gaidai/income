package org.kirillgaidai.income.service.converter;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.springframework.stereotype.Component;

@Component
public class AccountConverter implements IGenericConverter<AccountEntity, AccountDto> {

    @Override
    public AccountDto convertToDto(AccountEntity entity) {
        if (entity == null) {
            return null;
        }
        return new AccountDto(entity.getId(), entity.getCurrencyId(), null, null, entity.getSort(), entity.getTitle());
    }

    @Override
    public AccountEntity convertToEntity(AccountDto dto) {
        if (dto == null) {
            return null;
        }
        return new AccountEntity(dto.getId(), dto.getCurrencyId(), dto.getSort(), dto.getTitle());
    }

}
