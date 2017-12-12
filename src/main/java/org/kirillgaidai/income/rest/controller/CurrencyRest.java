package org.kirillgaidai.income.rest.controller;

import org.kirillgaidai.income.rest.dto.currency.CurrencyCreateRestDto;
import org.kirillgaidai.income.rest.dto.currency.CurrencyGetRestDto;
import org.kirillgaidai.income.rest.dto.currency.CurrencyUpdateRestDto;
import org.kirillgaidai.income.rest.mappers.IGenericRestDtoMapper;
import org.kirillgaidai.income.service.dto.CurrencyDto;
import org.kirillgaidai.income.service.intf.IGenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rest/currencies")
public class CurrencyRest
        extends SerialRest<CurrencyGetRestDto, CurrencyCreateRestDto, CurrencyUpdateRestDto, CurrencyDto>
        implements ISerialRest<CurrencyGetRestDto, CurrencyCreateRestDto, CurrencyUpdateRestDto> {

    @Autowired
    public CurrencyRest(IGenericService<CurrencyDto> service, IGenericRestDtoMapper<CurrencyGetRestDto,
            CurrencyCreateRestDto, CurrencyUpdateRestDto, CurrencyDto> mapper) {
        super(service, mapper);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CurrencyGetRestDto> getList() {
        return super.getList();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CurrencyGetRestDto get(@PathVariable("id") Integer id) {
        return super.get(id);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CurrencyGetRestDto create(@Valid @RequestBody CurrencyCreateRestDto newRestDto) {
        return super.create(newRestDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CurrencyGetRestDto update(@Valid @RequestBody CurrencyUpdateRestDto restDto) {
        return super.update(restDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        super.delete(id);
    }

}
