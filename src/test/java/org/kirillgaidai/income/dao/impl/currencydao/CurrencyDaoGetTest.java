package org.kirillgaidai.income.dao.impl.currencydao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;

import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.CurrencyDao#get(Integer)} test
 *
 * @author Kirill Gaidai
 */
public class CurrencyDaoGetTest extends CurrencyDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        CurrencyEntity expected = orig.get(0);
        CurrencyEntity actual = currencyDao.get(3);
        assertEntityEquals(expected, actual);
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        CurrencyEntity actual = currencyDao.get(0);
        assertNull(actual);
    }

}
