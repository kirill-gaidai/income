package org.kirillgaidai.income.dao.impl.categorydao;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;

/**
 * {@link org.kirillgaidai.income.dao.impl.CategoryDao#getList(Set)} test
 *
 * @author Kirill Gaidai
 */
public class CategoryDaoGetListByIdsTest extends CategoryDaoBaseTest {

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Set<Integer> ids = Sets.newSet(3, 2);
        List<CategoryEntity> expected = Arrays.asList(orig.get(0), orig.get(1));
        List<CategoryEntity> actual = categoryDao.getList(ids);
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test empty
     *
     * @throws Exception exception
     */
    @Test
    public void testEmpty() throws Exception {
        Set<Integer> ids = Collections.emptySet();
        List<CategoryEntity> expected = Collections.emptyList();
        List<CategoryEntity> actual = categoryDao.getList(ids);
        assertEntityListEquals(expected, actual);
    }

    /**
     * Test ids empty
     *
     * @throws Exception exception
     */
    @Test
    public void testIdsEmpty() throws Exception {
        Set<Integer> ids = Sets.newSet(0, -1);
        List<CategoryEntity> expected = Collections.emptyList();
        List<CategoryEntity> actual = categoryDao.getList(ids);
        assertEntityListEquals(expected, actual);
    }

}
