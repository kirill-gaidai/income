package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.ISerialEntity;
import org.kirillgaidai.income.dao.intf.IGenericDao;
import org.kirillgaidai.income.dao.intf.ISerialDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.ISerialDto;
import org.kirillgaidai.income.service.intf.ISerialService;
import org.kirillgaidai.income.service.util.ServiceHelper;
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

    public SerialService(IGenericDao<E> dao, IGenericConverter<E, T> converter, ServiceHelper serviceHelper) {
        super(dao, converter, serviceHelper);
    }

    protected ISerialDao<E> getDao() {
        return (ISerialDao<E>) dao;
    }

    @Override
    public List<T> getList(Set<Integer> ids) {
        LOGGER.debug("Entering method");
        if (ids == null) {
            throw new IllegalArgumentException("Set of ids is null");
        }
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        return populateAdditionalFields(getDao().getList(ids).stream().map(converter::convertToDto)
                .collect(Collectors.toList()));
    }

    @Override
    public T save(T dto) {
        LOGGER.debug("Entering method");
        return dto == null || dto.getId() == null ? create(dto) : update(dto);
    }

    /**
     * Validates id
     *
     * @param id id
     */
    protected void validateId(Integer id) {
        LOGGER.debug("Entering method");
        if (id == null) {
            LOGGER.error("Id is null");
            throw new IllegalArgumentException("Id is null");
        }
    }

}
