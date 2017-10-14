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

    final private IGenericService<ST> service;
    final private IGenericRestDtoMapper<GT, CT, UT, ST> mapper;

    public GenericRest(
            IGenericService<ST> service,
            IGenericRestDtoMapper<GT, CT, UT, ST> mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public List<GT> getList() {
        LOGGER.debug("Getting entity list");
        return service.getDtoList().stream().map(mapper::toRestDto).collect(Collectors.toList());
    }

    @Override
    public GT get(Integer id) {
        LOGGER.debug("Getting entity");
        return mapper.toRestDto(service.getDto(id));
    }

    @Override
    public GT create(CT newRestDto) {
        LOGGER.debug("Creating entity");
        ST dto = mapper.toDto(newRestDto);
        service.saveDto(dto);
        return mapper.toRestDto(dto);
    }

    @Override
    public GT update(UT restDto) {
        LOGGER.debug("Updating entity");
        ST dto = mapper.toDto(restDto);
        service.saveDto(dto);
        return mapper.toRestDto(dto);
    }

    @Override
    public void delete(Integer id) {
        LOGGER.debug("Deleting entity");
        service.deleteDto(id);
    }

}
