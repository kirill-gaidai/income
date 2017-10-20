package org.kirillgaidai.income.rest.controller;

import org.kirillgaidai.income.rest.dto.IGenericCreateRestDto;
import org.kirillgaidai.income.rest.dto.IGenericGetRestDto;
import org.kirillgaidai.income.rest.dto.IGenericUpdateRestDto;

import java.util.List;

public interface IGenericRest<GT extends IGenericGetRestDto, CT extends IGenericCreateRestDto,
        UT extends IGenericUpdateRestDto> {

    List<GT> getList();

    GT create(CT newRestDto);

    GT update(UT restDto);

}
