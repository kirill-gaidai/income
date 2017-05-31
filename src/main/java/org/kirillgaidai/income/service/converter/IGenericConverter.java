package org.kirillgaidai.income.service.converter;

public interface IGenericConverter<E, D> {

    D convertToDto(final E entity);

    E convertToEntity(final D dto);

}
