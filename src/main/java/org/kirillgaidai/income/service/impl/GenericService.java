package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.IGenericEntity;
import org.kirillgaidai.income.dao.intf.IGenericDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.IGenericDto;
import org.kirillgaidai.income.service.intf.IGenericService;

import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericService<T extends IGenericDto, E extends IGenericEntity> implements IGenericService<T> {

    final protected IGenericDao<E> dao;
    final protected IGenericConverter<E, T> converter;

    public GenericService(IGenericDao<E> dao, IGenericConverter<E, T> converter) {
        this.dao = dao;
        this.converter = converter;
    }

    @Override
    public List<T> getList() {
        return populateAdditionalFields(dao.getList().stream().map(converter::convertToDto)
                .collect(Collectors.toList()));
    }

    @Override
    public T save(T operationDto) {
        if (operationDto == null) {
            throw new IllegalArgumentException("null");
        }
        E entity = converter.convertToEntity(operationDto);
        int affectedRows = dao.update(entity);
        if (affectedRows != 1) {
            dao.insert(entity);
        }
        // don't populate additional fields here, but in descendants in overriding method
        return converter.convertToDto(entity);
    }

    protected List<T> populateAdditionalFields(List<T> dtoList) {
        return dtoList;
    }

    protected T populateAdditionalFields(T dto) {
        return dto;
    }

}
