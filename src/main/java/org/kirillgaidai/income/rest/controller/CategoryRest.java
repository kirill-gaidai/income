package org.kirillgaidai.income.rest.controller;

import org.kirillgaidai.income.rest.dto.ResponseMessage;
import org.kirillgaidai.income.rest.dto.category.CategoryCreateRestDto;
import org.kirillgaidai.income.rest.dto.category.CategoryGetRestDto;
import org.kirillgaidai.income.rest.dto.category.CategoryUpdateRestDto;
import org.kirillgaidai.income.rest.mappers.IGenericRestDtoMapper;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.exception.IncomeServiceCategoryNotFoundException;
import org.kirillgaidai.income.service.intf.IGenericService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/category")
public class CategoryRest
        extends GenericRest<CategoryGetRestDto, CategoryCreateRestDto, CategoryUpdateRestDto, CategoryDto>
        implements IGenericRest<CategoryGetRestDto, CategoryCreateRestDto, CategoryUpdateRestDto> {

    @Autowired
    public CategoryRest(IGenericService<CategoryDto> service, IGenericRestDtoMapper<CategoryGetRestDto,
            CategoryCreateRestDto, CategoryUpdateRestDto, CategoryDto> mapper) {
        super(service, mapper);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CategoryGetRestDto> getList() {
        return super.getList();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CategoryGetRestDto get(@PathVariable("id") Integer id) {
        return super.get(id);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CategoryGetRestDto create(@RequestBody CategoryCreateRestDto newRestDto) {
        return super.create(newRestDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CategoryGetRestDto update(@RequestBody CategoryUpdateRestDto restDto) {
        return super.update(restDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        super.delete(id);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(IncomeServiceCategoryNotFoundException.class)
    public ResponseMessage handleNoFoundException(IncomeServiceCategoryNotFoundException exception) {
        return new ResponseMessage(exception.getMessage());
    }

}
