package org.kirillgaidai.income.service.intf;

import org.kirillgaidai.income.service.dto.IGenericDto;

import java.util.List;
import java.util.Set;

public interface IGenericService<T extends IGenericDto> {

    List<T> getList();

    List<T> getList(Set<Integer> ids);

    T getDto(Integer id);

    void saveDto(T operationDto);

    void deleteDto(Integer id);

}
