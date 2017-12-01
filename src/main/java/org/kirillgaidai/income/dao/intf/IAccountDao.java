package org.kirillgaidai.income.dao.intf;

import org.kirillgaidai.income.dao.entity.AccountEntity;

import java.util.List;

/**
 * Account DAO
 *
 * @author Kirill Gaidai
 */
public interface IAccountDao extends ISerialDao<AccountEntity> {

    List<AccountEntity> getList(Integer currencyId);

    int getCountByCurrencyId(Integer currencyId);

}
