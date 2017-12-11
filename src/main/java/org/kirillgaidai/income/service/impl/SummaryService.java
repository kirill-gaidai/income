package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.dao.intf.IOperationDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.dto.BalanceDto;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.dto.CurrencyDto;
import org.kirillgaidai.income.service.dto.OperationDto;
import org.kirillgaidai.income.service.dto.SummaryDto;
import org.kirillgaidai.income.service.intf.ISummaryService;
import org.kirillgaidai.income.service.util.ServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SummaryService implements ISummaryService {

    final private IAccountDao accountDao;
    final private IBalanceDao balanceDao;
    final private ICategoryDao categoryDao;
    final private IOperationDao operationDao;
    final private IGenericConverter<AccountEntity, AccountDto> accountConverter;
    final private IGenericConverter<BalanceEntity, BalanceDto> balanceConverter;
    final private IGenericConverter<CategoryEntity, CategoryDto> categoryConverter;
    final private IGenericConverter<CurrencyEntity, CurrencyDto> currencyConverter;
    final private IGenericConverter<OperationEntity, OperationDto> operationConverter;
    final private ServiceHelper serviceHelper;

    @Autowired
    public SummaryService(
            IAccountDao accountDao,
            ICategoryDao categoryDao,
            IOperationDao operationDao,
            IBalanceDao balanceDao,
            IGenericConverter<AccountEntity, AccountDto> accountConverter,
            IGenericConverter<BalanceEntity, BalanceDto> balanceConverter,
            IGenericConverter<CategoryEntity, CategoryDto> categoryConverter,
            IGenericConverter<CurrencyEntity, CurrencyDto> currencyConverter,
            IGenericConverter<OperationEntity, OperationDto> operationConverter,
            ServiceHelper serviceHelper) {
        this.accountDao = accountDao;
        this.categoryDao = categoryDao;
        this.operationDao = operationDao;
        this.balanceDao = balanceDao;
        this.accountConverter = accountConverter;
        this.balanceConverter = balanceConverter;
        this.categoryConverter = categoryConverter;
        this.currencyConverter = currencyConverter;
        this.operationConverter = operationConverter;
        this.serviceHelper = serviceHelper;
    }

    /**
     * Outputs operations and account balances summary
     *
     * @param currencyId - currencyId
     * @param firstDay   - first day of report
     * @param lastDay    - last day of report
     * @return summary dto
     */
    @Override
    public SummaryDto get(Integer currencyId, LocalDate firstDay, LocalDate lastDay) {
        CurrencyEntity currencyEntity = serviceHelper.getCurrencyEntity(currencyId);
        List<AccountEntity> accountEntityList = accountDao.getList(currencyId);
        List<CategoryEntity> categoryEntityList = categoryDao.getList();

        Set<Integer> accountIds = accountEntityList.stream().map(AccountEntity::getId).collect(Collectors.toSet());
        Set<Integer> categoryIds = categoryEntityList.stream().map(CategoryEntity::getId).collect(Collectors.toSet());
        List<BalanceEntity> initialBalanceEntityList = balanceDao.getList(accountIds, firstDay.minusDays(1L));
        List<BalanceEntity> balanceEntityList = balanceDao.getList(accountIds, firstDay, lastDay);
        List<OperationEntity> operationEntityList = operationDao.getList(accountIds, categoryIds, firstDay, lastDay);

        Map<Integer, String> accountMap = accountEntityList.stream()
                .collect(Collectors.toMap(AccountEntity::getId, AccountEntity::getTitle));
        Map<Integer, String> categoryMap = categoryEntityList.stream()
                .collect(Collectors.toMap(CategoryEntity::getId, CategoryEntity::getTitle));

        CurrencyDto currencyDto = currencyConverter.convertToDto(currencyEntity);
        List<AccountDto> accountDtoList = accountEntityList.stream()
                .map(accountConverter::convertToDto)
                .peek(dto -> {
                    dto.setCurrencyCode(currencyEntity.getCode());
                    dto.setCurrencyTitle(currencyEntity.getTitle());
                })
                .collect(Collectors.toList());
        List<CategoryDto> categoryDtoList = categoryEntityList.stream()
                .map(categoryConverter::convertToDto)
                .collect(Collectors.toList());
        List<BalanceDto> initialBalanceDtoList = initialBalanceEntityList.stream()
                .map(balanceConverter::convertToDto)
                .peek(dto -> dto.setAccountTitle(accountMap.get(dto.getAccountId())))
                .collect(Collectors.toList());
        List<BalanceDto> balanceDtoList = balanceEntityList.stream()
                .map(balanceConverter::convertToDto)
                .peek(dto -> dto.setAccountTitle(accountMap.get(dto.getAccountId())))
                .collect(Collectors.toList());
        List<OperationDto> operationDtoList = operationEntityList.stream()
                .map(operationConverter::convertToDto)
                .peek(dto -> {
                    dto.setCategoryTitle(categoryMap.get(dto.getCategoryId()));
                    dto.setAccountTitle(accountMap.get(dto.getAccountId()));
                })
                .collect(Collectors.toList());

        return new SummaryDto(
                firstDay, lastDay, currencyDto, accountDtoList, categoryDtoList,
                initialBalanceDtoList, balanceDtoList, operationDtoList);
    }

}
