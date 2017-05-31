package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.dao.intf.IOperationDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.dto.SummaryDto;
import org.kirillgaidai.income.service.intf.ISummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SummaryService implements ISummaryService {

    final private IAccountDao accountDao;
    final private ICategoryDao categoryDao;
    final private IOperationDao operationDao;
    final private IBalanceDao balanceDao;
    final private IGenericConverter<AccountEntity, AccountDto> accountConverter;
    final private IGenericConverter<CategoryEntity, CategoryDto> categoryConverter;

    @Autowired
    public SummaryService(
            IAccountDao accountDao,
            ICategoryDao categoryDao,
            IOperationDao operationDao,
            IBalanceDao balanceDao,
            IGenericConverter<AccountEntity, AccountDto> accountConverter,
            IGenericConverter<CategoryEntity, CategoryDto> categoryConverter) {
        this.accountDao = accountDao;
        this.categoryDao = categoryDao;
        this.operationDao = operationDao;
        this.balanceDao = balanceDao;
        this.accountConverter = accountConverter;
        this.categoryConverter = categoryConverter;
    }

    @Override
    public SummaryDto getSummaryDto(Set<Integer> accountIds, LocalDate firstDay, LocalDate lastDay) {
        LocalDate previousDay = firstDay.minusDays(1L);

        List<OperationEntity> operationEntityList = operationDao.getEntityList(accountIds, firstDay, lastDay);
        List<BalanceEntity> balanceEntityList = balanceDao.getEntityList(accountIds, previousDay, lastDay);

        Set<Integer> categoryIds = operationEntityList.stream().map(OperationEntity::getCategoryId)
                .collect(Collectors.toSet());

        List<AccountEntity> accountEntityList = accountDao.getEntityList(accountIds);
        Map<Integer, Integer> accountIndexes = new HashMap<>();
        for (int index = 0; index < accountEntityList.size(); index++) {
            accountIndexes.put(accountEntityList.get(index).getId(), index);
        }

        List<CategoryEntity> categoryEntityList = categoryDao.getEntityList(categoryIds);
        Map<Integer, Integer> categoryIndexes = new HashMap<>();
        for (int index = 0; index < categoryEntityList.size(); index++) {
            categoryIndexes.put(categoryEntityList.get(index).getId(), index);
        }

        List<List<BigDecimal>> amounts = getAmounts(operationEntityList, firstDay, lastDay, categoryIndexes);
        List<List<BigDecimal>> balances = getBalances(balanceEntityList, previousDay, lastDay, accountIndexes);

        List<SummaryDto.SummaryDtoRow> summaryDtoRowList = new ArrayList<>();
        BigDecimal prevBalancesSummary = balances.get(0).stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        LocalDate day = firstDay;
        for (int index = 0; index < amounts.size(); index++) {
            List<BigDecimal> amountsRow = amounts.get(index);
            BigDecimal amountsSummary = amountsRow.stream().reduce(BigDecimal.ZERO, BigDecimal::add);

            List<BigDecimal> balancesRow = balances.get(index + 1);
            BigDecimal balancesSummary = balancesRow.stream().reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal difference = balancesSummary.subtract(prevBalancesSummary).add(amountsSummary);

            summaryDtoRowList.add(new SummaryDto.SummaryDtoRow(day, difference, balancesRow, balancesSummary,
                    amountsRow, amountsSummary));

            day = day.plusDays(1L);
            prevBalancesSummary = balancesSummary;
        }

        SummaryDto summaryDto = new SummaryDto();
        summaryDto.setAccountDtoList(accountEntityList.stream().map(accountConverter::convertToDto)
                .collect(Collectors.toList()));
        summaryDto.setCategoryDtoList(categoryEntityList.stream().map(categoryConverter::convertToDto)
                .collect(Collectors.toList()));
        summaryDto.setSummaryDtoRowList(summaryDtoRowList);
        return summaryDto;
    }

    private List<List<BigDecimal>> getAmounts(
            List<OperationEntity> operationEntityList, LocalDate firstDay, LocalDate lastDay,
            Map<Integer, Integer> categoryIndexes) {
        int height = (int) (ChronoUnit.DAYS.between(firstDay, lastDay) + 1);
        int width = categoryIndexes.size();

        // Generating amounts matrix and filling it with zeros
        List<List<BigDecimal>> result = Stream
                .generate(() -> Stream.generate(() -> BigDecimal.ZERO).limit(width).collect(Collectors.toList()))
                .limit(height).collect(Collectors.toList());

        // Summarizing results
        for (OperationEntity operationEntity : operationEntityList) {
            int y = (int) ChronoUnit.DAYS.between(firstDay, operationEntity.getDay());
            int x = categoryIndexes.get(operationEntity.getCategoryId());
            List<BigDecimal> row = result.get(y);
            BigDecimal amount = row.get(x);
            amount = amount.add(operationEntity.getAmount());
            row.set(x, amount);
        }

        return result;
    }

    private List<List<BigDecimal>> getBalances(
            List<BalanceEntity> balanceEntityList, LocalDate firstDay, LocalDate lastDay,
            Map<Integer, Integer> accountIndexes) {
        int height = (int) (ChronoUnit.DAYS.between(firstDay, lastDay) + 1);
        int width = accountIndexes.size();

        // Generating amounts matrix and filling it with zeros
        List<List<BigDecimal>> result = Stream
                .generate(() -> Stream.generate(() -> BigDecimal.ZERO).limit(width).collect(Collectors.toList()))
                .limit(height).collect(Collectors.toList());

        // Summarizing results
        for (BalanceEntity balanceEntity : balanceEntityList) {
            int y = (int) ChronoUnit.DAYS.between(firstDay, balanceEntity.getDay());
            int x = accountIndexes.get(balanceEntity.getAccountId());
            result.get(y).set(x, balanceEntity.getAmount());
        }

        return result;
    }

}
