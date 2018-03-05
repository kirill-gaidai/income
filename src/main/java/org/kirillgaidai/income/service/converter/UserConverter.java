package org.kirillgaidai.income.service.converter;

import org.kirillgaidai.income.dao.entity.UserEntity;
import org.kirillgaidai.income.service.dto.UserDto;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Component
public class UserConverter implements IGenericConverter<UserEntity, UserDto> {

    @Override
    public UserDto convertToDto(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return new UserDto(entity.getId(), entity.getLogin(), entity.getPassword(), entity.getAdmin(),
                entity.getBlocked(), entity.getToken(), ZonedDateTime.of(entity.getExpires(), ZoneOffset.UTC));
    }

    @Override
    public UserEntity convertToEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }
        return new UserEntity(dto.getId(), dto.getLogin(), dto.getPassword(), dto.getAdmin(), dto.getBlocked(),
                dto.getToken(), dto.getExpires().withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime());
    }

}
