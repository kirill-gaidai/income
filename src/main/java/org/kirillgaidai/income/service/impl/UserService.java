package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.UserEntity;
import org.kirillgaidai.income.dao.intf.IGenericDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.UserDto;
import org.kirillgaidai.income.service.intf.IUserService;
import org.kirillgaidai.income.service.util.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService extends SerialService<UserDto, UserEntity> implements IUserService {

    final private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(
            IGenericDao<UserEntity> dao,
            ServiceHelper serviceHelper,
            IGenericConverter<UserEntity, UserDto> converter) {
        super(dao, converter, serviceHelper);
    }

    @Override
    public UserDto get(Integer id) {
        LOGGER.debug("Entering method");
        validateId(id);
        return converter.convertToDto(serviceHelper.getUserEntity(id));
    }

    @Override
    public UserDto create(UserDto dto) {
        LOGGER.debug("Entering method");
        validateDto(dto);
        UserEntity entity = converter.convertToEntity(dto);
        serviceHelper.createUserEntity(entity);
        return converter.convertToDto(entity);
    }

    @Override
    public UserDto update(UserDto dto) {
        LOGGER.debug("Entering method");
        validateDto(dto);
        Integer id = dto.getId();
        validateId(id);
        UserEntity oldEntity = serviceHelper.getUserEntity(id);
        UserEntity newEntity = converter.convertToEntity(dto);
        serviceHelper.updateUserEntity(newEntity, oldEntity);
        return converter.convertToDto(newEntity);
    }

    @Override
    public void delete(Integer id) {
        LOGGER.debug("Entering method");
        validateId(id);
        serviceHelper.deleteUserEntity(serviceHelper.getUserEntity(id));
    }

}
