package org.kirillgaidai.income.dao.impl.currencydao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.CurrencyDao#getList()} test
 *
 * @author Kirill Gaidai
 */
public class CurrencyDaoGetListTest extends CurrencyDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        List<CurrencyEntity> expected = Arrays.asList(orig.get(0), orig.get(1), orig.get(2));
        List<CurrencyEntity> actual = currencyDao.getList();
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test empty
     *
     * @throws Exception exception
     */
    @Test
    public void testEmpty() throws Exception {
        deleteCurrencyEntities();
        List<CurrencyEntity> expected = Collections.emptyList();
        List<CurrencyEntity> actual = currencyDao.getList();
        assertEntityListEquals(expected, actual);
    }

}
