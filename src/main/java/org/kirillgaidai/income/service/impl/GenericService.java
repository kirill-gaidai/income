package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.IGenericEntity;
import org.kirillgaidai.income.dao.intf.IGenericDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.IGenericDto;
import org.kirillgaidai.income.service.intf.IGenericService;
import org.kirillgaidai.income.service.util.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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
    final protected ServiceHelper serviceHelper;

    public GenericService(IGenericDao<E> dao, IGenericConverter<E, T> converter, ServiceHelper serviceHelper) {
        this.dao = dao;
        this.converter = converter;
        this.serviceHelper = serviceHelper;
    }

    @Override
    public List<T> getList() {
        return populateAdditionalFields(dao.getList().stream().map(converter::convertToDto)
                .collect(Collectors.toList()));
    }

    /**
     * Override to populate additional fields for dto list. For reading methods only
     *
     * @param dtoList dto list
     * @return populated dto list
     */
    protected List<T> populateAdditionalFields(List<T> dtoList) {
        LOGGER.debug("Entering method");
        return dtoList;
    }

    /**
     * Override to populate additional fields for dto. For reading methods only
     *
     * @param dto dto
     * @return populated dto
     */
    protected T populateAdditionalFields(T dto) {
        LOGGER.debug("Entering method");
        return dto;
    }

    /**
     * Override to validate dto
     *
     * @param dto dto
     */
    protected void validateDto(T dto) {
        LOGGER.debug("Entering method");
        if (dto == null) {
            LOGGER.error("Dto is null");
            throw new IllegalArgumentException("Dto is null");
        }
    }

}
