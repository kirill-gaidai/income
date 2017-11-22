package org.kirillgaidai.income.service.impl.balanceservice;

import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.service.converter.BalanceConverter;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.BalanceDto;
import org.kirillgaidai.income.service.impl.BalanceService;
import org.kirillgaidai.income.service.impl.ServiceBaseTest;
import org.kirillgaidai.income.service.intf.IBalanceService;

public abstract class BalanceServiceBaseTest extends ServiceBaseTest {

    final protected IGenericConverter<BalanceEntity, BalanceDto> converter = new BalanceConverter();
    final protected IBalanceService service = new BalanceService(balanceDao, accountDao, converter);

}
