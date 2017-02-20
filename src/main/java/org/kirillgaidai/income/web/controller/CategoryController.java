package org.kirillgaidai.income.web.controller;

import org.kirillgaidai.income.dto.CategoryDto;
import org.kirillgaidai.income.exception.IncomeControllerNotFoundException;
import org.kirillgaidai.income.service.CategoryService;
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

    private CategoryService categoryService;

    @Autowired
    public CategoryController(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ModelAndView showCategories() {
        return new ModelAndView("category/list", "categories", categoryService.getCategoryList());
    }

    @RequestMapping(value = "/edit/new", method = RequestMethod.GET)
    public ModelAndView showCategoryForm() {
        return new ModelAndView("category/form", "categoryDto", new CategoryDto());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public ModelAndView showCategoryForm(final @PathVariable("id") Integer id) {
        final CategoryDto categoryDto = categoryService.getCategoryById(id);
        if (categoryDto == null) {
            throw new IncomeControllerNotFoundException("category with id %d not found", id);
        }
        return new ModelAndView("category/form", "categoryDto", categoryDto);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String saveCategory(final @Validated CategoryDto categoryDto) {
        categoryService.saveCategory(categoryDto);
        return "redirect:/category/list";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String deleteCategory(final @PathVariable("id") Integer id) {
        categoryService.deleteCategory(id);
        return "redirect:/category/list";
    }

}
