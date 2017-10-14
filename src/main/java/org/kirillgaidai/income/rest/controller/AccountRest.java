package org.kirillgaidai.income.rest.controller;

import org.kirillgaidai.income.rest.dto.account.AccountCreateRestDto;
import org.kirillgaidai.income.rest.dto.account.AccountGetRestDto;
import org.kirillgaidai.income.rest.dto.account.AccountUpdateRestDto;
import org.kirillgaidai.income.rest.mappers.IGenericRestDtoMapper;
import org.kirillgaidai.income.service.dto.AccountDto;
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

import java.util.List;

@RestController
@RequestMapping("/rest/account")
public class AccountRest
        extends GenericRest<AccountGetRestDto, AccountCreateRestDto, AccountUpdateRestDto, AccountDto>
        implements IGenericRest<AccountGetRestDto, AccountCreateRestDto, AccountUpdateRestDto> {

    @Autowired
    public AccountRest(IGenericService<AccountDto> service, IGenericRestDtoMapper<AccountGetRestDto,
            AccountCreateRestDto, AccountUpdateRestDto, AccountDto> mapper) {
        super(service, mapper);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AccountGetRestDto> getList() {
        return super.getList();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public AccountGetRestDto get(@PathVariable("id") Integer id) {
        return super.get(id);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public AccountGetRestDto create(@RequestBody AccountCreateRestDto newRestDto) {
        return super.create(newRestDto);
    }

    @Override
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public AccountGetRestDto update(@RequestBody AccountUpdateRestDto restDto) {
        return super.update(restDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        super.delete(id);
    }

}
