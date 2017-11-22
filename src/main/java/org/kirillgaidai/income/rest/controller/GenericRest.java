package org.kirillgaidai.income.rest.controller;

import org.kirillgaidai.income.rest.dto.IGenericCreateRestDto;
import org.kirillgaidai.income.rest.dto.IGenericGetRestDto;
import org.kirillgaidai.income.rest.dto.IGenericUpdateRestDto;
import org.kirillgaidai.income.rest.mappers.IGenericRestDtoMapper;
import org.kirillgaidai.income.service.dto.IGenericDto;
import org.kirillgaidai.income.service.intf.IGenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public abstract class GenericRest<
        GT extends IGenericGetRestDto, CT extends IGenericCreateRestDto, UT extends IGenericUpdateRestDto,
        ST extends IGenericDto> implements IGenericRest<GT, CT, UT> {

    final private static Logger LOGGER = LoggerFactory.getLogger(GenericRest.class);

    final protected IGenericService<ST> service;
    final protected IGenericRestDtoMapper<GT, CT, UT, ST> mapper;

    public GenericRest(IGenericService<ST> service, IGenericRestDtoMapper<GT, CT, UT, ST> mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public List<GT> getList() {
        LOGGER.debug("Entering method");
        return service.getList().stream().map(mapper::toRestDto).collect(Collectors.toList());
    }

    @Override
    public GT create(CT newRestDto) {
        LOGGER.debug("Entering method");
        return mapper.toRestDto(service.save(mapper.toDto(newRestDto)));
    }

    @Override
    public GT update(UT restDto) {
        LOGGER.debug("Entering method");
        return mapper.toRestDto(service.save(mapper.toDto(restDto)));
    }

}
