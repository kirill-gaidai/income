package org.kirillgaidai.income.controller;

import org.kirillgaidai.income.dto.CategoryDto;
import org.kirillgaidai.income.dto.CurrencyDto;
import org.kirillgaidai.income.service.CategoryService;
import org.kirillgaidai.income.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = {"/index.html", "/"})
    public ModelAndView showIndexPage() {
        final ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");

        modelAndView.addObject("message", "Hello, World!");

        final List<CategoryDto> categoryDtos = categoryService.getCategoryList();
        modelAndView.addObject("id", categoryDtos.get(0).getId());
        modelAndView.addObject("title", categoryDtos.get(0).getTitle());

        return modelAndView;
    }

}
