package org.kirillgaidai.income.dao.impl;

import org.junit.After;
import org.junit.runner.RunWith;
import org.kirillgaidai.income.dao.config.PersistenceTestConfig;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.dao.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceTestConfig.class)
public abstract class DaoBaseTest {

    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @After
    public void tearDown() throws Exception {
        deleteAccountEntities();
        deleteCategoryEntities();
        deleteCurrencyEntities();
        deleteBalanceEntities();
        deleteOperationEntities();
        deleteUserEntities();
    }

    protected void insertAccountEntity(AccountEntity entity) {
        String sql = "INSERT INTO accounts(id, currency_id, sort, title) VALUES(:id, :currency_id, :sort, :title)";
        Map<String, Object> params = new HashMap<>();
        params.put("id", entity.getId());
        params.put("currency_id", entity.getCurrencyId());
        params.put("sort", entity.getSort());
        params.put("title", entity.getTitle());
        namedParameterJdbcTemplate.update(sql, params);
    }

    protected void insertBalanceEntity(BalanceEntity entity) {
        String sql =
                "INSERT INTO balances(account_id, day, amount, manual) VALUES(:account_id, :day, :amount, :manual)";
        Map<String, Object> params = new HashMap<>();
        params.put("account_id", entity.getAccountId());
        params.put("day", Date.valueOf(entity.getDay()));
        params.put("amount", entity.getAmount());
        params.put("manual", entity.getManual());
        namedParameterJdbcTemplate.update(sql, params);
    }

    protected void insertCategoryEntity(CategoryEntity entity) {
        String sql = "INSERT INTO categories(id, sort, title) VALUES(:id, :sort, :title)";
        Map<String, Object> params = new HashMap<>();
        params.put("id", entity.getId());
        params.put("sort", entity.getSort());
        params.put("title", entity.getTitle());
        namedParameterJdbcTemplate.update(sql, params);
    }

    protected void insertCurrencyEntity(CurrencyEntity entity) {
        String sql = "INSERT INTO currencies(id, code, title, accuracy) VALUES(:id, :code, :title, :accuracy)";
        Map<String, Object> params = new HashMap<>();
        params.put("id", entity.getId());
        params.put("code", entity.getCode());
        params.put("title", entity.getTitle());
        params.put("accuracy", entity.getAccuracy());
        namedParameterJdbcTemplate.update(sql, params);
    }

    protected void insertOperationEntity(OperationEntity entity) {
        String sql = "INSERT INTO operations(id, account_id, category_id, day, amount, note) " +
                "VALUES(:id, :account_id, :category_id, :day, :amount, :note)";
        Map<String, Object> params = new HashMap<>();
        params.put("id", entity.getId());
        params.put("account_id", entity.getAccountId());
        params.put("category_id", entity.getCategoryId());
        params.put("day", entity.getDay());
        params.put("amount", entity.getAmount());
        params.put("note", entity.getNote());
        namedParameterJdbcTemplate.update(sql, params);
    }

    protected void insertUserEntity(UserEntity entity) {
        String sql = "INSERT INTO users(id, login, password, admin, blocked, token, expires) " +
                "VALUES(:id, :login, :password, :admin, :blocked, :token, :expires)";
        Map<String, Object> params = new HashMap<>();
        params.put("id", entity.getId());
        params.put("login", entity.getLogin());
        params.put("password", entity.getPassword());
        params.put("admin", entity.getAdmin());
        params.put("blocked", entity.getBlocked());
        params.put("token", entity.getToken());
        params.put("expires", Timestamp.valueOf(entity.getExpires()));
        namedParameterJdbcTemplate.update(sql, params);
    }

    protected void deleteAccountEntities() {
        namedParameterJdbcTemplate.update("DELETE FROM accounts", Collections.emptyMap());
    }

    protected void deleteBalanceEntities() {
        namedParameterJdbcTemplate.update("DELETE FROM balances", Collections.emptyMap());
    }

    protected void deleteCategoryEntities() {
        namedParameterJdbcTemplate.update("DELETE FROM categories", Collections.emptyMap());
    }

    protected void deleteCurrencyEntities() {
        namedParameterJdbcTemplate.update("DELETE FROM currencies", Collections.emptyMap());
    }

    protected void deleteOperationEntities() {
        namedParameterJdbcTemplate.update("DELETE FROM operations", Collections.emptyMap());
    }

    protected void deleteUserEntities() {
        namedParameterJdbcTemplate.update("DELETE FROM users", Collections.emptyMap());
    }

}
