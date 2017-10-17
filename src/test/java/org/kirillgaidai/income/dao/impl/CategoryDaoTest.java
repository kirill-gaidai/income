package org.kirillgaidai.income.dao.impl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.config.PersistenceTestConfig;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertCategoryEntityEquals;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertCategoryEntityListEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceTestConfig.class)
public class CategoryDaoTest {

    final private List<CategoryEntity> orig = Arrays.asList(
            new CategoryEntity(3, "01", "category1"),
            new CategoryEntity(2, "02", "category2"),
            new CategoryEntity(1, "03", "category3")
    );

    @Autowired
    private ICategoryDao categoryDao;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Before
    public void setUp() throws Exception {
        String sql = "INSERT INTO categories(id, sort, title) VALUES(:id, :sort, :title)";
        for (CategoryEntity entity : orig) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", entity.getId());
            params.put("sort", entity.getSort());
            params.put("title", entity.getTitle());
            namedParameterJdbcTemplate.update(sql, params);
        }
    }

    @After
    public void tearDown() throws Exception {
        namedParameterJdbcTemplate.update("DELETE FROM categories", Collections.emptyMap());
    }

    @Test
    public void testGetEntityList_All() throws Exception {
        List<CategoryEntity> expected = Arrays.asList(orig.get(0), orig.get(1), orig.get(2));
        List<CategoryEntity> actual = categoryDao.getEntityList();
        assertCategoryEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_AllEmpty() throws Exception {
        namedParameterJdbcTemplate.update("DELETE FROM categories", Collections.emptyMap());
        List<CategoryEntity> expected = Collections.emptyList();
        List<CategoryEntity> actual = categoryDao.getEntityList();
        assertCategoryEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_Ids() throws Exception {
        List<CategoryEntity> expected = Arrays.asList(orig.get(0), orig.get(1));
        List<CategoryEntity> actual = categoryDao.getEntityList(Sets.newSet(3, 2));
        assertCategoryEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_IdsEmpty() throws Exception {
        List<CategoryEntity> expected = Collections.emptyList();
        List<CategoryEntity> actual = categoryDao.getEntityList(Sets.newSet(0));
        assertCategoryEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntity_Ok() throws Exception {
        CategoryEntity expected = orig.get(0);
        CategoryEntity actual = categoryDao.getEntity(3);
        assertCategoryEntityEquals(expected, actual);
    }

    @Test
    public void testGetEntity_NotFound() throws Exception {
        CategoryEntity actual = categoryDao.getEntity(0);
        Assert.assertNull(actual);
    }

    @Test
    public void testInsertEntity_Ok() throws Exception {
        CategoryEntity entity = new CategoryEntity(null, "04", "category4");
        List<CategoryEntity> expected = Arrays.asList(orig.get(0), orig.get(1), orig.get(2), entity);
        int affectedRows = categoryDao.insertEntity(entity);
        assertEquals(1, affectedRows);
        assertEquals(Integer.valueOf(4), entity.getId());
        List<CategoryEntity> actual = categoryDao.getEntityList();
        assertCategoryEntityListEquals(expected, actual);
    }

    @Test
    public void testUpdateEntity_Ok() throws Exception {
        CategoryEntity entity = new CategoryEntity(3, "04", "category4");
        List<CategoryEntity> expected = Arrays.asList(orig.get(1), orig.get(2), entity);
        int affectedRows = categoryDao.updateEntity(entity);
        assertEquals(1, affectedRows);
        List<CategoryEntity> actual = categoryDao.getEntityList();
        assertCategoryEntityListEquals(expected, actual);
    }

    @Test
    public void testUpdateEntity_NotFound() throws Exception {
        CategoryEntity entity = new CategoryEntity(4, "04", "category4");
        int affectedRows = categoryDao.updateEntity(entity);
        assertEquals(0, affectedRows);
        List<CategoryEntity> actual = categoryDao.getEntityList();
        assertCategoryEntityListEquals(orig, actual);
    }

    @Test
    public void testDeleteEntity_Ok() throws Exception {
        List<CategoryEntity> expected = Arrays.asList(orig.get(1), orig.get(2));
        int affectedRows = categoryDao.deleteEntity(3);
        assertEquals(1, affectedRows);
        List<CategoryEntity> actual = categoryDao.getEntityList();
        assertCategoryEntityListEquals(expected, actual);
    }

    @Test
    public void testDeleteEntity_NotFound() throws Exception {
        int affectedRows = categoryDao.deleteEntity(0);
        assertEquals(0, affectedRows);
        List<CategoryEntity> actual = categoryDao.getEntityList();
        assertCategoryEntityListEquals(orig, actual);
    }

}
