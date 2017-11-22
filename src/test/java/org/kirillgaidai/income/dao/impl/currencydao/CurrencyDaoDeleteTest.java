package org.kirillgaidai.income.dao.impl.currencydao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.CurrencyDao#delete(Integer)} test
 *
 * @author Kirill Gaidai
 */
public class CurrencyDaoDeleteTest extends CurrencyDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        List<CurrencyEntity> expected = Arrays.asList(orig.get(1), orig.get(2));
        int affectedRows = currencyDao.delete(3);
        assertEquals(1, affectedRows);
        List<CurrencyEntity> actual = currencyDao.getList();
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        int affectedRows = currencyDao.delete(0);
        assertEquals(0, affectedRows);
        List<CurrencyEntity> actual = currencyDao.getList();
        assertEntityListEquals(orig, actual);
    }

}
