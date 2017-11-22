package org.kirillgaidai.income.dao.impl.operationdao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.OperationEntity;

import java.util.Collections;
import java.util.List;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.OperationDao#getList()} test
 *
 * @author Kirill Gaidai
 */
public class OperationDaoGetListTest extends OperationDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        List<OperationEntity> actual = operationDao.getList();
        assertEntityListEquals(orig, actual);
    }

    /**
     * Test empty
     *
     * @throws Exception exception
     */
    @Test
    public void testEmpty() throws Exception {
        namedParameterJdbcTemplate.update("DELETE FROM operations", Collections.emptyMap());
        List<OperationEntity> expected = Collections.emptyList();
        List<OperationEntity> actual = operationDao.getList();
        assertEntityListEquals(expected, actual);
    }

}
