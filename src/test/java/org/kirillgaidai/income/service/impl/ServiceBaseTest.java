package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.dao.intf.IOperationDao;
import org.kirillgaidai.income.dao.intf.IUserDao;
import org.kirillgaidai.income.service.util.ServiceHelper;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

public abstract class ServiceBaseTest {

    final protected IAccountDao accountDao = mock(IAccountDao.class);
    final protected IBalanceDao balanceDao = mock(IBalanceDao.class);
    final protected ICategoryDao categoryDao = mock(ICategoryDao.class);
    final protected ICurrencyDao currencyDao = mock(ICurrencyDao.class);
    final protected IOperationDao operationDao = mock(IOperationDao.class);
    final protected IUserDao userDao = mock(IUserDao.class);
    final protected ServiceHelper serviceHelper =
            new ServiceHelper(accountDao, balanceDao, categoryDao, currencyDao, operationDao, userDao);

    public void verifyNoMoreDaoInteractions() {
        Mockito.verifyNoMoreInteractions(accountDao, balanceDao, categoryDao, currencyDao, operationDao, userDao);
    }

}
