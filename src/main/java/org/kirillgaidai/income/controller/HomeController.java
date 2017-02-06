package org.kirillgaidai.income.controller;

import org.kirillgaidai.income.dto.CurrencyDto;
import org.kirillgaidai.income.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @Autowired
    private CurrencyService currencyService;

    @RequestMapping(value = {"/index.html", "/"})
    public ModelAndView showIndexPage() {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");

        modelAndView.addObject("message", "Hello, World!");

        final CurrencyDto currencyDto = currencyService.getCurrencyById(33L);
        modelAndView.addObject("id", currencyDto.getId());
        modelAndView.addObject("code", currencyDto.getCode());
        modelAndView.addObject("title", currencyDto.getTitle());

        return modelAndView;
    }

}
