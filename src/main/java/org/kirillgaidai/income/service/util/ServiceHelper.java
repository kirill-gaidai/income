package org.kirillgaidai.income.service.util;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.dao.intf.IOperationDao;
import org.kirillgaidai.income.service.exception.IncomeServiceAccountDependentOnCurrencyException;
import org.kirillgaidai.income.service.exception.IncomeServiceAccountNotFoundException;
import org.kirillgaidai.income.service.exception.IncomeServiceBalanceDependentOnAccountException;
import org.kirillgaidai.income.service.exception.IncomeServiceCategoryNotFoundException;
import org.kirillgaidai.income.service.exception.IncomeServiceCurrencyNotFoundException;
import org.kirillgaidai.income.service.exception.IncomeServiceIntegrityException;
import org.kirillgaidai.income.service.exception.IncomeServiceOperationDependentOnAccountException;
import org.kirillgaidai.income.service.exception.IncomeServiceOperationDependentOnCategoryException;
import org.kirillgaidai.income.service.exception.IncomeServiceOperationNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceHelper {

    final private static Logger LOGGER = LoggerFactory.getLogger(ServiceHelper.class);

    final private IAccountDao accountDao;
    final private IBalanceDao balanceDao;
    final private ICategoryDao categoryDao;
    final private ICurrencyDao currencyDao;
    final private IOperationDao operationDao;

    @Autowired
    public ServiceHelper(
            IAccountDao accountDao,
            IBalanceDao balanceDao,
            ICategoryDao categoryDao,
            ICurrencyDao currencyDao,
            IOperationDao operationDao) {
        this.accountDao = accountDao;
        this.balanceDao = balanceDao;
        this.categoryDao = categoryDao;
        this.currencyDao = currencyDao;
        this.operationDao = operationDao;
    }

    public AccountEntity getAccountEntity(Integer accountId) {
        LOGGER.debug("Entering method");
        AccountEntity result = accountDao.get(accountId);
        if (result == null) {
            LOGGER.error("Account with id {} not found", accountId);
            throw new IncomeServiceAccountNotFoundException(accountId);
        }
        return result;
    }

    public CategoryEntity getCategoryEntity(Integer id) {
        LOGGER.debug("Entering method");
        CategoryEntity result = categoryDao.get(id);
        if (result == null) {
            LOGGER.error("Category with id {} not found", id);
            throw new IncomeServiceCategoryNotFoundException(id);
        }
        return result;
    }

    public CurrencyEntity getCurrencyEntity(Integer id) {
        LOGGER.debug("Entering method");
        CurrencyEntity result = currencyDao.get(id);
        if (result == null) {
            LOGGER.error("Currency with id {} not found", id);
            throw new IncomeServiceCurrencyNotFoundException(id);
        }
        return result;
    }

    public OperationEntity getOperationEntity(Integer operationId) {
        LOGGER.debug("Entering method");
        OperationEntity result = operationDao.get(operationId);
        if (result == null) {
            LOGGER.error("Operation with id {} not found", operationId);
            throw new IncomeServiceOperationNotFoundException(operationId);
        }
        return result;
    }

    public void checkAccountDependentBalances(Integer accountId) {
        LOGGER.debug("Entering method");
        if (balanceDao.getCountByAccountId(accountId) != 0) {
            LOGGER.error("Balances dependent on account with id {} exist", accountId);
            throw new IncomeServiceBalanceDependentOnAccountException(accountId);
        }
    }

    public void checkAccountDependentOperations(Integer accountId) {
        LOGGER.debug("Entering method");
        if (operationDao.getCountByAccountId(accountId) != 0) {
            LOGGER.error("Operations dependent on account with id {} exist", accountId);
            throw new IncomeServiceOperationDependentOnAccountException(accountId);
        }
    }

    public void checkCategoryDependentOperations(Integer categoryId) {
        LOGGER.debug("Entering method");
        if (operationDao.getCountByCategoryId(categoryId) != 0) {
            LOGGER.error("Operations dependent on category with id {} exist", categoryId);
            throw new IncomeServiceOperationDependentOnCategoryException(categoryId);
        }
    }

    public void checkCurrencyDependentAccounts(Integer currencyId) {
        LOGGER.debug("Entering method");
        if (accountDao.getCountByCurrencyId(currencyId) != 0) {
            LOGGER.error("Accounts dependent on currency with id {} exist", currencyId);
            throw new IncomeServiceAccountDependentOnCurrencyException(currencyId);
        }
    }

    public void deleteAccount(AccountEntity accountEntity) {
        LOGGER.debug("Entering method");
        if (accountDao.delete(accountEntity) != 1) {
            LOGGER.error("Integrity error when deleting account with id {}", accountEntity.getId());
            throw new IncomeServiceIntegrityException();
        }
    }

}
