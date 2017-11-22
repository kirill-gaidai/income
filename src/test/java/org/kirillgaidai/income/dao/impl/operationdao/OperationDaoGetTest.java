package org.kirillgaidai.income.dao.impl.operationdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.OperationEntity;

import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.OperationDao#get(Integer)} test
 *
 * @author Kirill Gaidai
 */
public class OperationDaoGetTest extends OperationDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        OperationEntity expected = orig.get(0);
        OperationEntity actual = operationDao.get(1);
        assertEntityEquals(expected, actual);
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        OperationEntity entity = operationDao.get(0);
        assertNull(entity);
    }

}
