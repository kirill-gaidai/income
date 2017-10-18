package org.kirillgaidai.income.service.impl;

import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.dao.intf.IOperationDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.dto.SummaryDto;
import org.kirillgaidai.income.service.intf.ISummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    final private ICurrencyDao currencyDao;
    final private IGenericConverter<AccountEntity, AccountDto> accountConverter;
    final private IGenericConverter<CategoryEntity, CategoryDto> categoryConverter;

    @Autowired
    public SummaryService(
            IAccountDao accountDao,
            ICategoryDao categoryDao,
            IOperationDao operationDao,
            IBalanceDao balanceDao,
            ICurrencyDao currencyDao,
            IGenericConverter<AccountEntity, AccountDto> accountConverter,
            IGenericConverter<CategoryEntity, CategoryDto> categoryConverter) {
        this.accountDao = accountDao;
        this.categoryDao = categoryDao;
        this.operationDao = operationDao;
        this.balanceDao = balanceDao;
        this.currencyDao = currencyDao;
        this.accountConverter = accountConverter;
        this.categoryConverter = categoryConverter;
    }

    /**
     * Outputs operations and account balances summary
     *
     * @param accountIds - set of account ids
     * @param firstDay   - first day of report
     * @param lastDay    - last day of report
     * @return summary dto
     */
    @Override
    public SummaryDto getSummaryDto(Set<Integer> accountIds, LocalDate firstDay, LocalDate lastDay) {
        List<BalanceEntity> initialBalanceEntityList = balanceDao.getList(accountIds, firstDay.minusDays(1L));
        List<BalanceEntity> balanceEntityList = balanceDao.getList(accountIds, firstDay, lastDay);
        List<OperationEntity> operationEntityList = operationDao.getEntityList(accountIds, firstDay, lastDay);

        Set<Integer> categoryIds = operationEntityList
                .stream().map(OperationEntity::getCategoryId).collect(Collectors.toSet());

        List<AccountEntity> accountEntityList = accountDao.getList(accountIds);
        Map<Integer, Integer> accountIndexes = new HashMap<>();
        for (int index = 0; index < accountEntityList.size(); index++) {
            accountIndexes.put(accountEntityList.get(index).getId(), index);
        }

        int accuracy = currencyDao.get(accountEntityList.get(0).getCurrencyId()).getAccuracy();

        List<CategoryEntity> categoryEntityList = categoryDao.getList(categoryIds);
        Map<Integer, Integer> categoryIndexes = new HashMap<>();
        for (int index = 0; index < categoryEntityList.size(); index++) {
            categoryIndexes.put(categoryEntityList.get(index).getId(), index);
        }

        BigDecimal prevBalancesSummary = initialBalanceEntityList
                .stream().map(BalanceEntity::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        List<List<BigDecimal>> balances =
                getBalances(initialBalanceEntityList, balanceEntityList, firstDay, lastDay, accountIndexes);
        List<List<BigDecimal>> amounts = getAmounts(operationEntityList, firstDay, lastDay, categoryIndexes);

        List<BigDecimal> totalAmounts =
                Stream.generate(() -> BigDecimal.ZERO).limit(categoryIds.size()).collect(Collectors.toList());
        BigDecimal totalAmountsSummary = BigDecimal.ZERO;

        List<SummaryDto.SummaryDtoRow> summaryDtoRowList = new ArrayList<>();
        LocalDate day = firstDay;
        for (int rowIndex = 0; rowIndex < amounts.size(); rowIndex++) {
            List<BigDecimal> amountsRow = amounts.get(rowIndex);
            List<BigDecimal> balancesRow = balances.get(rowIndex);

            for (int columnIndex = 0; columnIndex < amountsRow.size(); columnIndex++) {
                BigDecimal amount = amountsRow.get(columnIndex);
                totalAmounts.set(columnIndex, totalAmounts.get(columnIndex).add(amount));
                totalAmountsSummary = totalAmountsSummary.add(amount);
            }

            BigDecimal amountsSummary = amountsRow.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
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
        summaryDto.setTotalAmounts(totalAmounts);
        summaryDto.setTotalAmountsSummary(totalAmountsSummary);

        setAccuracy(summaryDto, accuracy);

        return summaryDto;
    }

    /**
     * Transforms list of operations into list of rows between first day and last day. If there are two operations of
     * the same category in the same day, then amounts are summed
     *
     * @param operationEntityList - list of operations between these two days
     * @param firstDay            - first day of amounts
     * @param lastDay             - last day of amounts
     * @param categoryIndexes     - category id to column index map
     * @return amounts matrix
     */
    List<List<BigDecimal>> getAmounts(
            List<OperationEntity> operationEntityList,
            LocalDate firstDay, LocalDate lastDay, Map<Integer, Integer> categoryIndexes) {
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
            List<BigDecimal> resultRow = result.get(y);
            resultRow.set(x, resultRow.get(x).add(operationEntity.getAmount()));
        }

        return result;
    }

    /**
     * Transforms balance entity list to list of rows between first day and last day. When no balance for account was
     * found for this day, then balance from previous day is taken into account. If no balance before a day not found,
     * then it is filled with zero
     *
     * @param initialBalanceEntityList - list of balances just before first day
     * @param balanceEntityList        - list of balances between fist day and last day
     * @param firstDay                 - first day of balances
     * @param lastDay                  - last day of balances
     * @param accountIndexes           - account id to column index map
     * @return balances matrix
     */
    List<List<BigDecimal>> getBalances(
            List<BalanceEntity> initialBalanceEntityList, List<BalanceEntity> balanceEntityList,
            LocalDate firstDay, LocalDate lastDay, Map<Integer, Integer> accountIndexes) {
        int height = (int) (ChronoUnit.DAYS.between(firstDay, lastDay) + 1);
        int width = accountIndexes.size();

        // Preparing initial matrix and filling with balances and nulls where balance does not exist
        List<List<BigDecimal>> result = Stream
                .generate(() -> Stream.generate(() -> (BigDecimal) null).limit(width).collect(Collectors.toList()))
                .limit(height).collect(Collectors.toList());
        for (BalanceEntity balanceEntity : balanceEntityList) {
            result.get((int) ChronoUnit.DAYS.between(firstDay, balanceEntity.getDay()))
                    .set(accountIndexes.get(balanceEntity.getAccountId()), balanceEntity.getAmount());
        }

        // Preparing previous row
        List<BigDecimal> previousRow = Stream.generate(() -> BigDecimal.ZERO).limit(width).collect(Collectors.toList());
        for (BalanceEntity balanceEntity : initialBalanceEntityList) {
            previousRow.set(accountIndexes.get(balanceEntity.getAccountId()), balanceEntity.getAmount());
        }

        // Filling null positions with values from previous row
        for (List<BigDecimal> resultRow : result) {
            for (int index = 0; index < resultRow.size(); index++) {
                if (resultRow.get(index) == null) {
                    resultRow.set(index, previousRow.get(index));
                }
            }
            previousRow = resultRow;
        }

        return result;
    }

    /**
     * Sets accuracy for summary dto
     *
     * @param summaryDto - summary dto
     * @param accuracy   - number or digits after period
     */
    void setAccuracy(SummaryDto summaryDto, int accuracy) {
        for (SummaryDto.SummaryDtoRow row : summaryDto.getSummaryDtoRowList()) {
            row.setDifference(row.getDifference().setScale(accuracy, RoundingMode.HALF_UP));
            row.setBalancesSummary(row.getBalancesSummary().setScale(accuracy, RoundingMode.HALF_UP));
            row.setAmountsSummary(row.getAmountsSummary().setScale(accuracy, RoundingMode.HALF_UP));
            List<BigDecimal> balances = row.getBalances();
            for (int index = 0; index < balances.size(); index++) {
                balances.set(index, balances.get(index).setScale(accuracy, RoundingMode.HALF_UP));
            }
            List<BigDecimal> amounts = row.getAmounts();
            for (int index = 0; index < amounts.size(); index++) {
                amounts.set(index, amounts.get(index).setScale(accuracy, RoundingMode.HALF_UP));
            }
        }

        List<BigDecimal> totalAmounts = summaryDto.getTotalAmounts();
        for (int index = 0; index < totalAmounts.size(); index++) {
            totalAmounts.set(index, totalAmounts.get(index).setScale(accuracy, RoundingMode.HALF_UP));
        }

        summaryDto.setTotalAmountsSummary(summaryDto.getTotalAmountsSummary().setScale(accuracy, RoundingMode.HALF_UP));
    }

}
