package org.kirillgaidai.income.service;

import org.kirillgaidai.income.dto.AccountDto;

import java.util.List;

public interface AccountService {

    List<AccountDto> getAccountList();

    AccountDto getAccountById(final Integer id);

    void saveAccount(final AccountDto accountDto);

    void deleteAccount(final Integer id);

}
