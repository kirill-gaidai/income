package org.kirillgaidai.income.service.intf;

import org.kirillgaidai.income.service.dto.AccountDto;

import java.util.List;

public interface IAccountService extends ISerialService<AccountDto> {

    List<AccountDto> getList(Integer currencyId);

}
