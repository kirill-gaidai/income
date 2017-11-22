package org.kirillgaidai.income.dao.impl.balancedao;

import junit.framework.AssertionFailedError;
import org.junit.Test;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.entity.IGenericEntity;
import org.springframework.dao.DuplicateKeyException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;
import static org.kirillgaidai.income.utils.TestUtils.throwUnreachableException;

/**
 * {@link org.kirillgaidai.income.dao.impl.BalanceDao#insert(IGenericEntity)} test
 *
 * @author Kirill Gaidai
 */
public class BalanceDaoInsertTest extends BalanceDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        BalanceEntity entity = new BalanceEntity(ACCOUNT_ID_1, DAY_3, new BigDecimal("0.8"), true);
        List<BalanceEntity> expected = new ArrayList<>(orig);
        expected.add(0, entity);
        int affectedRows = balanceDao.insert(entity);
        assertEquals(1, affectedRows);
        List<BalanceEntity> actual = balanceDao.getList();
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test duplicate
     *
     * @throws Exception exception
     */
    @Test
    public void testDuplicate() throws Exception {
        BalanceEntity entity = new BalanceEntity(ACCOUNT_ID_1, DAY_2, new BigDecimal("0.8"), true);
        try {
            balanceDao.insert(entity);
            throwUnreachableException();
        } catch (DuplicateKeyException e) {
            List<BalanceEntity> actual = balanceDao.getList();
            assertEntityListEquals(orig, actual);
        }
    }

}
