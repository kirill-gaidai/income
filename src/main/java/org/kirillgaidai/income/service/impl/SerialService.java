package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.ISerialEntity;
import org.kirillgaidai.income.dao.intf.IGenericDao;
import org.kirillgaidai.income.dao.intf.ISerialDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.ISerialDto;
import org.kirillgaidai.income.service.intf.ISerialService;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class SerialService<T extends ISerialDto, E extends ISerialEntity>
        extends GenericService<T, E> implements ISerialService<T> {

    public SerialService(IGenericDao<E> dao, IGenericConverter<E, T> converter) {
        super(dao, converter);
    }

    protected ISerialDao<E> getSerialDao() {
        return (ISerialDao<E>) dao;
    }

    @Override
    public T save(T dto) {
        if (dto == null) {
            throw new IllegalArgumentException("null");
        }
        E entity = converter.convertToEntity(dto);
        int affectedRows = dto.getId() == null ? getSerialDao().insert(entity) : getSerialDao().update(entity);
        if (affectedRows != 1) {
            throwNotFoundException(dto.getId());
        }
        // don't populate additional fields here, but in descendants in overriding method
        return converter.convertToDto(entity);
    }

    @Override
    public List<T> getList(Set<Integer> ids) {
        if (ids == null) {
            throw new IllegalArgumentException("null");
        }
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        return populateAdditionalFields(getSerialDao().getList(ids).stream().map(converter::convertToDto)
                .collect(Collectors.toList()));
    }

    @Override
    public T get(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("null");
        }
        E entity = getSerialDao().get(id);
        if (entity == null) {
            throwNotFoundException(id);
        }
        return populateAdditionalFields(converter.convertToDto(entity));
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("null");
        }
        int affectedRows = getSerialDao().delete(id);
        if (affectedRows == 0) {
            throwNotFoundException(id);
        }
    }

    protected abstract void throwNotFoundException(Integer id);

}
