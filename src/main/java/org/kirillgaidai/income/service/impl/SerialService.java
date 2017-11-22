package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.ISerialEntity;
import org.kirillgaidai.income.dao.intf.IGenericDao;
import org.kirillgaidai.income.dao.intf.ISerialDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.ISerialDto;
import org.kirillgaidai.income.service.exception.IncomeServiceException;
import org.kirillgaidai.income.service.intf.ISerialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Generic service for {@link ISerialDto}
 *
 * @param <T> dto class
 * @param <E> entity class
 * @author Kirill Gaidai
 */
public abstract class SerialService<T extends ISerialDto, E extends ISerialEntity>
        extends GenericService<T, E> implements ISerialService<T> {

    final private static Logger LOGGER = LoggerFactory.getLogger(SerialService.class);

    public SerialService(IGenericDao<E> dao, IGenericConverter<E, T> converter) {
        super(dao, converter);
    }

    protected ISerialDao<E> getDao() {
        return (ISerialDao<E>) dao;
    }

    @Override
    public T save(T dto) {
        LOGGER.debug("Entering method");
        if (dto == null) {
            throw new IllegalArgumentException("null");
        }
        E entity = converter.convertToEntity(dto);
        int affectedRows;
        if (dto.getId() == null) {
            LOGGER.debug("Creating dto");
            affectedRows = getDao().insert(entity);
        } else {
            LOGGER.debug("Updating dto");
            affectedRows = getDao().update(entity);
        }
        if (affectedRows != 1) {
            LOGGER.error("Dto isn't inserted or updated");
            throw new IncomeServiceException("Dto isn't inserted or updated");
        }
        // additional fields are set in the overriding method which wraps this method
        return converter.convertToDto(entity);
    }

    @Override
    public List<T> getList(Set<Integer> ids) {
        LOGGER.debug("Entering method");
        if (ids == null) {
            throw new IllegalArgumentException("null");
        }
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        return populateAdditionalFields(getDao().getList(ids).stream().map(converter::convertToDto)
                .collect(Collectors.toList()));
    }

    @Override
    public T get(Integer id) {
        LOGGER.debug("Entering method");
        if (id == null) {
            throw new IllegalArgumentException("null");
        }
        E entity = getDao().get(id);
        if (entity == null) {
            throwNotFoundException(id);
        }
        return populateAdditionalFields(converter.convertToDto(entity));
    }

    @Override
    public void delete(Integer id) {
        LOGGER.debug("Entering method");
        if (id == null) {
            throw new IllegalArgumentException("null");
        }
        int affectedRows = getDao().delete(id);
        if (affectedRows == 0) {
            throwNotFoundException(id);
        }
    }

    protected abstract void throwNotFoundException(Integer id);

}
