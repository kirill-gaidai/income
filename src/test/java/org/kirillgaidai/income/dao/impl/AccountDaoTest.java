package org.kirillgaidai.income.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kirillgaidai.income.dao.config.PersistenceTestConfig;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.intf.IAccountDao;
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
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertAccountEntityEquals;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertAccountEntityListEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceTestConfig.class)
public class AccountDaoTest {

    final private List<AccountEntity> orig = Arrays.asList(
            new AccountEntity(3, 5, "01", "account1"),
            new AccountEntity(2, 6, "02", "account2"),
            new AccountEntity(1, 7, "03", "account3")
    );

    @Autowired
    private IAccountDao accountDao;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Before
    public void setUp() throws Exception {
        String sql = "INSERT INTO accounts(id, currency_id, sort, title) VALUES(:id, :currency_id, :sort, :title)";
        for (AccountEntity entity : orig) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", entity.getId());
            params.put("currency_id", entity.getCurrencyId());
            params.put("sort", entity.getSort());
            params.put("title", entity.getTitle());
            namedParameterJdbcTemplate.update(sql, params);
        }
    }

    @After
    public void tearDown() throws Exception {
        namedParameterJdbcTemplate.update("DELETE FROM accounts", Collections.emptyMap());
    }

    @Test
    public void testGetEntityList_All() throws Exception {
        List<AccountEntity> expected = Arrays.asList(orig.get(0), orig.get(1), orig.get(2));
        List<AccountEntity> actual = accountDao.getEntityList();
        assertAccountEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_AllEmpty() throws Exception {
        namedParameterJdbcTemplate.update("DELETE FROM accounts", Collections.emptyMap());
        List<AccountEntity> expected = Collections.emptyList();
        List<AccountEntity> actual = accountDao.getEntityList();
        assertAccountEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_Ids() throws Exception {
        List<AccountEntity> expected = Arrays.asList(orig.get(0), orig.get(1));
        List<AccountEntity> actual = accountDao.getEntityList(Sets.newSet(3, 2));
        assertAccountEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_IdsEmpty() throws Exception {
        List<AccountEntity> expected = Collections.emptyList();
        List<AccountEntity> actual = accountDao.getEntityList(Sets.newSet(0));
        assertAccountEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntity_Ok() throws Exception {
        AccountEntity expected = orig.get(0);
        AccountEntity actual = accountDao.getEntity(3);
        assertAccountEntityEquals(expected, actual);
    }

    @Test
    public void testGetEntity_NotFound() throws Exception {
        AccountEntity actual = accountDao.getEntity(0);
        assertNull(actual);
    }

    @Test
    public void testInsertEntity_Ok() throws Exception {
        AccountEntity entity = new AccountEntity(null, 8, "04", "account4");
        List<AccountEntity> expected = Arrays.asList(orig.get(0), orig.get(1), orig.get(2), entity);
        int affectedRows = accountDao.insertEntity(entity);
        assertEquals(1, affectedRows);
        assertEquals(Integer.valueOf(4), entity.getId());
        List<AccountEntity> actual = accountDao.getEntityList();
        assertAccountEntityListEquals(expected, actual);
    }

    @Test
    public void testUpdateEntity_Ok() throws Exception {
        AccountEntity entity = new AccountEntity(3, 8, "04", "account4");
        List<AccountEntity> expected = Arrays.asList(orig.get(1), orig.get(2), entity);
        int affectedRows = accountDao.updateEntity(entity);
        assertEquals(1, affectedRows);
        List<AccountEntity> actual = accountDao.getEntityList();
        assertAccountEntityListEquals(expected, actual);
    }

    @Test
    public void testUpdateEntity_NotFound() throws Exception {
        AccountEntity entity = new AccountEntity(4, 8, "04", "account4");
        int affectedRows = accountDao.updateEntity(entity);
        assertEquals(0, affectedRows);
        List<AccountEntity> actual = accountDao.getEntityList();
        assertAccountEntityListEquals(orig, actual);
    }

    @Test
    public void testDeleteEntity_Ok() throws Exception {
        List<AccountEntity> expected = Arrays.asList(orig.get(1), orig.get(2));
        int affectedRows = accountDao.deleteEntity(3);
        assertEquals(1, affectedRows);
        List<AccountEntity> actual = accountDao.getEntityList();
        assertAccountEntityListEquals(expected, actual);
    }

    @Test
    public void testDeleteEntity_NotFound() throws Exception {
        int affectedRows = accountDao.deleteEntity(0);
        assertEquals(0, affectedRows);
        List<AccountEntity> actual = accountDao.getEntityList();
        assertAccountEntityListEquals(orig, actual);
    }

}
