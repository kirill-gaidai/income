package org.kirillgaidai.income.web.rest;

import org.kirillgaidai.income.dto.CategoryDto;
import org.kirillgaidai.income.dto.rest.CategoryListDto;
import org.kirillgaidai.income.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/category")
public class CategoryRest {

    private CategoryService categoryService;

    @Autowired
    public CategoryRest(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(value = "/{id}", produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity getCategoryById(final @PathVariable("id") Long id) {
        final CategoryDto categoryDto = categoryService.getCategoryById(id);
        return ResponseEntity.ok(categoryDto);
    }

    @RequestMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity getCategoryList() {
        final CategoryListDto categoryListDto = new CategoryListDto();
        categoryListDto.setCategoryDtoList(categoryService.getCategoryList());
        return ResponseEntity.ok(categoryListDto);
    }

}
