package org.kirillgaidai.income.service.util;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.dao.intf.IOperationDao;
import org.kirillgaidai.income.service.exception.IncomeServiceDependentOnException;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticCreateException;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticDeleteException;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticUpdateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

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
            String message = String.format("Account with id %d not found", accountId);
            LOGGER.error(message);
            throw new IncomeServiceNotFoundException(message);
        }
        return result;
    }

    public BalanceEntity getBalanceEntity(Integer accountId, LocalDate day, int precedence) {
        LOGGER.debug("Entering method");

        if (precedence < 0) {
            BalanceEntity result = balanceDao.getBefore(accountId, day);
            if (result == null) {
                String message = String.format("Balance for account with id %d before %s not found", accountId, day);
                LOGGER.error(message);
                throw new IncomeServiceNotFoundException(message);
            }
            return result;
        }

        if (precedence == 0) {
            BalanceEntity result = balanceDao.get(accountId, day);
            if (result == null) {
                String message = String.format("Balance for account with id %d on %s not found", accountId, day);
                LOGGER.error(message);
                throw new IncomeServiceNotFoundException(message);
            }
            return result;
        }

        BalanceEntity result = balanceDao.getAfter(accountId, day);
        if (result == null) {
            String message = String.format("Balance for account with id %d after %s not found", accountId, day);
            LOGGER.error(message);
            throw new IncomeServiceNotFoundException(message);
        }
        return result;
    }

    public CategoryEntity getCategoryEntity(Integer categoryId) {
        LOGGER.debug("Entering method");
        CategoryEntity result = categoryDao.get(categoryId);
        if (result == null) {
            String message = String.format("Category with id %d not found", categoryId);
            LOGGER.error(message);
            throw new IncomeServiceNotFoundException(message);
        }
        return result;
    }

    public CurrencyEntity getCurrencyEntity(Integer currencyId) {
        LOGGER.debug("Entering method");
        CurrencyEntity result = currencyDao.get(currencyId);
        if (result == null) {
            String message = String.format("Currency with id %d not found", currencyId);
            LOGGER.error(message);
            throw new IncomeServiceNotFoundException(message);
        }
        return result;
    }

    public OperationEntity getOperationEntity(Integer operationId) {
        LOGGER.debug("Entering method");
        OperationEntity result = operationDao.get(operationId);
        if (result == null) {
            String message = String.format("Operation with id %d not found", operationId);
            LOGGER.error(message);
            throw new IncomeServiceNotFoundException(message);
        }
        return result;
    }

    public void createAccountEntity(AccountEntity accountEntity) {
        LOGGER.debug("Entering method");
        if (accountDao.insert(accountEntity) != 1) {
            String message = "Account create failure";
            LOGGER.error(message);
            throw new IncomeServiceOptimisticCreateException(message);
        }
    }

    public void createBalanceEntity(BalanceEntity balanceEntity) {
        LOGGER.debug("Entering method");
        if (balanceDao.insert(balanceEntity) != 1) {
            String message = String.format("Balance for account with id %d on %s create failure",
                    balanceEntity.getAccountId(), balanceEntity.getDay());
            LOGGER.error(message);
            throw new IncomeServiceOptimisticCreateException(message);
        }
    }

    public void createCategoryEntity(CategoryEntity categoryEntity) {
        LOGGER.debug("Entering method");
        if (categoryDao.insert(categoryEntity) != 1) {
            String message = "Category create failure";
            LOGGER.error(message);
            throw new IncomeServiceOptimisticCreateException(message);
        }
    }

    public void createCurrencyEntity(CurrencyEntity currencyEntity) {
        LOGGER.debug("Entering method");
        if (currencyDao.insert(currencyEntity) != 1) {
            String message = "Currency create failure";
            LOGGER.error(message);
            throw new IncomeServiceOptimisticCreateException(message);
        }
    }

    public void createOperationEntity(OperationEntity operationEntity) {
        LOGGER.debug("Entering method");
        if (operationDao.insert(operationEntity) != 1) {
            String message = "Operation create failure";
            LOGGER.error(message);
            throw new IncomeServiceOptimisticCreateException(message);
        }
    }

    public void updateAccountEntity(AccountEntity newAccountEntity, AccountEntity oldAccountEntity) {
        LOGGER.debug("Entering method");
        if (accountDao.update(newAccountEntity, oldAccountEntity) != 1) {
            String message = String.format("Account with id %d update failure", newAccountEntity.getId());
            LOGGER.error(message);
            throw new IncomeServiceOptimisticUpdateException(message);
        }
    }

    public void updateBalanceEntity(BalanceEntity newBalanceEntity, BalanceEntity oldBalanceEntity) {
        LOGGER.debug("Entering method");
        if (balanceDao.update(newBalanceEntity, oldBalanceEntity) != 1) {
            String message = String.format("Balance for account with id %d on %s update failure",
                    newBalanceEntity.getAccountId(), newBalanceEntity.getDay());
            LOGGER.error(message);
            throw new IncomeServiceOptimisticUpdateException(message);
        }
    }

    public void updateCategoryEntity(CategoryEntity newCategoryEntity, CategoryEntity oldCategoryEntity) {
        LOGGER.debug("Entering method");
        if (categoryDao.update(newCategoryEntity, oldCategoryEntity) != 1) {
            String message = String.format("Category with id %d update failure", newCategoryEntity.getId());
            LOGGER.error(message);
            throw new IncomeServiceOptimisticUpdateException(message);
        }
    }

    public void updateCurrencyEntity(CurrencyEntity newCurrencyEntity, CurrencyEntity oldCurrencyEntity) {
        LOGGER.debug("Entering method");
        if (currencyDao.update(newCurrencyEntity, oldCurrencyEntity) != 1) {
            String message = String.format("Currency with id %d update failure", newCurrencyEntity.getId());
            LOGGER.error(message);
            throw new IncomeServiceOptimisticUpdateException(message);
        }
    }

    public void updateOperationEntity(OperationEntity newOperationEntity, OperationEntity oldOperationEntity) {
        LOGGER.debug("Entering method");
        if (operationDao.update(newOperationEntity, oldOperationEntity) != 1) {
            String message = String.format("Operation with id %d update failure", newOperationEntity.getId());
            LOGGER.error(message);
            throw new IncomeServiceOptimisticUpdateException(message);
        }
    }

    public void deleteAccountEntity(AccountEntity accountEntity) {
        LOGGER.debug("Entering method");
        if (accountDao.delete(accountEntity) != 1) {
            String message = String.format("Account with id %d delete failure", accountEntity.getId());
            LOGGER.error(message);
            throw new IncomeServiceOptimisticDeleteException(message);
        }
    }

    public void deleteBalanceEntity(BalanceEntity balanceEntity) {
        LOGGER.debug("Entering method");
        if (balanceDao.delete(balanceEntity) != 1) {
            String message = String.format("Balance for account with id %d on %s delete failure",
                    balanceEntity.getAccountId(), balanceEntity.getDay());
            LOGGER.error(message);
            throw new IncomeServiceOptimisticDeleteException(message);
        }
    }

    public void deleteCategoryEntity(CategoryEntity categoryEntity) {
        LOGGER.debug("Entering method");
        if (categoryDao.delete(categoryEntity) != 1) {
            String message = String.format("Category with id %d delete failure", categoryEntity.getId());
            LOGGER.error(message);
            throw new IncomeServiceOptimisticDeleteException(message);
        }
    }

    public void deleteCurrencyEntity(CurrencyEntity currencyEntity) {
        LOGGER.debug("Entering meyhod");
        if (currencyDao.delete(currencyEntity) != 1) {
            String message = String.format("Currency with id %d delete failure", currencyEntity.getId());
            LOGGER.error(message);
            throw new IncomeServiceOptimisticDeleteException(message);
        }
    }

    public void deleteOperationEntity(OperationEntity operationEntity) {
        LOGGER.debug("Entering method");
        if (operationDao.delete(operationEntity) != 1) {
            String message = String.format("Operation with id %d delete failure", operationEntity.getId());
            LOGGER.error(message);
            throw new IncomeServiceOptimisticDeleteException(message);
        }
    }

    public void checkAccountDependentBalances(Integer accountId) {
        LOGGER.debug("Entering method");
        if (balanceDao.getCountByAccountId(accountId) != 0) {
            String message = String.format("Balances dependent on account with id %d found", accountId);
            LOGGER.error(message);
            throw new IncomeServiceDependentOnException(message);
        }
    }

    public void checkAccountDependentOperations(Integer accountId) {
        LOGGER.debug("Entering method");
        if (operationDao.getCountByAccountId(accountId) != 0) {
            String message = String.format("Operations dependent on account with id %d found", accountId);
            LOGGER.error(message);
            throw new IncomeServiceDependentOnException(message);
        }
    }

    public void checkCategoryDependentOperations(Integer categoryId) {
        LOGGER.debug("Entering method");
        if (operationDao.getCountByCategoryId(categoryId) != 0) {
            String message = String.format("Operations dependent on category with id %d found", categoryId);
            LOGGER.error(message);
            throw new IncomeServiceDependentOnException(message);
        }
    }

    public void checkCurrencyDependentAccounts(Integer currencyId) {
        LOGGER.debug("Entering method");
        if (accountDao.getCountByCurrencyId(currencyId) != 0) {
            String message = String.format("Accounts dependent on currency with id %d found", currencyId);
            LOGGER.error(message);
            throw new IncomeServiceDependentOnException(message);
        }
    }

}
