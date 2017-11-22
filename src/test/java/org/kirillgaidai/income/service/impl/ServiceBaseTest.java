package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.impl.AccountDao;
import org.kirillgaidai.income.dao.impl.BalanceDao;
import org.kirillgaidai.income.dao.impl.CategoryDao;
import org.kirillgaidai.income.dao.impl.CurrencyDao;
import org.kirillgaidai.income.dao.impl.OperationDao;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.dao.intf.IOperationDao;
import org.kirillgaidai.income.service.util.ServiceHelper;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

public abstract class ServiceBaseTest {

    final protected IAccountDao accountDao = mock(IAccountDao.class);
    final protected IBalanceDao balanceDao = mock(IBalanceDao.class);
    final protected ICategoryDao categoryDao = mock(ICategoryDao.class);
    final protected ICurrencyDao currencyDao = mock(ICurrencyDao.class);
    final protected IOperationDao operationDao = mock(IOperationDao.class);
    final protected ServiceHelper serviceHelper =
            new ServiceHelper(accountDao, balanceDao, categoryDao, currencyDao, operationDao);

    public void verifyNoMoreInteractions() {
        Mockito.verifyNoMoreInteractions(accountDao, balanceDao, categoryDao, currencyDao, operationDao);
    }

}
