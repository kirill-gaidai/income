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
        LOGGER.debug("Getting entity list");
        return service.getList().stream().map(mapper::toRestDto).collect(Collectors.toList());
    }

    @Override
    public GT create(CT newRestDto) {
        LOGGER.debug("Creating entity");
        ST dto = mapper.toDto(newRestDto);
        service.save(dto);
        return mapper.toRestDto(dto);
    }

    @Override
    public GT update(UT restDto) {
        LOGGER.debug("Updating entity");
        ST dto = mapper.toDto(restDto);
        service.save(dto);
        return mapper.toRestDto(dto);
    }

}
