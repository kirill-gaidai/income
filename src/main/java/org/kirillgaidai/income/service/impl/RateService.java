package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.entity.RateEntity;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.dao.intf.IRateDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.RateDto;
import org.kirillgaidai.income.service.exception.IncomeServiceCurrencyNotFoundException;
import org.kirillgaidai.income.service.intf.IRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RateService implements IRateService {

    final private IRateDao rateDao;
    final private ICurrencyDao currencyDao;
    final private IGenericConverter<RateEntity, RateDto> rateConverter;

    @Autowired
    public RateService(IRateDao rateDao, ICurrencyDao currencyDao, IGenericConverter<RateEntity, RateDto> rateConverter) {
        this.rateDao = rateDao;
        this.currencyDao = currencyDao;
        this.rateConverter = rateConverter;
    }

    @Override
    public List<RateDto> getDtoList(
            Integer currencyIdFrom, Integer currencyIdTo, LocalDate firstDay, LocalDate lastDay) {
        CurrencyEntity currencyEntityFrom = currencyDao.getEntity(currencyIdFrom);
        if (currencyEntityFrom == null) {
            throw new IncomeServiceCurrencyNotFoundException(currencyIdFrom);
        }

        CurrencyEntity currencyEntityTo = currencyDao.getEntity(currencyIdTo);
        if (currencyEntityTo == null) {
            throw new IncomeServiceCurrencyNotFoundException(currencyIdTo);
        }

        List<RateDto> rateDtoList = rateDao.getEntityList(currencyIdFrom, currencyIdTo, firstDay, lastDay).stream()
                .map(rateConverter::convertToDto).collect(Collectors.toList());
        int index = 0;
        LocalDate day = firstDay;
        while (!day.isAfter(lastDay)) {
            if (index == rateDtoList.size() || rateDtoList.get(index).getDay().isAfter(day)) {
                rateDtoList.add(index, new RateDto(currencyIdFrom, null, null, currencyIdTo, null, null,
                        day, BigDecimal.ZERO));
            }
            index++;
            day = day.plusDays(1L);
        }
        rateDtoList.forEach(rateDto -> {
            rateDto.setCurrencyCodeFrom(currencyEntityFrom.getCode());
            rateDto.setCurrencyTitleFrom(currencyEntityFrom.getTitle());
            rateDto.setCurrencyCodeTo(currencyEntityTo.getCode());
            rateDto.setCurrencyTitleTo(currencyEntityTo.getTitle());
        });
        return rateDtoList;
    }

    @Override
    public RateDto getDto(Integer currencyIdFrom, Integer currencyIdTo, LocalDate day) {
        CurrencyEntity currencyEntityFrom = currencyDao.getEntity(currencyIdFrom);
        if (currencyEntityFrom == null) {
            throw new IncomeServiceCurrencyNotFoundException(currencyIdFrom);
        }

        CurrencyEntity currencyEntityTo = currencyDao.getEntity(currencyIdTo);
        if (currencyEntityTo == null) {
            throw new IncomeServiceCurrencyNotFoundException(currencyIdTo);
        }

        RateEntity rateEntity = rateDao.getEntity(currencyIdFrom, currencyIdTo, day);
        if (rateEntity == null) {
            return new RateDto(currencyIdFrom, currencyEntityFrom.getCode(), currencyEntityFrom.getTitle(),
                    currencyIdTo, currencyEntityTo.getCode(), currencyEntityTo.getTitle(), day, BigDecimal.ZERO);
        }

        RateDto rateDto = rateConverter.convertToDto(rateEntity);
        rateDto.setCurrencyCodeFrom(currencyEntityFrom.getCode());
        rateDto.setCurrencyTitleFrom(currencyEntityFrom.getTitle());
        rateDto.setCurrencyCodeTo(currencyEntityTo.getCode());
        rateDto.setCurrencyTitleTo(currencyEntityTo.getTitle());
        return rateDto;
    }

    @Override
    public void saveDto(RateDto dto) {
        RateEntity entity = rateConverter.convertToEntity(dto);
        int affectedRows = rateDao.updateEntity(entity);
        if (affectedRows == 0) {
            rateDao.insertEntity(entity);
        }
    }

    @Override
    public void deleteDto(Integer currencyIdFrom, Integer currencyIdTo, LocalDate day) {
        rateDao.deleteEntity(currencyIdFrom, currencyIdTo, day);
    }

}
