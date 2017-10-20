package org.kirillgaidai.income.rest.controller;

import org.kirillgaidai.income.rest.dto.IGenericCreateRestDto;
import org.kirillgaidai.income.rest.dto.ISerialGetRestDto;
import org.kirillgaidai.income.rest.dto.ISerialUpdateRestDto;
import org.kirillgaidai.income.rest.mappers.IGenericRestDtoMapper;
import org.kirillgaidai.income.service.dto.ISerialDto;
import org.kirillgaidai.income.service.intf.IGenericService;
import org.kirillgaidai.income.service.intf.ISerialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SerialRest<GT extends ISerialGetRestDto, CT extends IGenericCreateRestDto,
        UT extends ISerialUpdateRestDto, ST extends ISerialDto>
        extends GenericRest<GT, CT, UT, ST> implements ISerialRest<GT, CT, UT> {

    final private static Logger LOGGER = LoggerFactory.getLogger(SerialRest.class);

    public SerialRest(IGenericService<ST> service, IGenericRestDtoMapper<GT, CT, UT, ST> mapper) {
        super(service, mapper);
    }

    protected ISerialService<ST> getService() {
        return (ISerialService<ST>) service;
    }

    @Override
    public GT get(Integer id) {
        LOGGER.debug("Getting entity");
        return mapper.toRestDto(getService().get(id));
    }

    @Override
    public void delete(Integer id) {
        LOGGER.debug("Deleting entity");
        getService().delete(id);
    }

}
