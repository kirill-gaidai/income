package org.kirillgaidai.income.dao.impl.currencydao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.entity.IGenericEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.CurrencyDao#update(IGenericEntity)} test
 *
 * @author Kirill Gaidai
 */
public class CurrencyDaoUpdateTest extends CurrencyDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        CurrencyEntity entity = new CurrencyEntity(3, "cc4", "currency4", 2);
        List<CurrencyEntity> expected = Arrays.asList(orig.get(1), orig.get(2), entity);
        int affectedRows = currencyDao.update(entity);
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
        CurrencyEntity entity = new CurrencyEntity(4, "cc4", "currency4", 2);
        int affectedRows = currencyDao.update(entity);
        assertEquals(0, affectedRows);
        List<CurrencyEntity> actual = currencyDao.getList();
        assertEntityListEquals(orig, actual);
    }

}
