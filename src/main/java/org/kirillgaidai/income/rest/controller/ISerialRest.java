package org.kirillgaidai.income.rest.controller;

import org.kirillgaidai.income.rest.dto.IGenericCreateRestDto;
import org.kirillgaidai.income.rest.dto.ISerialGetRestDto;
import org.kirillgaidai.income.rest.dto.ISerialUpdateRestDto;

public interface ISerialRest<GT extends ISerialGetRestDto, CT extends IGenericCreateRestDto,
        UT extends ISerialUpdateRestDto> extends IGenericRest<GT, CT, UT> {

    GT get(Integer id);

    void delete(Integer id);

}
