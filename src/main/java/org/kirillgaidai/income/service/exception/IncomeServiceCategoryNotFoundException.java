package org.kirillgaidai.income.service.exception;

public class IncomeServiceCategoryNotFoundException extends IncomeServiceNotFoundException {

    public IncomeServiceCategoryNotFoundException() {
        super("Category not found");
    }

    public IncomeServiceCategoryNotFoundException(Integer id) {
        super(String.format("Category with id %d not found", id));
    }

}
