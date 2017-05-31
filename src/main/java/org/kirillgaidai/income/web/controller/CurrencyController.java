package org.kirillgaidai.income.web.controller;

import org.kirillgaidai.income.service.dto.CurrencyDto;
import org.kirillgaidai.income.web.exception.IncomeControllerNotFoundException;
import org.kirillgaidai.income.service.intf.ICurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/currency")
public class CurrencyController {

    final private ICurrencyService currencyService;

    @Autowired
    public CurrencyController(ICurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView showCurrencyList() {
        return new ModelAndView("currency/list", "currencies", currencyService.getDtoList());
    }

    @RequestMapping(value = "/edit/new", method = RequestMethod.GET)
    public ModelAndView showCurrencyForm() {
        return new ModelAndView("currency/form", "currencyDto", new CurrencyDto());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/edit/{id}")
    public ModelAndView showCurrencyForm(@PathVariable("id") Integer id) {
        CurrencyDto currencyDto = currencyService.getDto(id);
        if (currencyDto == null) {
            throw new IncomeControllerNotFoundException("currency with id " + id + " not found");
        }
        return new ModelAndView("currency/form", "currencyDto", currencyDto);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String saveCategory(@Validated CurrencyDto currencyDto) {
        currencyService.saveDto(currencyDto);
        return "redirect:/currency/list";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String deleteCategory(@PathVariable("id") Integer id) {
        currencyService.deleteDto(id);
        return "redirect:/currency/list";
    }

}
