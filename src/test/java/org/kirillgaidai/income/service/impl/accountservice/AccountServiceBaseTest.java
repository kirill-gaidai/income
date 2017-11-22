package org.kirillgaidai.income.service.impl.accountservice;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.impl.AccountDao;
import org.kirillgaidai.income.dao.impl.CurrencyDao;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.service.converter.AccountConverter;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.impl.AccountService;
import org.kirillgaidai.income.service.intf.IAccountService;

import static org.mockito.Mockito.mock;

public abstract class AccountServiceBaseTest {

    final protected IAccountDao accountDao = mock(AccountDao.class);
    final protected ICurrencyDao currencyDao = mock(CurrencyDao.class);
    final protected IGenericConverter<AccountEntity, AccountDto> converter = mock(AccountConverter.class);
    final protected IAccountService service = new AccountService(accountDao, currencyDao, converter);

}
