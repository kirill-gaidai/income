package org.kirillgaidai.income.service.intf;

import org.kirillgaidai.income.service.dto.UserDto;

/**
 * User service
 *
 * @author Kirill Gaidai
 */
public interface IUserService extends ISerialService<UserDto> {

    /**
     * Checks whether user is logged in
     *
     * @param token token
     * @return user is logged in
     */
    boolean isLoggedIn(String token);

    /**
     * Logins user
     *
     * @return user dto with populated token and expiration time
     */
    UserDto login(UserDto dto);

    /**
     * Logs out user
     *
     * @param token token
     */
    void logout(String token);

}
