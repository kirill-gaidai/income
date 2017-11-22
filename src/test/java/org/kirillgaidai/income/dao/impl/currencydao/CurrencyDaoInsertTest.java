package org.kirillgaidai.income.dao.impl.currencydao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.entity.ISerialEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.CurrencyDao#insert(ISerialEntity)} test
 *
 * @author Kirill Gaidai
 */
public class CurrencyDaoInsertTest extends CurrencyDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        CurrencyEntity entity = new CurrencyEntity(null, "cc4", "currency4", 2);
        List<CurrencyEntity> expected = Arrays.asList(orig.get(0), orig.get(1), orig.get(2), entity);
        int affectedRows = currencyDao.insert(entity);
        assertEquals(1, affectedRows);
        assertEquals(Integer.valueOf(4), entity.getId());
        List<CurrencyEntity> actual = currencyDao.getList();
        assertEntityListEquals(expected, actual);
    }

}
