package org.kirillgaidai.income.dao.impl.currencydao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

public class CurrencyDaoUpdateOptimisticTest extends CurrencyDaoBaseTest {

    @Test
    public void testSuccessful() throws Exception {
        CurrencyEntity newEntity = new CurrencyEntity(3, "cc4", "currency4", 2);
        CurrencyEntity oldEntity = orig.get(0);
        List<CurrencyEntity> expected = Arrays.asList(orig.get(1), orig.get(2), newEntity);
        int affectedRows = currencyDao.update(newEntity, oldEntity);
        assertEquals(1, affectedRows);
        List<CurrencyEntity> actual = currencyDao.getList();
        assertEntityListEquals(expected, actual);
    }

    @Test
    public void testNotFound() throws Exception {
        CurrencyEntity newEntity = new CurrencyEntity(0, "cc4", "currency4", 2);
        CurrencyEntity oldEntity = new CurrencyEntity(0, "cc1", "currency1", 2);
        int affectedRows = currencyDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<CurrencyEntity> actual = currencyDao.getList();
        assertEntityListEquals(orig, actual);
    }

    @Test
    public void testAlreadyModofied() throws Exception {
        CurrencyEntity newEntity = new CurrencyEntity(3, "cc1", "currency1", 2);
        CurrencyEntity oldEntity = new CurrencyEntity(3, "cc1", "currency1", 3);
        int affectedRows = currencyDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<CurrencyEntity> actual = currencyDao.getList();
        assertEntityListEquals(orig, actual);
    }

}
