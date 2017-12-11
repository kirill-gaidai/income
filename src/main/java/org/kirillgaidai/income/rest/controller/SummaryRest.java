package org.kirillgaidai.income.rest.controller;

import org.kirillgaidai.income.rest.dto.SummaryRestDto;
import org.kirillgaidai.income.rest.mappers.SummaryRestDtoMapper;
import org.kirillgaidai.income.service.intf.ISummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RequestMapping("/rest/summary")
@RestController
public class SummaryRest {

    final private ISummaryService service;
    final private SummaryRestDtoMapper mapper;

    @Autowired
    public SummaryRest(
            ISummaryService service,
            SummaryRestDtoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SummaryRestDto get(
            @RequestParam("first_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate firstDay,
            @RequestParam("last_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastDay,
            @RequestParam("currency_id") Integer currencyId) {
        return mapper.toRestDto(service.get(currencyId, firstDay, lastDay));
    }

}
