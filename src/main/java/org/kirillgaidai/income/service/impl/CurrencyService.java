package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.CurrencyDto;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.service.exception.IncomeServiceCurrencyNotFoundException;
import org.kirillgaidai.income.service.intf.ICurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CurrencyService implements ICurrencyService {

    final private ICurrencyDao currencyDao;
    final private IGenericConverter<CurrencyEntity, CurrencyDto> currencyConverter;

    @Autowired
    public CurrencyService(
            ICurrencyDao currencyDao,
            IGenericConverter<CurrencyEntity, CurrencyDto> currencyConverter) {
        super();
        this.currencyDao = currencyDao;
        this.currencyConverter = currencyConverter;
    }

    @Override
    public List<CurrencyDto> getList() {
        return currencyDao.getEntityList().stream().map(currencyConverter::convertToDto).collect(Collectors.toList());
    }

    @Override
    public List<CurrencyDto> getList(Set<Integer> ids) {
        return currencyDao.getEntityList(ids).stream().map(currencyConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CurrencyDto getDto(Integer id) {
        if (id == null) {
            throw new IncomeServiceCurrencyNotFoundException();
        }
        CurrencyEntity currencyEntity = currencyDao.getEntity(id);
        if (currencyEntity == null) {
            throw new IncomeServiceCurrencyNotFoundException(id);
        }
        return currencyConverter.convertToDto(currencyEntity);
    }

    @Override
    public void saveDto(CurrencyDto currencyDto) {
        if (currencyDto == null) {
            throw new IncomeServiceCurrencyNotFoundException();
        }
        CurrencyEntity currencyEntity = currencyConverter.convertToEntity(currencyDto);
        if (currencyEntity.getId() == null) {
            currencyDao.insertEntity(currencyEntity);
            currencyDto.setId(currencyEntity.getId());
            return;
        }
        int affectedRows = currencyDao.updateEntity(currencyEntity);
        if (affectedRows != 1) {
            throw new IncomeServiceCurrencyNotFoundException(currencyDto.getId());
        }
    }

    @Override
    public void deleteDto(Integer id) {
        if (id == null) {
            throw new IncomeServiceCurrencyNotFoundException();
        }
        int affectedRows = currencyDao.deleteEntity(id);
        if (affectedRows != 1) {
            throw new IncomeServiceCurrencyNotFoundException(id);
        }
    }

}
