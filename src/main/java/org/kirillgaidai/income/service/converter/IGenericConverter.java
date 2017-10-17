package org.kirillgaidai.income.service.converter;

import org.kirillgaidai.income.dao.entity.IGenericEntity;
import org.kirillgaidai.income.service.dto.IGenericDto;

public interface IGenericConverter<E extends IGenericEntity, D extends IGenericDto> {

    D convertToDto(E entity);

    E convertToEntity(D dto);

}
