package org.kirillgaidai.income.service.intf;

import org.kirillgaidai.income.service.dto.OperationDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface IOperationService extends ISerialService<OperationDto> {

    List<OperationDto> getList(
            Set<Integer> accountIds, Set<Integer> categoryIds, LocalDate firstDay, LocalDate lastDay);

}
