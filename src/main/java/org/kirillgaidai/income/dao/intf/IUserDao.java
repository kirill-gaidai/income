package org.kirillgaidai.income.dao.intf;

import org.kirillgaidai.income.dao.entity.UserEntity;

/**
 * User DAO
 *
 * @author Kirill Gaidai
 */
public interface IUserDao extends ISerialDao<UserEntity> {

    /**
     * Returns user by token
     *
     * @param token token
     * @return user entity
     */
    UserEntity get(String token);

}
