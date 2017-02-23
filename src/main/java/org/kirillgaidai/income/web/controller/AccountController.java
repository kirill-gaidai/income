package org.kirillgaidai.income.web.controller;

import org.kirillgaidai.income.dto.AccountDto;
import org.kirillgaidai.income.exception.IncomeControllerNotFoundException;
import org.kirillgaidai.income.service.AccountService;
import org.kirillgaidai.income.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/account")
public class AccountController {

    private AccountService accountService;
    private CurrencyService currencyService;

    @Autowired
    public AccountController(final AccountService accountService, final CurrencyService currencyService) {
        this.accountService = accountService;
        this.currencyService = currencyService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView showAccountsList() {
        return new ModelAndView("account/list", "accounts", accountService.getAccountList());
    }

    @RequestMapping(value = "/edit/new", method = RequestMethod.GET)
    public ModelAndView showAccountForm() {
        final ModelAndView modelAndView = new ModelAndView("account/form");
        modelAndView.addObject("currencies", currencyService.getCurrencyList());
        modelAndView.addObject("account", new AccountDto());
        return modelAndView;
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView showAccountForm(final @PathVariable("id") Integer id) {
        AccountDto accountDto = accountService.getAccountById(id);
        if (accountDto == null) {
            throw new IncomeControllerNotFoundException("account with id %d not found", id);
        }

        final ModelAndView modelAndView = new ModelAndView("account/form");
        modelAndView.addObject("currencies", currencyService.getCurrencyList());
        modelAndView.addObject("account", accountDto);
        return modelAndView;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String saveAccount(final @Validated AccountDto accountDto) {
        accountService.saveAccount(accountDto);
        return "redirect:/account/list";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String deleteAccount(final @PathVariable("id") Integer id) {
        accountService.deleteAccount(id);
        return "redirect:/account/list";
    }

}
