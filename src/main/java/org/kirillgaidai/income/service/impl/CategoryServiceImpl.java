package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.CategoryDao;
import org.kirillgaidai.income.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

}
