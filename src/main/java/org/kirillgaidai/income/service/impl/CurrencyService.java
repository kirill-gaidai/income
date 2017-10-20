package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.CurrencyDto;
import org.kirillgaidai.income.service.exception.IncomeServiceCurrencyNotFoundException;
import org.kirillgaidai.income.service.intf.ICurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService extends SerialService<CurrencyDto, CurrencyEntity> implements ICurrencyService {

    @Autowired
    public CurrencyService(
            ICurrencyDao dao,
            IGenericConverter<CurrencyEntity, CurrencyDto> converter) {
        super(dao, converter);
    }

    @Override
    protected void throwNotFoundException(Integer id) {
        throw new IncomeServiceCurrencyNotFoundException(id);
    }

}
