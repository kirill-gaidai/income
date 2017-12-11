package org.kirillgaidai.income.service.intf;

import org.kirillgaidai.income.service.dto.SummaryDto;

import java.time.LocalDate;

public interface ISummaryService {

    SummaryDto get(Integer categoryId, LocalDate firstDay, LocalDate lastDay);

}
