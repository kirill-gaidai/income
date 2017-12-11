package org.kirillgaidai.income.rest.mappers;

import org.kirillgaidai.income.rest.dto.SummaryRestDto;
import org.kirillgaidai.income.rest.dto.account.AccountCreateRestDto;
import org.kirillgaidai.income.rest.dto.account.AccountGetRestDto;
import org.kirillgaidai.income.rest.dto.account.AccountUpdateRestDto;
import org.kirillgaidai.income.rest.dto.balance.BalanceGetRestDto;
import org.kirillgaidai.income.rest.dto.balance.BalanceUpdateRestDto;
import org.kirillgaidai.income.rest.dto.category.CategoryCreateRestDto;
import org.kirillgaidai.income.rest.dto.category.CategoryGetRestDto;
import org.kirillgaidai.income.rest.dto.category.CategoryUpdateRestDto;
import org.kirillgaidai.income.rest.dto.currency.CurrencyCreateRestDto;
import org.kirillgaidai.income.rest.dto.currency.CurrencyGetRestDto;
import org.kirillgaidai.income.rest.dto.currency.CurrencyUpdateRestDto;
import org.kirillgaidai.income.rest.dto.operation.OperationCreateRestDto;
import org.kirillgaidai.income.rest.dto.operation.OperationGetRestDto;
import org.kirillgaidai.income.rest.dto.operation.OperationUpdateRestDto;
import org.kirillgaidai.income.service.dto.AccountDto;
import org.kirillgaidai.income.service.dto.BalanceDto;
import org.kirillgaidai.income.service.dto.CategoryDto;
import org.kirillgaidai.income.service.dto.CurrencyDto;
import org.kirillgaidai.income.service.dto.OperationDto;
import org.kirillgaidai.income.service.dto.SummaryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SummaryRestDtoMapper {

    final private IGenericRestDtoMapper<AccountGetRestDto, AccountCreateRestDto, AccountUpdateRestDto, AccountDto>
            accountMapper;
    final private IGenericRestDtoMapper<BalanceGetRestDto, BalanceUpdateRestDto, BalanceUpdateRestDto, BalanceDto>
            balanceMapper;
    final private IGenericRestDtoMapper<CategoryGetRestDto, CategoryCreateRestDto, CategoryUpdateRestDto, CategoryDto>
            categoryMapper;
    final private IGenericRestDtoMapper<CurrencyGetRestDto, CurrencyCreateRestDto, CurrencyUpdateRestDto, CurrencyDto>
            currencyMapper;
    final private IGenericRestDtoMapper<OperationGetRestDto, OperationCreateRestDto, OperationUpdateRestDto,
            OperationDto> operationMapper;

    @Autowired
    public SummaryRestDtoMapper(
            IGenericRestDtoMapper<AccountGetRestDto, AccountCreateRestDto, AccountUpdateRestDto, AccountDto>
                    accountMapper,
            IGenericRestDtoMapper<BalanceGetRestDto, BalanceUpdateRestDto, BalanceUpdateRestDto, BalanceDto>
                    balanceMapper,
            IGenericRestDtoMapper<CategoryGetRestDto, CategoryCreateRestDto, CategoryUpdateRestDto, CategoryDto>
                    categoryMapper,
            IGenericRestDtoMapper<CurrencyGetRestDto, CurrencyCreateRestDto, CurrencyUpdateRestDto, CurrencyDto>
                    currencyMapper,
            IGenericRestDtoMapper<OperationGetRestDto, OperationCreateRestDto, OperationUpdateRestDto, OperationDto>
                    operationMapper) {
        this.accountMapper = accountMapper;
        this.balanceMapper = balanceMapper;
        this.categoryMapper = categoryMapper;
        this.currencyMapper = currencyMapper;
        this.operationMapper = operationMapper;
    }

    public SummaryRestDto toRestDto(SummaryDto dto) {
        return new SummaryRestDto(
                dto.getFirstDay(),
                dto.getLastDay(),
                currencyMapper.toRestDto(dto.getCurrencyDto()),
                dto.getAccountDtoList().stream().map(accountMapper::toRestDto).collect(Collectors.toList()),
                dto.getCategoryDtoList().stream().map(categoryMapper::toRestDto).collect(Collectors.toList()),
                dto.getInitialBalanceDtoList().stream().map(balanceMapper::toRestDto).collect(Collectors.toList()),
                dto.getBalanceDtoList().stream().map(balanceMapper::toRestDto).collect(Collectors.toList()),
                dto.getOperationDtoList().stream().map(operationMapper::toRestDto).collect(Collectors.toList()));
    }

}
