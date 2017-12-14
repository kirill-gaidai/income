package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.CurrencyDto;
import org.kirillgaidai.income.service.intf.ICurrencyService;
import org.kirillgaidai.income.service.util.ServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class CurrencyService extends SerialService<CurrencyDto, CurrencyEntity> implements ICurrencyService {

    final private static Logger LOGGER = LoggerFactory.getLogger(CurrencyService.class);

    @Autowired
    public CurrencyService(
            ICurrencyDao dao,
            ServiceHelper serviceHelper,
            IGenericConverter<CurrencyEntity, CurrencyDto> converter) {
        super(dao, converter, serviceHelper);
    }

    @Override
    @Transactional
    public List<CurrencyDto> getList() {
        LOGGER.debug("Entering method");
        return super.getList();
    }

    @Override
    @Transactional
    public List<CurrencyDto> getList(Set<Integer> ids) {
        LOGGER.debug("Entering method");
        return super.getList(ids);
    }

    @Override
    @Transactional
    public CurrencyDto get(Integer id) {
        LOGGER.debug("Entering method");
        validateId(id);
        return converter.convertToDto(serviceHelper.getCurrencyEntity(id));
    }

    @Override
    @Transactional
    public CurrencyDto create(CurrencyDto dto) {
        LOGGER.debug("Entering method");
        validateDto(dto);
        CurrencyEntity entity = converter.convertToEntity(dto);
        serviceHelper.createCurrencyEntity(entity);
        return converter.convertToDto(entity);
    }

    @Override
    @Transactional
    public CurrencyDto update(CurrencyDto dto) {
        LOGGER.debug("Entering method");
        validateDto(dto);
        Integer id = dto.getId();
        validateId(id);
        CurrencyEntity oldEntity = serviceHelper.getCurrencyEntity(id);
        CurrencyEntity newEntity = converter.convertToEntity(dto);
        serviceHelper.updateCurrencyEntity(newEntity, oldEntity);
        return converter.convertToDto(newEntity);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        LOGGER.debug("Entering method");
        validateId(id);
        CurrencyEntity entity = serviceHelper.getCurrencyEntity(id);
        serviceHelper.checkCurrencyDependentAccounts(id);
        serviceHelper.deleteCurrencyEntity(entity);
    }

}
