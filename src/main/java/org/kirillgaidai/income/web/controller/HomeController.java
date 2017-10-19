package org.kirillgaidai.income.web.controller;

import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.intf.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
public class HomeController {

    final private IAccountService accountService;

    @Autowired
    public HomeController(
            IAccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = {"/index.html", "/"}, method = RequestMethod.GET)
    public ModelAndView showIndexPage() {
        LocalDate lastDay = LocalDate.now();
        LocalDate firstDay = lastDay.minusDays(10L);

        List<AccountDto> accountDtoList = accountService.getList();

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("lastDay", lastDay);
        modelAndView.addObject("firstDay", firstDay);
        modelAndView.addObject("accountDtoList", accountDtoList);
        return modelAndView;
    }

}
