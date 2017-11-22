package org.kirillgaidai.income.dao.impl.operationdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.ISerialEntity;
import org.kirillgaidai.income.dao.entity.OperationEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.OperationDao#insert(ISerialEntity)} test
 *
 * @author Kirill Gaidai
 */
public class OperationDaoInsertTest extends OperationDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testInsertEntity_Ok() throws Exception {
        OperationEntity entity = new OperationEntity(null, ACCOUNT_ID_2, CATEGORY_ID_2, DAY_2, new BigDecimal("409.6"),
                "Note 13");
        int affectedRows = operationDao.insert(entity);
        assertEquals(1, affectedRows);
        List<OperationEntity> expected = new ArrayList<>(orig);
        expected.add(entity);
        List<OperationEntity> actual = operationDao.getList();
        assertEntityListEquals(expected, actual);
    }

}
