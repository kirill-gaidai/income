package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.BalanceDto;
import org.kirillgaidai.income.service.exception.IncomeServiceAccountNotFoundException;
import org.kirillgaidai.income.service.exception.IncomeServiceBalanceNotFoundException;
import org.kirillgaidai.income.service.intf.IBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class BalanceService implements IBalanceService {

    final private IBalanceDao balanceDao;
    final private IAccountDao accountDao;
    final private IGenericConverter<BalanceEntity, BalanceDto> balanceConverter;

    @Autowired
    public BalanceService(
            IBalanceDao balanceDao,
            IAccountDao accountDao,
            IGenericConverter<BalanceEntity, BalanceDto> balanceConverter) {
        super();
        this.balanceDao = balanceDao;
        this.accountDao = accountDao;
        this.balanceConverter = balanceConverter;
    }

    @Override
    public List<BalanceDto> getList() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BalanceDto get(Integer accountId, LocalDate day) {
        if (accountId == null || day == null) {
            throw new IncomeServiceBalanceNotFoundException();
        }

        AccountEntity accountEntity = accountDao.get(accountId);
        if (accountEntity == null) {
            throw new IncomeServiceAccountNotFoundException(accountId);
        }

        BalanceEntity balanceEntity = balanceDao.get(accountId, day);
        if (balanceEntity != null) {
            BalanceDto dto = balanceConverter.convertToDto(balanceEntity);
            dto.setAccountTitle(accountEntity.getTitle());
            return dto;
        }

        balanceEntity = balanceDao.getEntityBefore(accountId, day);
        if (balanceEntity != null) {
            return new BalanceDto(accountId, accountEntity.getTitle(), day, balanceEntity.getAmount(), false);
        }

        balanceEntity = balanceDao.getEntityAfter(accountId, day);
        if (balanceEntity != null) {
            return new BalanceDto(accountId, accountEntity.getTitle(), day, balanceEntity.getAmount(), false);
        }

        return new BalanceDto(accountId, accountEntity.getTitle(), day, BigDecimal.ZERO, false);
    }

    @Override
    public BalanceDto save(BalanceDto dto) {
        if (dto == null) {
            throw new IncomeServiceBalanceNotFoundException();
        }

        if (dto.getAccountId() == null) {
            throw new IncomeServiceAccountNotFoundException();
        }

        AccountEntity accountEntity = accountDao.get(dto.getAccountId());
        if (accountEntity == null) {
            throw new IncomeServiceAccountNotFoundException(dto.getAccountId());
        }

        BalanceEntity entity = balanceConverter.convertToEntity(dto);
        int affectedRows = balanceDao.update(entity);
        if (affectedRows == 0) {
            balanceDao.insert(entity);
        }
        return null;
    }

}
