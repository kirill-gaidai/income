package org.kirillgaidai.income.web.controller;

import org.kirillgaidai.income.service.intf.ISummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Set;

@Controller
public class SummaryController {

    final private ISummaryService summaryService;

    @Autowired
    public SummaryController(
            final ISummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public ModelAndView getSummary(
            @RequestParam("account_id") Set<Integer> accountIds,
            @RequestParam("first_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate firstDay,
            @RequestParam("last_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastDay) {
        ModelAndView modelAndView = new ModelAndView("summary/list");
        modelAndView.addObject("summaryDto", summaryService.getSummaryDto(accountIds, firstDay, lastDay));
        modelAndView.addObject("returnFirstDay", firstDay);
        modelAndView.addObject("returnLastDay", lastDay);
        modelAndView.addObject("returnAccountIds", accountIds);
        return modelAndView;
    }

}
