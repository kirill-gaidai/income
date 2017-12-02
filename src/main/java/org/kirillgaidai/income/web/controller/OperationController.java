package org.kirillgaidai.income.web.controller;

import org.kirillgaidai.income.service.dto.OperationDto;
import org.kirillgaidai.income.service.intf.IAccountService;
import org.kirillgaidai.income.service.intf.ICategoryService;
import org.kirillgaidai.income.service.intf.IOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

@Controller
@RequestMapping("/operation")
public class OperationController {

    final private IAccountService accountService;
    final private IOperationService operationService;
    final private ICategoryService categoryService;

    @Autowired
    public OperationController(
            IAccountService accountService,
            IOperationService operationService,
            ICategoryService categoryService) {
        this.accountService = accountService;
        this.operationService = operationService;
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView showOperations(
            @RequestParam("account_id") Set<Integer> accountIds,
            @RequestParam("day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate day,
            @RequestParam(value = "category_id", required = false) Integer categoryId,
            @RequestParam("return_account_id") Set<Integer> returnAccountIds,
            @RequestParam("return_first_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnFirstDay,
            @RequestParam("return_last_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnLastDay) {
        ModelAndView modelAndView = new ModelAndView("operation/form");

        modelAndView.addObject("operationDto", operationService.get(accountIds, categoryId ,day));
        if (accountIds.size() != 1) {
            modelAndView.addObject("accountDtoList", accountService.getList(accountIds));
        }
        if (categoryId == null) {
            modelAndView.addObject("categoryDtoList", categoryService.getList());
        }

        if (categoryId == null) {
            modelAndView.addObject("operationDtoList",
                    operationService.getList(accountIds, Collections.emptySet(), day, day));
        } else {
            modelAndView.addObject("operationDtoList",
                    operationService.getList(accountIds, Collections.singleton(categoryId), day, day));
        }

        modelAndView.addObject("returnAccountIds", returnAccountIds);
        modelAndView.addObject("returnFirstDay", returnFirstDay);
        modelAndView.addObject("returnLastDay", returnLastDay);

        return modelAndView;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String saveAccountOperation(
            @Validated OperationDto operationDto,
            @RequestParam("return_account_id") Set<Integer> returnAccountIds,
            @RequestParam("return_first_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnFirstDay,
            @RequestParam("return_last_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnLastDay) {
        operationService.save(operationDto);
        StringBuilder result = new StringBuilder("redirect:/summary")
                .append("?first_day=").append(returnFirstDay)
                .append("&last_day=").append(returnLastDay);
        for (Integer returnAccountId : returnAccountIds) {
            result.append("&account_id=").append(returnAccountId);
        }
        return result.toString();
    }

    @RequestMapping(value = "/{operationId}/delete", method = RequestMethod.POST)
    public String deleteAccountOperation(
            @PathVariable("operationId") Integer operationId,
            @RequestParam("return_account_id") Set<Integer> returnAccountIds,
            @RequestParam("return_first_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnFirstDay,
            @RequestParam("return_last_day") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnLastDay) {
        operationService.delete(operationId);
        StringBuilder result = new StringBuilder("redirect:/summary")
                .append("?first_day=").append(returnFirstDay)
                .append("&last_day=").append(returnLastDay);
        for (Integer returnAccountId : returnAccountIds) {
            result.append("&account_id=").append(returnAccountId);
        }
        return result.toString();
    }

}
