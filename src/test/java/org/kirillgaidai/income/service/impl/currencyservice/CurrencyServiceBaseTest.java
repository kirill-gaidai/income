package org.kirillgaidai.income.service.impl.currencyservice;

import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.service.converter.CurrencyConverter;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.CurrencyDto;
import org.kirillgaidai.income.service.impl.CurrencyService;
import org.kirillgaidai.income.service.impl.ServiceBaseTest;
import org.kirillgaidai.income.service.intf.ICurrencyService;

public class CurrencyServiceBaseTest extends ServiceBaseTest {

    final protected IGenericConverter<CurrencyEntity, CurrencyDto> converter = new CurrencyConverter();
    final protected ICurrencyService service = new CurrencyService(currencyDao, serviceHelper, converter);

}
