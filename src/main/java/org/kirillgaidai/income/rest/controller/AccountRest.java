package org.kirillgaidai.income.rest.controller;

import org.kirillgaidai.income.rest.dto.account.AccountCreateRestDto;
import org.kirillgaidai.income.rest.dto.account.AccountGetRestDto;
import org.kirillgaidai.income.rest.dto.account.AccountUpdateRestDto;
import org.kirillgaidai.income.rest.mappers.IGenericRestDtoMapper;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.intf.IAccountService;
import org.kirillgaidai.income.service.intf.IGenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/accounts")
public class AccountRest
        extends SerialRest<AccountGetRestDto, AccountCreateRestDto, AccountUpdateRestDto, AccountDto>
        implements ISerialRest<AccountGetRestDto, AccountCreateRestDto, AccountUpdateRestDto> {

    final private static Logger LOGGER = LoggerFactory.getLogger(AccountRest.class);

    @Autowired
    public AccountRest(
            IGenericService<AccountDto> service,
            IGenericRestDtoMapper<AccountGetRestDto, AccountCreateRestDto, AccountUpdateRestDto, AccountDto> mapper) {
        super(service, mapper);
    }

    @Override
    protected IAccountService getService() {
        return (IAccountService) service;
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AccountGetRestDto> getList() {
        LOGGER.debug("Entering method");
        return super.getList();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, params = "currency_id")
    public List<AccountGetRestDto> getList(@RequestParam("currency_id") Integer currencyId) {
        LOGGER.debug("Entering method");
        return getService().getList(currencyId).stream().map(mapper::toRestDto).collect(Collectors.toList());
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountGetRestDto get(@PathVariable("id") Integer id) {
        LOGGER.debug("Entering method");
        return super.get(id);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AccountGetRestDto create(@Valid @RequestBody AccountCreateRestDto newRestDto) {
        LOGGER.debug("Entering method");
        return super.create(newRestDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AccountGetRestDto update(@Valid @RequestBody AccountUpdateRestDto restDto) {
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
