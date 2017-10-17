package org.kirillgaidai.income.web.controller;

import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.intf.IAccountService;
import org.kirillgaidai.income.service.intf.ICurrencyService;
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

    final private IAccountService accountService;
    final private ICurrencyService currencyService;

    @Autowired
    public AccountController(
            IAccountService accountService,
            ICurrencyService currencyService) {
        super();
        this.accountService = accountService;
        this.currencyService = currencyService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView showAccountsList() {
        return new ModelAndView("account/list", "accounts", accountService.getList());
    }

    @RequestMapping(value = "/edit/new", method = RequestMethod.GET)
    public ModelAndView showAccountForm() {
        final ModelAndView modelAndView = new ModelAndView("account/form");
        modelAndView.addObject("currencies", currencyService.getList());
        modelAndView.addObject("account", new AccountDto());
        return modelAndView;
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView showAccountForm(@PathVariable("id") Integer id) {
        AccountDto accountDto = accountService.get(id);
        final ModelAndView modelAndView = new ModelAndView("account/form");
        modelAndView.addObject("currencies", currencyService.getList());
        modelAndView.addObject("account", accountDto);
        return modelAndView;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String saveAccount(@Validated AccountDto accountDto) {
        accountService.save(accountDto);
        return "redirect:/account/list";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String deleteAccount(@PathVariable("id") Integer id) {
        accountService.delete(id);
        return "redirect:/account/list";
    }

}
