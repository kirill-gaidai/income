package org.kirillgaidai.income.rest.controller;

import org.kirillgaidai.income.rest.dto.balance.BalanceCreateRestDto;
import org.kirillgaidai.income.rest.dto.balance.BalanceGetRestDto;
import org.kirillgaidai.income.rest.dto.balance.BalanceUpdateRestDto;
import org.kirillgaidai.income.rest.mappers.IGenericRestDtoMapper;
import org.kirillgaidai.income.service.dto.BalanceDto;
import org.kirillgaidai.income.service.intf.IBalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/rest/balances")
public class BalanceRest
        extends GenericRest<BalanceGetRestDto, BalanceCreateRestDto, BalanceUpdateRestDto, BalanceDto>
        implements IGenericRest<BalanceGetRestDto, BalanceCreateRestDto, BalanceUpdateRestDto> {

    final private static Logger LOGGER = LoggerFactory.getLogger(BalanceRest.class);

    @Autowired
    public BalanceRest(
            IBalanceService service,
            IGenericRestDtoMapper<BalanceGetRestDto, BalanceCreateRestDto, BalanceUpdateRestDto, BalanceDto> mapper) {
        super(service, mapper);
    }

    private IBalanceService getService() {
        return (IBalanceService) service;
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BalanceGetRestDto> getList() {
        LOGGER.debug("Entering method");
        return super.getList();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,
            params = {"day", "account_id"})
    public BalanceUpdateRestDto get(
            @RequestParam("day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day,
            @RequestParam("account_id") Integer accountId) {
        LOGGER.debug("Entering method");
        return mapper.toRestDto(getService().get(accountId, day));
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BalanceGetRestDto create(@Valid @RequestBody BalanceCreateRestDto newRestDto) {
        LOGGER.debug("Entering method");
        return super.create(newRestDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public BalanceGetRestDto update(@Valid @RequestBody BalanceUpdateRestDto restDto) {
        LOGGER.debug("Entering method");
        return super.update(restDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE, params = {"day", "account_id"})
    public void delete(
            @RequestParam("day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day,
            @RequestParam("account_id") Integer accountId) {
        LOGGER.debug("Entering method");
    }

}
