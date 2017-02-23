package org.kirillgaidai.income.dao;

import org.kirillgaidai.income.entity.AccountEntity;

import java.util.List;

public interface AccountDao {

    List<AccountEntity> getAccountList();

    AccountEntity getAccountById(final Integer id);

    int insertAccount(final AccountEntity accountEntity);

    int updateAccount(final AccountEntity accountEntity);

    int deleteAccount(final Integer id);

}
