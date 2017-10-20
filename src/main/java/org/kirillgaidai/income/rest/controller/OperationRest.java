package org.kirillgaidai.income.rest.controller;

import org.kirillgaidai.income.rest.dto.operation.OperationCreateRestDto;
import org.kirillgaidai.income.rest.dto.operation.OperationGetRestDto;
import org.kirillgaidai.income.rest.dto.operation.OperationUpdateRestDto;
import org.kirillgaidai.income.rest.mappers.IGenericRestDtoMapper;
import org.kirillgaidai.income.service.dto.OperationDto;
import org.kirillgaidai.income.service.intf.IGenericService;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/rest/operation")
public class OperationRest
        extends SerialRest<OperationGetRestDto, OperationCreateRestDto, OperationUpdateRestDto, OperationDto>
        implements ISerialRest<OperationGetRestDto, OperationCreateRestDto, OperationUpdateRestDto> {

    @Autowired
    public OperationRest(IGenericService<OperationDto> service, IGenericRestDtoMapper<OperationGetRestDto,
            OperationCreateRestDto, OperationUpdateRestDto, OperationDto> mapper) {
        super(service, mapper);
    }

    @Override
    public List<OperationGetRestDto> getList() {
        return super.getList();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public List<OperationGetRestDto> getList(
            @RequestParam("firstDay") LocalDate firstDay,
            @RequestParam("lastDay") LocalDate lastDay,
            @RequestParam("categoryId") Set<Integer> categoryIds) {
        return Collections.emptyList();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
    public OperationGetRestDto get(@PathVariable("id") Integer id) {
        return super.get(id);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public OperationGetRestDto create(@RequestBody OperationCreateRestDto newRestDto) {
        return super.create(newRestDto);
    }

    @Override
    public OperationGetRestDto update(OperationUpdateRestDto restDto) {
        return super.update(restDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        super.delete(id);
    }

}
