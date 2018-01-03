package org.kirillgaidai.income.rest.controller;

import org.kirillgaidai.income.rest.dto.category.CategoryCreateRestDto;
import org.kirillgaidai.income.rest.dto.category.CategoryGetRestDto;
import org.kirillgaidai.income.rest.dto.category.CategoryUpdateRestDto;
import org.kirillgaidai.income.rest.mappers.IGenericRestDtoMapper;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.intf.IGenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/rest/categories")
public class CategoryRest
        extends SerialRest<CategoryGetRestDto, CategoryCreateRestDto, CategoryUpdateRestDto, CategoryDto>
        implements ISerialRest<CategoryGetRestDto, CategoryCreateRestDto, CategoryUpdateRestDto> {

    final private static Logger LOGGER = LoggerFactory.getLogger(CategoryRest.class);

    @Autowired
    public CategoryRest(
            IGenericService<CategoryDto> service,
            IGenericRestDtoMapper<
                    CategoryGetRestDto, CategoryCreateRestDto, CategoryUpdateRestDto, CategoryDto> mapper) {
        super(service, mapper);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<CategoryGetRestDto> getList() {
        LOGGER.debug("Entering method");
        return super.getList();
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public CategoryGetRestDto get(@PathVariable("id") Integer id) {
        LOGGER.debug("Entering method");
        return super.get(id);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CategoryGetRestDto create(@Valid @RequestBody CategoryCreateRestDto newRestDto) {
        LOGGER.debug("Entering method");
        return super.create(newRestDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CategoryGetRestDto update(@Valid @RequestBody CategoryUpdateRestDto restDto) {
        LOGGER.debug("Entering method");
        return super.update(restDto);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Integer id) {
        LOGGER.debug("Entering method");
        super.delete(id);
    }

}
