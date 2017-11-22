package org.kirillgaidai.income.service.impl.operationsservice;

import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.kirillgaidai.income.dao.intf.IOperationDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.converter.OperationConverter;
import org.kirillgaidai.income.service.dto.OperationDto;
import org.kirillgaidai.income.service.impl.OperationService;
import org.kirillgaidai.income.service.impl.ServiceBaseTest;
import org.kirillgaidai.income.service.intf.IOperationService;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public abstract class OperationServiceBaseTest extends ServiceBaseTest {

    final protected IGenericConverter<OperationEntity, OperationDto> converter = new OperationConverter();
    final protected IOperationService service =
            new OperationService(accountDao, operationDao, balanceDao, categoryDao, converter);

}
