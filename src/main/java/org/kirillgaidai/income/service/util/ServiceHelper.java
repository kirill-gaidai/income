package org.kirillgaidai.income.service.util;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.entity.IGenericEntity;
import org.kirillgaidai.income.dao.entity.ISerialEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.dao.entity.UserEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.dao.intf.IGenericDao;
import org.kirillgaidai.income.dao.intf.IOperationDao;
import org.kirillgaidai.income.dao.intf.ISerialDao;
import org.kirillgaidai.income.dao.intf.IUserDao;
import org.kirillgaidai.income.service.exception.IncomeServiceDependentOnException;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticCreateException;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticDeleteException;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticUpdateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ServiceHelper {

    final private static Logger LOGGER = LoggerFactory.getLogger(ServiceHelper.class);

    final private IAccountDao accountDao;
    final private IBalanceDao balanceDao;
    final private ICategoryDao categoryDao;
    final private ICurrencyDao currencyDao;
    final private IOperationDao operationDao;
    final private IUserDao userDao;

    @Autowired
    public ServiceHelper(
            IAccountDao accountDao,
            IBalanceDao balanceDao,
            ICategoryDao categoryDao,
            ICurrencyDao currencyDao,
            IOperationDao operationDao,
            IUserDao userDao) {
        this.accountDao = accountDao;
        this.balanceDao = balanceDao;
        this.categoryDao = categoryDao;
        this.currencyDao = currencyDao;
        this.operationDao = operationDao;
        this.userDao = userDao;
    }

    public AccountEntity getAccountEntity(Integer accountId) {
        LOGGER.debug("Entering method");
        return getEntity(accountId, accountDao, AccountEntity.class);
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
        return getEntity(categoryId, categoryDao, CategoryEntity.class);
    }

    public CurrencyEntity getCurrencyEntity(Integer currencyId) {
        LOGGER.debug("Entering method");
        return getEntity(currencyId, currencyDao, CurrencyEntity.class);
    }

    public OperationEntity getOperationEntity(Integer operationId) {
        LOGGER.debug("Entering method");
        return getEntity(operationId, operationDao, OperationEntity.class);
    }

    public UserEntity getUserEntity(Integer userId) {
        LOGGER.debug("Entering method");
        return getEntity(userId, userDao, UserEntity.class);
    }

    private <E extends ISerialEntity> E getEntity(Integer id, ISerialDao<E> dao, Class<E> clazz) {
        LOGGER.debug("Entering method");
        E result = dao.get(id);
        if (result == null) {
            String message = String.format("%s with id %d not found", getEntityName(clazz), id);
            LOGGER.error(message);
            throw new IncomeServiceNotFoundException(message);
        }
        return result;
    }

    public List<AccountEntity> getAccountListByIds(Set<Integer> ids) {
        LOGGER.debug("Entering method");
        return getListByIds(ids, accountDao, AccountEntity.class);
    }

    public List<CategoryEntity> getCategoryListByIds(Set<Integer> ids) {
        LOGGER.debug("Entering method");
        return getListByIds(ids, categoryDao, CategoryEntity.class);
    }

    public List<CurrencyEntity> getCurrencyListByIds(Set<Integer> ids) {
        LOGGER.debug("Entering method");
        return getListByIds(ids, currencyDao, CurrencyEntity.class);
    }

    public List<OperationEntity> getOperationListByIds(Set<Integer> ids) {
        LOGGER.debug("Entering method");
        return getListByIds(ids, operationDao, OperationEntity.class);
    }

    public List<UserEntity> getUserListByIds(Set<Integer> ids) {
        LOGGER.debug("Entering method");
        return getListByIds(ids, userDao, UserEntity.class);
    }

    private <E extends ISerialEntity> List<E> getListByIds(Set<Integer> ids, ISerialDao<E> dao, Class<E> clazz) {
        LOGGER.debug("Entering method");
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        List<E> result = dao.getList(ids);
        if (result.size() != ids.size()) {
            Set<Integer> resultIds = result.stream().map(E::getId).collect(Collectors.toSet());
            for (Integer id : ids) {
                if (!resultIds.contains(id)) {
                    String message = String.format("%s with id %d not found", getEntityName(clazz), id);
                    LOGGER.error(message);
                    throw new IncomeServiceNotFoundException(message);
                }
            }
        }
        return result;
    }

    public void createAccountEntity(AccountEntity accountEntity) {
        LOGGER.debug("Entering method");
        createEntity(accountEntity, accountDao, AccountEntity.class);
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
        createEntity(categoryEntity, categoryDao, CategoryEntity.class);
    }

    public void createCurrencyEntity(CurrencyEntity currencyEntity) {
        LOGGER.debug("Entering method");
        createEntity(currencyEntity, currencyDao, CurrencyEntity.class);
    }

    public void createOperationEntity(OperationEntity operationEntity) {
        LOGGER.debug("Entering method");
        createEntity(operationEntity, operationDao, OperationEntity.class);
    }

    public void createUserEntity(UserEntity userEntity) {
        LOGGER.debug("Entering method");
        createEntity(userEntity, userDao, UserEntity.class);
    }

    private <E extends ISerialEntity> void createEntity(E entity, IGenericDao<E> dao, Class<E> clazz) {
        LOGGER.debug("Entering method");
        if (dao.insert(entity) != 1) {
            String message = String.format("%s create failure", getEntityName(clazz));
            LOGGER.error(message);
            throw new IncomeServiceOptimisticCreateException(message);
        }
    }

    public void updateAccountEntity(AccountEntity newAccountEntity, AccountEntity oldAccountEntity) {
        LOGGER.debug("Entering method");
        updateEntity(newAccountEntity, oldAccountEntity, accountDao, AccountEntity.class);
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
        updateEntity(newCategoryEntity, oldCategoryEntity, categoryDao, CategoryEntity.class);
    }

    public void updateCurrencyEntity(CurrencyEntity newCurrencyEntity, CurrencyEntity oldCurrencyEntity) {
        LOGGER.debug("Entering method");
        updateEntity(newCurrencyEntity, oldCurrencyEntity, currencyDao, CurrencyEntity.class);
    }

    public void updateOperationEntity(OperationEntity newOperationEntity, OperationEntity oldOperationEntity) {
        LOGGER.debug("Entering method");
        updateEntity(newOperationEntity, oldOperationEntity, operationDao, OperationEntity.class);
    }

    public void updateUserEntity(UserEntity newUserEntity, UserEntity oldUserEntity) {
        LOGGER.debug("Entering method");
        updateEntity(newUserEntity, oldUserEntity, userDao, UserEntity.class);
    }

    private <E extends ISerialEntity> void updateEntity(E newEntity, E oldEntity, ISerialDao<E> dao, Class<E> clazz) {
        LOGGER.debug("Entering method");
        if (dao.update(newEntity, oldEntity) != 1) {
            String message = String.format("%s with id %d update failure", getEntityName(clazz), newEntity.getId());
            LOGGER.error(message);
            throw new IncomeServiceOptimisticUpdateException(message);
        }
    }

    public void deleteAccountEntity(AccountEntity accountEntity) {
        LOGGER.debug("Entering method");
        deleteEntity(accountEntity, accountDao, AccountEntity.class);
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
        deleteEntity(categoryEntity, categoryDao, CategoryEntity.class);
    }

    public void deleteCurrencyEntity(CurrencyEntity currencyEntity) {
        LOGGER.debug("Entering method");
        deleteEntity(currencyEntity, currencyDao, CurrencyEntity.class);
    }

    public void deleteOperationEntity(OperationEntity operationEntity) {
        LOGGER.debug("Entering method");
        deleteEntity(operationEntity, operationDao, OperationEntity.class);
    }

    public void deleteUserEntity(UserEntity userEntity) {
        LOGGER.debug("Entering method");
        deleteEntity(userEntity, userDao, UserEntity.class);
    }

    private <E extends ISerialEntity> void deleteEntity(E entity, ISerialDao<E> dao, Class<E> clazz) {
        LOGGER.debug("Entering method");
        if (dao.delete(entity) != 1) {
            String message = String.format("%s with id %d delete failure", getEntityName(clazz), entity.getId());
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

    public void checkAccountAndDayDependentOperations(Integer accountId, LocalDate day) {
        LOGGER.debug("Entering method");
        if (operationDao.getCountByAccountIdAndDay(accountId, day) != 0) {
            String message = String.format("Operations dependent on account with id %d on %s found", accountId, day);
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

    private String getEntityName(Class<? extends IGenericEntity> clazz) {
        String className = clazz.getName();
        return className.substring(className.lastIndexOf(".") + 1, className.length() - 6);
    }

}
