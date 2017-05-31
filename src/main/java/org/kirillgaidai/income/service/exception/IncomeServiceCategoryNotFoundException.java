package org.kirillgaidai.income.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Category not found")
public class IncomeServiceCategoryNotFoundException extends IncomeServiceNotFoundException {

    public IncomeServiceCategoryNotFoundException() {
        super("Category not found");
    }

    public IncomeServiceCategoryNotFoundException(final Integer id) {
        super(String.format("Category with id %d not found", id));
    }

}
