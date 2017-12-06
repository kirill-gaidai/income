package org.kirillgaidai.income.dao.impl.currencydao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

public class CurrencyDaoUpdateOptimisticTest extends CurrencyDaoBaseTest {

    /**
     * Test id changed
     *
     * @throws Exception exception
     */
    @Test
    public void testIdChanged() throws Exception {
        CurrencyEntity newEntity = new CurrencyEntity(3, "cc0", "currency0", 1);
        CurrencyEntity oldEntity = new CurrencyEntity(0, "cc1", "currency1", 4);
        int affectedRows = currencyDao.update(oldEntity, newEntity);
        assertEquals(0, affectedRows);
    }

    /**
     * Test code changed
     *
     * @throws Exception exception
     */
    @Test
    public void testCodeChanged() throws Exception {
        CurrencyEntity newEntity = new CurrencyEntity(3, "cc0", "currency0", 1);
        CurrencyEntity oldEntity = new CurrencyEntity(3, "cc0", "currency1", 4);
        int affectedRows = currencyDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<CurrencyEntity> actual = currencyDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test title changed
     *
     * @throws Exception exception
     */
    @Test
    public void testTitleChanged() throws Exception {
        CurrencyEntity newEntity = new CurrencyEntity(3, "cc0", "currency0", 1);
        CurrencyEntity oldEntity = new CurrencyEntity(3, "cc1", "currency0", 4);
        int affectedRows = currencyDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<CurrencyEntity> actual = currencyDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test accuracy changed
     *
     * @throws Exception exception
     */
    @Test
    public void testAccuracyChanged() throws Exception {
        CurrencyEntity newEntity = new CurrencyEntity(3, "cc0", "currency0", 1);
        CurrencyEntity oldEntity = new CurrencyEntity(3, "cc1", "currency1", 3);
        int affectedRows = currencyDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<CurrencyEntity> actual = currencyDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        CurrencyEntity newEntity = new CurrencyEntity(0, "cc0", "currency0", 1);
        CurrencyEntity oldEntity = new CurrencyEntity(3, "cc1", "currency1", 4);
        int affectedRows = currencyDao.update(newEntity, oldEntity);
        assertEquals(0, affectedRows);
        List<CurrencyEntity> actual = currencyDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        CurrencyEntity newEntity = new CurrencyEntity(3, "cc0", "currency0", 1);
        CurrencyEntity oldEntity = new CurrencyEntity(3, "cc1", "currency1", 4);
        int affectedRows = currencyDao.update(newEntity, oldEntity);
        assertEquals(1, affectedRows);
        List<CurrencyEntity> expected = Arrays.asList(newEntity, orig.get(1), orig.get(2));
        List<CurrencyEntity> actual = currencyDao.getList();
        assertEntityListEquals(expected, actual);
    }

}
