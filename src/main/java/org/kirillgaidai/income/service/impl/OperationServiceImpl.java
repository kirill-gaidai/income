package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.BalanceDao;
import org.kirillgaidai.income.dao.OperationDao;
import org.kirillgaidai.income.dto.OperationDto;
import org.kirillgaidai.income.entity.OperationEntity;
import org.kirillgaidai.income.service.OperationService;
import org.springframework.stereotype.Repository;

@Repository
public class OperationServiceImpl implements OperationService {

    private OperationDao operationDao;
    private BalanceDao balanceDao;

    public OperationServiceImpl(final OperationDao operationDao, final BalanceDao balanceDao) {
        this.operationDao = operationDao;
        this.balanceDao = balanceDao;
    }

    @Override
    public void saveDto(final OperationDto operationDto) {
        operationDao.insertEntity(convertToOperationEntity(operationDto));
    }

    private OperationEntity convertToOperationEntity(final OperationDto operationDto) {
        final OperationEntity operationEntity = new OperationEntity();
        operationEntity.setId(operationDto.getId());
        operationEntity.setAccountId(operationDto.getAccountId());
        operationEntity.setCategoryId(operationDto.getCategoryId());
        operationEntity.setAmount(operationDto.getAmount());
        operationEntity.setDay(operationDto.getDay());
        operationEntity.setNote(operationDto.getNote());
        return operationEntity;
    }

}
