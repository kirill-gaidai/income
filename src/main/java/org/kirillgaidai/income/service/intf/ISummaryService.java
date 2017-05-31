package org.kirillgaidai.income.service.intf;

import org.kirillgaidai.income.service.dto.SummaryDto;

import java.time.LocalDate;
import java.util.Set;

public interface ISummaryService {

    SummaryDto getSummaryDto(Set<Integer> accountIds, LocalDate firstDay, LocalDate lastDay);

}
