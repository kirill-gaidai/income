package org.kirillgaidai.income.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kirillgaidai.income.dao.config.PersistenceTestConfig;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
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
import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertCurrencyEntityEquals;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertCurrencyEntityListEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceTestConfig.class)
public class CurrencyDaoTest {

    final private List<CurrencyEntity> orig = Arrays.asList(
            new CurrencyEntity(3, "cc1", "currency1", 4),
            new CurrencyEntity(2, "cc2", "currency2", 2),
            new CurrencyEntity(1, "cc3", "currency3" ,0)
    );

    @Autowired
    private ICurrencyDao currencyDao;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Before
    public void setUp() throws Exception {
        String sql = "INSERT INTO currencies(id, code, title, accuracy) VALUES(:id, :code, :title, :accuracy)";
        for (CurrencyEntity entity : orig) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", entity.getId());
            params.put("code", entity.getCode());
            params.put("title", entity.getTitle());
            params.put("accuracy", entity.getAccuracy());
            namedParameterJdbcTemplate.update(sql, params);
        }
    }

    @After
    public void tearDown() throws Exception {
        namedParameterJdbcTemplate.update("DELETE FROM currencies", Collections.emptyMap());
    }

    @Test
    public void testGetEntityList_All() throws Exception {
        List<CurrencyEntity> expected = Arrays.asList(orig.get(0), orig.get(1), orig.get(2));
        List<CurrencyEntity> actual = currencyDao.getList();
        assertCurrencyEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_AllEmpty() throws Exception {
        namedParameterJdbcTemplate.update("DELETE FROM currencies", Collections.emptyMap());
        List<CurrencyEntity> expected = Collections.emptyList();
        List<CurrencyEntity> actual = currencyDao.getList();
        assertCurrencyEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_Ids() throws Exception {
        List<CurrencyEntity> expected = Arrays.asList(orig.get(0), orig.get(1));
        List<CurrencyEntity> actual = currencyDao.getList(Sets.newSet(3, 2));
        assertCurrencyEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_IdsEmpty() throws Exception {
        List<CurrencyEntity> expected = Collections.emptyList();
        List<CurrencyEntity> actual = currencyDao.getList(Sets.newSet(0));
        assertCurrencyEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntity_Ok() throws Exception {
        CurrencyEntity expected = orig.get(0);
        CurrencyEntity actual = currencyDao.get(3);
        assertCurrencyEntityEquals(expected, actual);
    }

    @Test
    public void testGetEntity_NotFound() throws Exception {
        CurrencyEntity actual = currencyDao.get(0);
        assertNull(actual);
    }

    @Test
    public void testInsertEntity_Ok() throws Exception {
        CurrencyEntity entity = new CurrencyEntity(null, "cc4", "currency4", 2);
        List<CurrencyEntity> expected = Arrays.asList(orig.get(0), orig.get(1), orig.get(2), entity);
        int affectedRows = currencyDao.insert(entity);
        assertEquals(1, affectedRows);
        assertEquals(Integer.valueOf(4), entity.getId());
        List<CurrencyEntity> actual = currencyDao.getList();
        assertCurrencyEntityListEquals(expected, actual);
    }

    @Test
    public void testUpdateEntity_Ok() throws Exception {
        CurrencyEntity entity = new CurrencyEntity(3, "cc4", "currency4", 2);
        List<CurrencyEntity> expected = Arrays.asList(orig.get(1), orig.get(2), entity);
        int affectedRows = currencyDao.update(entity);
        assertEquals(1, affectedRows);
        List<CurrencyEntity> actual = currencyDao.getList();
        assertCurrencyEntityListEquals(expected, actual);
    }

    @Test
    public void testUpdateEntity_NotFound() throws Exception {
        CurrencyEntity entity = new CurrencyEntity(4, "cc4", "currency4", 2);
        int affectedRows = currencyDao.update(entity);
        assertEquals(0, affectedRows);
        List<CurrencyEntity> actual = currencyDao.getList();
        assertCurrencyEntityListEquals(orig, actual);
    }

    @Test
    public void testDeleteEntity_Ok() throws Exception {
        List<CurrencyEntity> expected = Arrays.asList(orig.get(1), orig.get(2));
        int affectedRows = currencyDao.delete(3);
        assertEquals(1, affectedRows);
        List<CurrencyEntity> actual = currencyDao.getList();
        assertCurrencyEntityListEquals(expected, actual);
    }

    @Test
    public void testDeleteEntity_NotFound() throws Exception {
        int affectedRows = currencyDao.delete(0);
        assertEquals(0, affectedRows);
        List<CurrencyEntity> actual = currencyDao.getList();
        assertCurrencyEntityListEquals(orig, actual);
    }

}
