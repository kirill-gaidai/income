package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.UserEntity;
import org.kirillgaidai.income.dao.intf.IGenericDao;
import org.kirillgaidai.income.dao.intf.IUserDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.UserDto;
import org.kirillgaidai.income.service.intf.IUserService;
import org.kirillgaidai.income.service.util.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
    protected IUserDao getDao() {
        return (IUserDao) dao;
    }

    @Override
    @Transactional
    public List<UserDto> getList() {
        LOGGER.debug("Entering method");
        return super.getList();
    }

    @Override
    @Transactional
    public List<UserDto> getList(Set<Integer> ids) {
        LOGGER.debug("Entering method");
        return super.getList(ids);
    }

    @Override
    @Transactional
    public UserDto get(Integer id) {
        LOGGER.debug("Entering method");
        validateId(id);
        return converter.convertToDto(serviceHelper.getUserEntity(id));
    }

    @Override
    @Transactional
    public UserDto create(UserDto dto) {
        LOGGER.debug("Entering method");
        validateDto(dto);
        UserEntity entity = converter.convertToEntity(dto);
        serviceHelper.createUserEntity(entity);
        return converter.convertToDto(entity);
    }

    @Override
    @Transactional
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
    @Transactional
    public UserDto save(UserDto dto) {
        LOGGER.debug("Entering method");
        return super.save(dto);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        LOGGER.debug("Entering method");
        validateId(id);
        serviceHelper.deleteUserEntity(serviceHelper.getUserEntity(id));
    }

    @Override
    @Transactional
    public boolean isLoggedIn(String token) {
        LOGGER.debug("Entering method");
        UserEntity entity = getDao().getByToken(token);
        // User is logged in when token found and its expiration time is in future, user shouldn't be blocked
        return entity != null && !entity.getBlocked() && !LocalDateTime.now().isAfter(entity.getExpires());
    }

    @Override
    @Transactional
    public UserDto login(UserDto dto) {
        LOGGER.debug("Entering method");
        validateDto(dto);

        UserEntity oldEntity = getDao().getByLogin(dto.getLogin());
        if (oldEntity == null || oldEntity.getBlocked() || !dto.getPassword().equals(oldEntity.getPassword())) {
            return null;
        }

        String token;
        do {
            token = UUID.randomUUID().toString();
        } while (getDao().getByToken(token) != null);

        UserEntity newEntity = new UserEntity(oldEntity.getId(),
                oldEntity.getLogin(), oldEntity.getPassword(),
                oldEntity.getAdmin(), oldEntity.getBlocked(),
                token, LocalDateTime.now().plusHours(1L));
        serviceHelper.updateUserEntity(newEntity, oldEntity);
        return new UserDto(null, newEntity.getLogin(), null, newEntity.getAdmin(), null,
                newEntity.getToken(), newEntity.getExpires());
    }

    @Override
    protected void validateDto(UserDto dto) {
        LOGGER.debug("Entering method");
        super.validateDto(dto);
        if (StringUtils.isEmpty(dto.getLogin())) {
            String message = "Login is empty";
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
        if (StringUtils.isEmpty(dto.getPassword())) {
            String message = "Password is empty";
            LOGGER.error(message);
            throw new IllegalArgumentException(message);
        }
    }

}
