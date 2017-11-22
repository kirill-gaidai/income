package org.kirillgaidai.income.dao.impl.currencydao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.CurrencyDao#getList(Set)} test
 *
 * @author Kirill Gaidai
 */
public class CurrencyDaoGetListByIdsTest extends CurrencyDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Set<Integer> ids = Sets.newSet(3, 2);
        List<CurrencyEntity> expected = Arrays.asList(orig.get(0), orig.get(1));
        List<CurrencyEntity> actual = currencyDao.getList(ids);
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test ids empty
     *
     * @throws Exception exception
     */
    @Test
    public void testIdsEmpty() throws Exception {
        Set<Integer> ids = Collections.emptySet();
        List<CurrencyEntity> expected = Collections.emptyList();
        List<CurrencyEntity> actual = currencyDao.getList(ids);
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test empty
     *
     * @throws Exception exception
     */
    @Test
    public void testEmpty() throws Exception {
        Set<Integer> ids = Sets.newSet(0, -1);
        List<CurrencyEntity> expected = Collections.emptyList();
        List<CurrencyEntity> actual = currencyDao.getList(ids);
        assertEntityListEquals(expected, actual);
    }

}
