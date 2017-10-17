package org.kirillgaidai.income.web.controller;

import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.web.exception.IncomeControllerNotFoundException;
import org.kirillgaidai.income.service.intf.ICategoryService;
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
@RequestMapping(value = "/category")
public class CategoryController {

    final private ICategoryService categoryService;

    @Autowired
    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView showCategories() {
        return new ModelAndView("category/list", "categories", categoryService.getList());
    }

    @RequestMapping(value = "/edit/new", method = RequestMethod.GET)
    public ModelAndView showCategoryForm() {
        return new ModelAndView("category/form", "categoryDto", new CategoryDto());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView showCategoryForm(@PathVariable("id") Integer id) {
        CategoryDto categoryDto = categoryService.get(id);
        if (categoryDto == null) {
            throw new IncomeControllerNotFoundException("category with id " + id + " not found");
        }
        return new ModelAndView("category/form", "categoryDto", categoryDto);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String saveCategory(@Validated CategoryDto categoryDto) {
        categoryService.save(categoryDto);
        return "redirect:/category/list";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String deleteCategory(@PathVariable("id") Integer id) {
        categoryService.deleteDto(id);
        return "redirect:/category/list";
    }

}
