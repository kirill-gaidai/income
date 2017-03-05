package org.kirillgaidai.income.web.controller;

import org.kirillgaidai.income.dto.AccountDto;
import org.kirillgaidai.income.dto.CategoryDto;
import org.kirillgaidai.income.dto.OperationDto;
import org.kirillgaidai.income.exception.IncomeControllerNotFoundException;
import org.kirillgaidai.income.service.AccountService;
import org.kirillgaidai.income.service.CategoryService;
import org.kirillgaidai.income.service.OperationService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/account")
public class OperationController {

    private AccountService accountService;
    private OperationService operationService;
    private CategoryService categoryService;

    public OperationController(
            final AccountService accountService, final OperationService operationService,
            final CategoryService categoryService) {
        this.accountService = accountService;
        this.operationService = operationService;
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "/{id}/operation/edit/new")
    public ModelAndView showOperationForm(final @PathVariable("id") Integer id) {
        AccountDto accountDto = accountService.getAccountById(id);
        if (accountDto == null) {
            throw new IncomeControllerNotFoundException("account with id %d not found", id);
        }

        final OperationDto operationDto = new OperationDto();
        operationDto.setAccountId(id);
        operationDto.setDay(new Date());

        final List<CategoryDto> categories = categoryService.getCategoryList();

        final ModelAndView modelAndView = new ModelAndView("operation/form");
        modelAndView.addObject("account", accountDto);
        modelAndView.addObject("operation", operationDto);
        modelAndView.addObject("categories", categories);
        return modelAndView;
    }

    @RequestMapping(value = "/{id}/operation/edit", method = RequestMethod.POST)
    public String saveAccountOperation(
            final @PathVariable("id") Integer id, final @Validated OperationDto operationDto) {
        operationService.saveDto(operationDto);
        return "redirect:/account/list";
    }

}
