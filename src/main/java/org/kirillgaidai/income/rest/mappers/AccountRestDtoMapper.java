package org.kirillgaidai.income.rest.mappers;

import org.kirillgaidai.income.rest.dto.account.AccountCreateRestDto;
import org.kirillgaidai.income.rest.dto.account.AccountGetRestDto;
import org.kirillgaidai.income.rest.dto.account.AccountUpdateRestDto;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.springframework.stereotype.Component;

@Component
public class AccountRestDtoMapper
        implements IGenericRestDtoMapper<AccountGetRestDto, AccountCreateRestDto, AccountUpdateRestDto, AccountDto> {

    @Override
    public AccountDto toDto(AccountCreateRestDto newRestDto) {
        return newRestDto == null ? null : new AccountDto(null, newRestDto.getCurrencyId(), null, null,
                newRestDto.getSort(), newRestDto.getTitle());
    }

    @Override
    public AccountDto toDto(AccountUpdateRestDto restDto) {
        return restDto == null ? null : new AccountDto(restDto.getId(), restDto.getCurrencyId(), null, null,
                restDto.getSort(), restDto.getTitle());
    }

    @Override
    public AccountGetRestDto toRestDto(AccountDto dto) {
        return dto == null ? null : new AccountGetRestDto(dto.getId(), dto.getCurrencyId(), dto.getCurrencyCode(),
                dto.getCurrencyTitle(), dto.getSort(), dto.getTitle());
    }

}
