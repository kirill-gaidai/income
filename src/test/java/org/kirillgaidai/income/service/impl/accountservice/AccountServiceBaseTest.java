package org.kirillgaidai.income.service.impl.accountservice;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.service.converter.AccountConverter;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.impl.AccountService;
import org.kirillgaidai.income.service.impl.ServiceBaseTest;
import org.kirillgaidai.income.service.intf.IAccountService;

public abstract class AccountServiceBaseTest extends ServiceBaseTest {

    final protected IGenericConverter<AccountEntity, AccountDto> converter = new AccountConverter();
    final protected IAccountService service = new AccountService(accountDao, currencyDao, serviceHelper, converter);

}
