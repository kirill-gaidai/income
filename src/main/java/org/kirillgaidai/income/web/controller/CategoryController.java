package org.kirillgaidai.income.web.controller;

import org.kirillgaidai.income.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView showCategoryForm() {
        return new ModelAndView("category/form");
    }

}
