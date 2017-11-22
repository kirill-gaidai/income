package org.kirillgaidai.income.dao.impl.categorydao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.CategoryDao#getList()} test
 *
 * @author Kirill Gaidai
 */
public class CategoryDaoGetListTest extends CategoryDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        List<CategoryEntity> expected = Arrays.asList(orig.get(0), orig.get(1), orig.get(2));
        List<CategoryEntity> actual = categoryDao.getList();
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test empty
     *
     * @throws Exception exception
     */
    @Test
    public void testEmpty() throws Exception {
        namedParameterJdbcTemplate.update("DELETE FROM categories", Collections.emptyMap());
        List<CategoryEntity> expected = Collections.emptyList();
        List<CategoryEntity> actual = categoryDao.getList();
        assertEntityListEquals(expected, actual);
    }

}
