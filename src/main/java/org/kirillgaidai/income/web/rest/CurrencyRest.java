package org.kirillgaidai.income.web.rest;

import org.kirillgaidai.income.dto.CurrencyDto;
import org.kirillgaidai.income.dto.rest.CurrencyListDto;
import org.kirillgaidai.income.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/currency")
public class CurrencyRest {

    private CurrencyService currencyService;

    @Autowired
    public CurrencyRest(final CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @RequestMapping(value = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity getCurrencyById(final @PathVariable("id") Integer id) {
        final CurrencyDto currencyDto = currencyService.getCurrencyById(id);
        return ResponseEntity.ok(currencyDto);
    }

    @RequestMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity getCurrencyList() {
        final CurrencyListDto currencyListDto = new CurrencyListDto();
        currencyListDto.setCurrencyDtoList(currencyService.getCurrencyList());
        return ResponseEntity.ok(currencyListDto);
    }

}
