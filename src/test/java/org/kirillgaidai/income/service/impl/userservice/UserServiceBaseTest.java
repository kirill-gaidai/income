package org.kirillgaidai.income.service.impl.userservice;

import org.kirillgaidai.income.dao.entity.UserEntity;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.converter.UserConverter;
import org.kirillgaidai.income.service.dto.UserDto;
import org.kirillgaidai.income.service.impl.ServiceBaseTest;
import org.kirillgaidai.income.service.impl.UserService;
import org.kirillgaidai.income.service.intf.IUserService;

public abstract class UserServiceBaseTest extends ServiceBaseTest {

    final protected IGenericConverter<UserEntity, UserDto> converter = new UserConverter();
    final protected IUserService service = new UserService(userDao, serviceHelper, converter);

}
