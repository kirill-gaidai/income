package org.kirillgaidai.income.rest.controller;

import org.kirillgaidai.income.rest.dto.balance.BalanceGetRestDto;
import org.kirillgaidai.income.rest.dto.balance.BalanceUpdateRestDto;
import org.kirillgaidai.income.rest.mappers.BalanceRestDtoMapper;
import org.kirillgaidai.income.service.intf.IBalanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("/rest/balance")
public class BalanceRest {

    final private static Logger LOGGER = LoggerFactory.getLogger(BalanceRest.class);

    final private IBalanceService service;
    final private BalanceRestDtoMapper mapper;

    @Autowired
    public BalanceRest(IBalanceService service, BalanceRestDtoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,
            params = {"firstDay", "lastDay", "accountId"})
    public List<BalanceGetRestDto> getList(
            @RequestParam("firstDay") LocalDate firstDay,
            @RequestParam("lastDay") LocalDate lastDay,
            @RequestParam("accountId") Set<Integer> accountIds) {
        LOGGER.debug("Getting balance list. firstDay=\"{}\", lastDay=\"{}\", accountIds=\"{}\"", firstDay, lastDay,
                accountIds);
        return Collections.emptyList();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE,
            params = {"day", "accountId"})
    public BalanceUpdateRestDto get(
            @RequestParam("day") LocalDate day,
            @RequestParam("accountId") Integer accountId) {
        LOGGER.debug("Getting balance. day=\"{}\", accountId=\"{}\"", day, accountId);
        return mapper.toRestDto(service.get(accountId, day));
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public BalanceGetRestDto update(@RequestBody BalanceUpdateRestDto restDto) {
        LOGGER.debug("Updating balance");
        return null;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(
            @RequestParam("day") LocalDate day,
            @RequestParam("accountId") Integer accountId) {
        LOGGER.debug("Deleting balance. day=\"{}\", accountId=\"{}\"", day, accountId);
    }

}
