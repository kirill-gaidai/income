package org.kirillgaidai.income.dao.impl.operationdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.IGenericEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.OperationDao#update(IGenericEntity)} test
 *
 * @author Kirill Gaidai
 */
public class OperationDaoUpdateTest extends OperationDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        OperationEntity entity = new OperationEntity(1, ACCOUNT_ID_2, CATEGORY_ID_2, DAY_2, new BigDecimal("409.6"),
                "Note 13");
        OperationEntity expectedEntity = new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_1, DAY_1,
                new BigDecimal("409.6"), "Note 13");
        int affectedRows = operationDao.update(entity);
        assertEquals(1, affectedRows);
        List<OperationEntity> expected = new ArrayList<>(orig);
        expected.set(0, expectedEntity);
        List<OperationEntity> actual = operationDao.getList();
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        OperationEntity entity = new OperationEntity(0, ACCOUNT_ID_2, CATEGORY_ID_2, DAY_2, new BigDecimal("409.6"),
                "Note 13");
        int affectedRows = operationDao.update(entity);
        assertEquals(0, affectedRows);
        List<OperationEntity> actual = operationDao.getList();
        assertEntityListEquals(orig, actual);
    }

}
