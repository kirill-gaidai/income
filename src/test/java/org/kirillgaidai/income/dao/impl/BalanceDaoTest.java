package org.kirillgaidai.income.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kirillgaidai.income.dao.config.PersistenceTestConfig;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertBalanceEntityEquals;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertBalanceEntityListEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceTestConfig.class)
public class BalanceDaoTest {

    final private Integer ACCOUNT_ID_1 = 10;
    final private Integer ACCOUNT_ID_2 = 11;
    final private Integer ACCOUNT_ID_3 = 12;
    final private LocalDate DAY_0 = LocalDate.of(2017, 3, 4);
    final private LocalDate DAY_1 = LocalDate.of(2017, 3, 5);
    final private LocalDate DAY_2 = LocalDate.of(2017, 3, 6);
    final private LocalDate DAY_3 = LocalDate.of(2017, 3, 7);
    final private List<BalanceEntity> orig = Arrays.asList(
            new BalanceEntity(ACCOUNT_ID_1, DAY_2, new BigDecimal("0.1"), true),
            new BalanceEntity(ACCOUNT_ID_1, DAY_1, new BigDecimal("0.2"), false),
            new BalanceEntity(ACCOUNT_ID_2, DAY_2, new BigDecimal("0.4"), true),
            new BalanceEntity(ACCOUNT_ID_2, DAY_1, new BigDecimal("0.8"), false),
            new BalanceEntity(ACCOUNT_ID_3, DAY_2, new BigDecimal("1.6"), true),
            new BalanceEntity(ACCOUNT_ID_3, DAY_1, new BigDecimal("3.2"), false)
    );

    @Autowired
    private IBalanceDao balanceDao;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Before
    public void setUp() throws Exception {
        String sql = "INSERT INTO balances(account_id, day, amount, manual) " +
                "VALUES(:account_id, :day, :amount, :manual)";
        for (BalanceEntity entity : orig) {
            Map<String, Object> params = new HashMap<>();
            params.put("account_id", entity.getAccountId());
            params.put("day", Date.valueOf(entity.getDay()));
            params.put("amount", entity.getAmount());
            params.put("manual", entity.getManual());
            namedParameterJdbcTemplate.update(sql, params);
        }
    }

    @After
    public void tearDown() throws Exception {
        namedParameterJdbcTemplate.update("DELETE FROM balances", Collections.emptyMap());
    }

    @Test
    public void testGetEntityList_All() throws Exception {
        List<BalanceEntity> actual = balanceDao.getEntityList();
        assertBalanceEntityListEquals(orig, actual);
    }

    @Test
    public void testGetEntityList_AllEmpty() throws Exception {
        namedParameterJdbcTemplate.update("DELETE FROM balances", Collections.emptyMap());
        List<BalanceEntity> expected = Collections.emptyList();
        List<BalanceEntity> actual = balanceDao.getEntityList();
        assertBalanceEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_AccountIdInterval() throws Exception {
        List<BalanceEntity> expected = Arrays.asList(orig.get(0), orig.get(1));
        List<BalanceEntity> actual = balanceDao.getEntityList(ACCOUNT_ID_1, DAY_0, DAY_3);
        assertBalanceEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_AccountIdIntervalEmpty() throws Exception {
        List<BalanceEntity> expected = Collections.emptyList();
        List<BalanceEntity> actual = balanceDao.getEntityList(ACCOUNT_ID_1, DAY_3, DAY_3);
        assertBalanceEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_AccountIdsInterval() throws Exception {
        List<BalanceEntity> expected = Arrays.asList(orig.get(0), orig.get(2));
        Set<Integer> accountIds = Sets.newSet(ACCOUNT_ID_1, ACCOUNT_ID_2);
        List<BalanceEntity> actual = balanceDao.getEntityList(accountIds, DAY_2, DAY_3);
        assertBalanceEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_AccountIdsEmptyIntervalEmpty() throws Exception {
        List<BalanceEntity> expected = Collections.emptyList();
        Set<Integer> accountIds = Collections.emptySet();
        List<BalanceEntity> actual = balanceDao.getEntityList(accountIds, DAY_0, DAY_3);
        assertBalanceEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_AccountIdsIntervalEmpty() throws Exception {
        List<BalanceEntity> expected = Collections.emptyList();
        Set<Integer> accountIds = Sets.newSet(ACCOUNT_ID_1, ACCOUNT_ID_2);
        List<BalanceEntity> actual = balanceDao.getEntityList(accountIds, DAY_3, DAY_3);
        assertBalanceEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntity_Ok() throws Exception {
        BalanceEntity expected = orig.get(1);
        BalanceEntity actual = balanceDao.getEntity(ACCOUNT_ID_1, DAY_1);
        assertBalanceEntityEquals(expected, actual);
    }

    @Test
    public void testGetEntity_NotFound() throws Exception {
        BalanceEntity actual = balanceDao.getEntity(ACCOUNT_ID_1, DAY_0);
        assertNull(actual);
    }

    @Test
    public void testInsertEntity_Ok() throws Exception {
        BalanceEntity entity = new BalanceEntity(ACCOUNT_ID_1, DAY_3, new BigDecimal("0.8"), true);
        List<BalanceEntity> expected = Arrays.asList(entity, orig.get(0), orig.get(1));
        int affectedRows = balanceDao.insertEntity(entity);
        assertEquals(1, affectedRows);
        List<BalanceEntity> actual = balanceDao.getEntityList(ACCOUNT_ID_1, DAY_1, DAY_3);
        assertBalanceEntityListEquals(expected, actual);
    }

    @Test
    public void testUpdateEntity_Ok() throws Exception {
        BalanceEntity entity = new BalanceEntity(ACCOUNT_ID_1, DAY_2, new BigDecimal("0.8"), false);
        List<BalanceEntity> expected = new ArrayList<>();
        expected.add(entity);
        expected.addAll(orig.subList(1, orig.size()));
        int affectedRows = balanceDao.updateEntity(entity);
        assertEquals(1, affectedRows);
        List<BalanceEntity> actual = balanceDao.getEntityList();
        assertBalanceEntityListEquals(expected, actual);
    }

    @Test
    public void testUpdateEntity_NotFound() throws Exception {
        BalanceEntity entity = new BalanceEntity(ACCOUNT_ID_1, DAY_0, new BigDecimal("0.8"), false);
        int affectedRows = balanceDao.updateEntity(entity);
        assertEquals(0, affectedRows);
        List<BalanceEntity> actual = balanceDao.getEntityList();
        assertBalanceEntityListEquals(orig, actual);
    }

    @Test
    public void testDeleteEntity_Ok() throws Exception {
        List<BalanceEntity> expected = orig.subList(1, orig.size());
        int affectedRows = balanceDao.deleteEntity(ACCOUNT_ID_1, DAY_2);
        assertEquals(1, affectedRows);
        List<BalanceEntity> actual = balanceDao.getEntityList();
        assertBalanceEntityListEquals(expected, actual);
    }

    @Test
    public void testDeleteEntity_NotFound() throws Exception {
        int affectedRows = balanceDao.deleteEntity(ACCOUNT_ID_1, DAY_0);
        assertEquals(0, affectedRows);
        List<BalanceEntity> actual = balanceDao.getEntityList();
        assertBalanceEntityListEquals(orig, actual);
    }

}
