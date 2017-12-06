package org.kirillgaidai.income.dao.impl.currencydao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

public class CurrencyDaoDeleteOptimisticTest extends CurrencyDaoBaseTest {

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        CurrencyEntity entity = new CurrencyEntity(0, "cc1", "currency1", 4);
        int affectedRows = currencyDao.delete(entity);
        assertEquals(0, affectedRows);
        List<CurrencyEntity> actual = currencyDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test sort changed
     *
     * @throws Exception exception
     */
    @Test
    public void testSortChanged() throws Exception {
        CurrencyEntity entity = new CurrencyEntity(3, "cc0", "currency1", 4);
        int affectedRows = currencyDao.delete(entity);
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
        CurrencyEntity entity = new CurrencyEntity(3, "cc1", "currency0", 4);
        int affectedRows = currencyDao.delete(entity);
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
        CurrencyEntity entity = new CurrencyEntity(3, "cc1", "currency1", 1);
        int affectedRows = currencyDao.delete(entity);
        assertEquals(0, affectedRows);
        List<CurrencyEntity> actual = currencyDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test dependent account exist
     *
     * @throws Exception exception
     */
    @Test
    public void testDependentAccountExist() throws Exception {
        AccountEntity accountEntity = new AccountEntity(1, 3, "01", "account1");
        insertAccountEntity(accountEntity);
        CurrencyEntity entity = new CurrencyEntity(3, "cc1", "currency1", 4);
        int affectedRows = currencyDao.delete(entity);
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
        CurrencyEntity entity = new CurrencyEntity(3, "cc1", "currency1", 4);
        int affectedRows = currencyDao.delete(entity);
        assertEquals(1, affectedRows);
        List<CurrencyEntity> expected = Arrays.asList(orig.get(1), orig.get(2));
        List<CurrencyEntity> actual = currencyDao.getList();
        assertEntityListEquals(expected, actual);
    }

}
