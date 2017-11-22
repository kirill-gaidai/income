package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.IGenericEntity;
import org.kirillgaidai.income.dao.intf.IGenericDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.IGenericDto;
import org.kirillgaidai.income.service.exception.IncomeServiceException;
import org.kirillgaidai.income.service.exception.IncomeServiceIntegrityException;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.kirillgaidai.income.service.intf.IGenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Generic service for {@link IGenericEntity}
 *
 * @param <T> dto class
 * @param <E> entity class
 * @author Kirill Gaidai
 */
public abstract class GenericService<T extends IGenericDto, E extends IGenericEntity> implements IGenericService<T> {

    final private static Logger LOGGER = LoggerFactory.getLogger(GenericService.class);

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
    public T create(T dto) {
        LOGGER.debug("Entering method");
        E entity = converter.convertToEntity(dto);
        int affectedRows = dao.insert(entity);
        if (affectedRows != 1) {
            LOGGER.error("Dto isn't created");
            throw new IncomeServiceIntegrityException();
        }
        // additional fields are set in the overriding method which wraps this method
        return converter.convertToDto(entity);
    }

    @Override
    public T update(T dto) {
        LOGGER.debug("Entering method");
        return update(dto, () -> new IncomeServiceNotFoundException("Entity not found"));
    }

    final protected T update(T dto, Supplier<? extends IncomeServiceNotFoundException> notFoundExceptionSupplier) {
        LOGGER.debug("Entering method");
        E entity = converter.convertToEntity(dto);
        int affectedRows = dao.update(entity);
        if (affectedRows != 1) {
            LOGGER.error("Entity not found");
            throw notFoundExceptionSupplier.get();
        }
        // additional fields are set in the overriding method which wraps this method
        return converter.convertToDto(entity);
    }

    @Override
    public T save(T dto) {
        LOGGER.debug("Entering method");
        E entity = converter.convertToEntity(dto);
        int affectedRows = dao.update(entity);
        if (affectedRows == 1) {
            LOGGER.debug("Dto is inserted");
            // additional fields are set in the overriding method which wraps this method
            return converter.convertToDto(entity);
        }
        affectedRows = dao.insert(entity);
        if (affectedRows == 1) {
            LOGGER.debug("Dto is updated");
            // additional fields are set in the overriding method which wraps this method
            return converter.convertToDto(entity);
        }
        LOGGER.error("Dto isn't inserted or updated");
        throw new IncomeServiceException("Dto isn't inserted or updated");
    }

    /**
     * Override to Populate additional fields for dto list. For reading methods only
     *
     * @param dtoList dto list
     * @return populated dto list
     */
    protected List<T> populateAdditionalFields(List<T> dtoList) {
        LOGGER.debug("Entering method");
        return dtoList;
    }

    /**
     * Override to Populate additional fields for dto. For reading methods only
     *
     * @param dto dto
     * @return populated dto
     */
    protected T populateAdditionalFields(T dto) {
        LOGGER.debug("Entering method");
        return dto;
    }

    protected void validateDto(T dto) {
        LOGGER.debug("Entering method");
        if (dto == null) {
            LOGGER.error("Dto is null");
            throw new IllegalArgumentException("Dto is null");
        }
    }

}
