package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.entity.UserEntity;
import org.kirillgaidai.income.dao.intf.IUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDao extends SerialDao<UserEntity> implements IUserDao {

    final private static Logger LOGGER = LoggerFactory.getLogger(UserDao.class);

    @Autowired
    public UserDao(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            RowMapper<UserEntity> rowMapper) {
        super(namedParameterJdbcTemplate, rowMapper);
    }

    @Override
    protected String getGetListSql() {
        LOGGER.debug("Entering method");
        return "SELECT id, login, password, admin, blocked, token, expires FROM users ORDER BY login";
    }

    @Override
    protected String getGetListByIdsSql() {
        LOGGER.debug("Entering method");
        return "SELECT id, login, password, admin, blocked, token, expires " +
                "FROM users WHERE id IN (:ids) ORDER BY login";
    }

    @Override
    protected String getGetByIdSql() {
        LOGGER.debug("Entering method");
        return "SELECT id, login, password, admin, blocked, token, expires FROM users WHERE id = :id";
    }

    @Override
    protected String getInsertSql() {
        LOGGER.debug("Entering method");
        return "INSERT INTO users(login, password, admin, blocked, token, expires) " +
                "VALUES(:login, :password, :admin, :blocked, :token, :expires)";
    }

    @Override
    protected String getUpdateSql() {
        LOGGER.debug("Entering method");
        return "UPDATE users SET password = :password, admin = :admin, blocked = :blocked, " +
                "token = :token, expires = :expires WHERE id = :id";
    }

    @Override
    protected String getUpdateOptimisticSql() {
        LOGGER.debug("Entering method");
        return getUpdateSql() + " AND (login = :login) AND (id = :old_id) AND (login = :old_login) " +
                "AND (password = :old_password) AND (admin = :old_admin) AND (blocked = :old_blocked) " +
                "AND (token = :old_token) AND (expires = :old_expires)";
    }

    @Override
    protected String getDeleteByIdSql() {
        LOGGER.debug("Entering method");
        return "DELETE FROM users WHERE id = :id";
    }

    @Override
    protected String getDeleteOptimisticSql() {
        LOGGER.debug("Entering method");
        return "DELETE FROM users WHERE (id = :id) AND (login = :login) AND (password = :password) " +
                "AND (admin = :admin) AND (blocked = :blocked) AND (token = :token) AND (expires = :expires)";
    }

    @Override
    protected Map<String, Object> getInsertParamsMap(UserEntity entity) {
        LOGGER.debug("Entering method");
        Map<String, Object> result = new HashMap<>();
        result.put("login", entity.getLogin());
        result.put("password", entity.getPassword());
        result.put("admin", entity.getAdmin());
        result.put("blocked", entity.getBlocked());
        result.put("token", entity.getToken());
        result.put("expires", Timestamp.valueOf(entity.getExpires()));
        return result;
    }

    @Override
    protected Map<String, Object> getUpdateOptimisticParamsMap(UserEntity newEntity, UserEntity oldEntity) {
        LOGGER.debug("Entering method");
        Map<String, Object> result = getUpdateParamsMap(newEntity);
        result.put("old_id", oldEntity.getId());
        result.put("old_login", oldEntity.getLogin());
        result.put("old_password", oldEntity.getPassword());
        result.put("old_admin", oldEntity.getAdmin());
        result.put("old_blocked", oldEntity.getBlocked());
        result.put("old_token", oldEntity.getToken());
        result.put("old_expires", Timestamp.valueOf(oldEntity.getExpires()));
        return result;
    }

    @Override
    public UserEntity getByToken(String token) {
        LOGGER.debug("Entering method");
        if (StringUtils.isEmpty(token)) {
            return null;
        }

        String sql = "SELECT id, login, password, admin, blocked, token, expires FROM users WHERE token = :token";
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, Collections.singletonMap("token", token), rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            String message = "Duplicate user token in db";
            LOGGER.error(message);
            throw new IllegalStateException(message, e);
        }
    }

    @Override
    public UserEntity getByLogin(String login) {
        LOGGER.debug("Entering method");
        if (StringUtils.isEmpty(login)) {
            return null;
        }

        String sql = "SELECT id, login, password, admin, blocked, token, expires FROM users WHERE login = :login";
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, Collections.singletonMap("login", login), rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            String message = "Duplicate user login in db";
            LOGGER.error(message);
            throw new IllegalStateException(message, e);
        }
    }

}
