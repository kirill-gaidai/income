package org.kirillgaidai.income.web.controller;

import org.kirillgaidai.income.dto.CurrencyDto;
import org.kirillgaidai.income.exception.IncomeControllerNotFoundException;
import org.kirillgaidai.income.service.CurrencyService;
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

    private CurrencyService currencyService;

    @Autowired
    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView showCurrencyList() {
        return new ModelAndView("currency/list", "currencies", currencyService.getCurrencyList());
    }

    @RequestMapping(value = "/edit/new", method = RequestMethod.GET)
    public ModelAndView showCurrencyForm() {
        return new ModelAndView("currency/form", "currencyDto", new CurrencyDto());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/edit/{id}")
    public ModelAndView showCurrencyForm(final @PathVariable("id") Integer id) {
        final CurrencyDto currencyDto = currencyService.getCurrencyById(id);
        if (currencyDto == null) {
            throw new IncomeControllerNotFoundException("currency with id %d not found", id);
        }
        return new ModelAndView("currency/form", "currencyDto", currencyDto);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String saveCategory(final @Validated CurrencyDto currencyDto) {
        currencyService.saveCurrency(currencyDto);
        return "redirect:/currency/list";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String deleteCategory(final @PathVariable("id") Integer id) {
        currencyService.deleteCurrency(id);
        return "redirect:/currency/list";
    }

}
