package org.kirillgaidai.income.dao.impl.currencydao;

import org.junit.Before;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.impl.DaoBaseTest;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public abstract class CurrencyDaoBaseTest extends DaoBaseTest {

    final protected List<CurrencyEntity> orig = Arrays.asList(
            new CurrencyEntity(3, "cc1", "currency1", 4),
            new CurrencyEntity(2, "cc2", "currency2", 2),
            new CurrencyEntity(1, "cc3", "currency3", 0)
    );

    @Autowired
    protected ICurrencyDao currencyDao;

    @Before
    public void setUp() throws Exception {
        orig.forEach(this::insertCurrencyEntity);
    }

}
