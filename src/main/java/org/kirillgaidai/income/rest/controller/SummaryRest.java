package org.kirillgaidai.income.rest.controller;

import org.kirillgaidai.income.rest.dto.SummaryRestDto;
import org.kirillgaidai.income.rest.dto.account.AccountGetRestDto;
import org.kirillgaidai.income.rest.dto.balance.BalanceGetRestDto;
import org.kirillgaidai.income.rest.dto.category.CategoryGetRestDto;
import org.kirillgaidai.income.rest.dto.currency.CurrencyGetRestDto;
import org.kirillgaidai.income.service.intf.ISummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@RequestMapping("/rest/summary")
@RestController
public class SummaryRest {

    final private ISummaryService summaryService;

    @Autowired
    public SummaryRest(ISummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public SummaryRestDto getSummary(
            @RequestParam("first_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate firstDay,
            @RequestParam("last_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastDay,
            @RequestParam("currency_id") Integer currencyId) {
        CurrencyGetRestDto currency = new CurrencyGetRestDto(currencyId, "CC1", "Currency 1", 2);
        List<AccountGetRestDto> accounts = Arrays.asList(
                new AccountGetRestDto(1, currencyId, "CC1", "Currency 1", "01", "Account 1"),
                new AccountGetRestDto(2, currencyId, "CC1", "Currency 1", "02", "Account 2")
        );
        List<CategoryGetRestDto> categories = Arrays.asList(
                new CategoryGetRestDto(1, "01", "Category 1"),
                new CategoryGetRestDto(2, "02", "Category 2"),
                new CategoryGetRestDto(3, "03", "Category 3")
        );
        /*List<BalanceGetRestDto> balances = Arrays.asList(
                new BalanceGetRestDto(1, "Account 1", LocalDate.of(2017, 4, 10), new BigDecimal("100.00"), false),
        );
        return new SummaryRestDto(currency, accounts, categories);*/
        return null;
    }

}
