package org.kirillgaidai.income.rest.controller;

import org.kirillgaidai.income.rest.dto.operation.OperationCreateRestDto;
import org.kirillgaidai.income.rest.dto.operation.OperationGetRestDto;
import org.kirillgaidai.income.rest.dto.operation.OperationUpdateRestDto;
import org.kirillgaidai.income.rest.mappers.IGenericRestDtoMapper;
import org.kirillgaidai.income.service.dto.OperationDto;
import org.kirillgaidai.income.service.intf.IGenericService;
import org.kirillgaidai.income.service.intf.IOperationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/operations")
public class OperationRest
        extends SerialRest<OperationGetRestDto, OperationCreateRestDto, OperationUpdateRestDto, OperationDto>
        implements ISerialRest<OperationGetRestDto, OperationCreateRestDto, OperationUpdateRestDto> {

    private final static Logger LOGGER = LoggerFactory.getLogger(OperationRest.class);

    @Autowired
    public OperationRest(IGenericService<OperationDto> service, IGenericRestDtoMapper<OperationGetRestDto,
            OperationCreateRestDto, OperationUpdateRestDto, OperationDto> mapper) {
        super(service, mapper);
    }

    @Override
    protected IOperationService getService() {
        return (IOperationService) super.getService();
    }

    @Override
    public List<OperationGetRestDto> getList() {
        LOGGER.debug("Entering method");
        throw new UnsupportedOperationException();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<OperationGetRestDto> getList(
            @RequestParam("first_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate firstDay,
            @RequestParam("last_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastDay,
            @RequestParam("account_id") Set<Integer> accountIds) {
        LOGGER.debug("Entering method");
        return getService().getList(accountIds, Collections.emptySet(), firstDay, lastDay).stream()
                .map(mapper::toRestDto).collect(Collectors.toList());
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public OperationGetRestDto get(@PathVariable("id") Integer id) {
        LOGGER.debug("Entering method");
        return super.get(id);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public OperationGetRestDto create(@RequestBody OperationCreateRestDto newRestDto) {
        LOGGER.debug("Entering method");
        return super.create(newRestDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public OperationGetRestDto update(@RequestBody OperationUpdateRestDto restDto) {
        LOGGER.debug("Entering method");
        return super.update(restDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        LOGGER.debug("Entering method");
        super.delete(id);
    }

}
