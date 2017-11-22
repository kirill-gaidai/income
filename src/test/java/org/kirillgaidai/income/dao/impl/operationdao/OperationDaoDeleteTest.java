package org.kirillgaidai.income.dao.impl.operationdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.OperationEntity;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.OperationDao#delete(Integer)} test
 *
 * @author Kirill Gaidai
 */
public class OperationDaoDeleteTest extends OperationDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testDeleteEntity_Ok() throws Exception {
        int affectedRows = operationDao.delete(1);
        assertEquals(1, affectedRows);
        List<OperationEntity> expected = orig.subList(1, 12);
        List<OperationEntity> actual = operationDao.getList();
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testDeleteEntity_NotFound() throws Exception {
        int affectedRows = operationDao.delete(0);
        assertEquals(0, affectedRows);
        List<OperationEntity> actual = operationDao.getList();
        assertEntityListEquals(orig, actual);
    }

}
