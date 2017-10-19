package org.kirillgaidai.income.service.intf;

import org.kirillgaidai.income.service.dto.OperationDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface IOperationService extends ISerialService<OperationDto> {

    List<OperationDto> getDtoList(Set<Integer> accountIds, LocalDate day);

    List<OperationDto> getDtoList(Set<Integer> accountIds, LocalDate day, Integer categoryId);

    OperationDto getDto(Set<Integer> accountIds, Integer categoryId, LocalDate day);

}
