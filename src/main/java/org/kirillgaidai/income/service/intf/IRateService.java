package org.kirillgaidai.income.service.intf;

import org.kirillgaidai.income.service.dto.RateDto;

import java.time.LocalDate;
import java.util.List;

public interface IRateService {

    List<RateDto> getDtoList(Integer currencyIdFrom, Integer currencyIdTo, LocalDate firstDay, LocalDate lastDay);

    RateDto getDto(Integer currencyIdFrom, Integer currencyIdTo, LocalDate day);

    void saveDto(RateDto dto);

    void deleteDto(Integer currencyIdFrom, Integer currencyIdTo, LocalDate day);

}
