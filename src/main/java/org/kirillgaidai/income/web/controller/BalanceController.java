package org.kirillgaidai.income.web.controller;

import org.kirillgaidai.income.service.dto.BalanceDto;
import org.kirillgaidai.income.service.intf.IBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Set;

@Controller
@RequestMapping(value = "/balance")
public class BalanceController {

    final private IBalanceService balanceService;

    @Autowired
    public BalanceController(
            IBalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public ModelAndView showBalanceForm(
            @RequestParam("account_id") Integer accountId,
            @RequestParam("day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day,
            @RequestParam("return_account_id") Set<Integer> returnAccountIds,
            @RequestParam("return_first_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnFirstDay,
            @RequestParam("return_last_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnLastDay) {
        BalanceDto balanceDto = balanceService.get(accountId, day);
        ModelAndView modelAndView = new ModelAndView("balance/form");
        modelAndView.addObject("balanceDto", balanceDto);
        modelAndView.addObject("returnAccountIds", returnAccountIds);
        modelAndView.addObject("returnFirstDay", returnFirstDay);
        modelAndView.addObject("returnLastDay", returnLastDay);
        return modelAndView;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String saveBalanceForm(
            @Validated BalanceDto balanceDto,
            @RequestParam("return_account_id") Set<Integer> returnAccountIds,
            @RequestParam("return_first_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnFirstDay,
            @RequestParam("return_last_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnLastDay) {
        balanceService.save(balanceDto);
        StringBuilder result = new StringBuilder("redirect:/summary")
                .append("?first_day=").append(returnFirstDay)
                .append("&last_day=").append(returnLastDay);
        for (Integer returnAccountId : returnAccountIds) {
            result.append("&account_id=").append(returnAccountId);
        }
        return result.toString();
    }

}
