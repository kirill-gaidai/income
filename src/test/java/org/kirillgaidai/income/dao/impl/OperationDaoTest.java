package org.kirillgaidai.income.dao.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kirillgaidai.income.dao.config.PersistenceTestConfig;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.dao.intf.IOperationDao;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
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
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertOperationEntityEquals;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertOperationEntityListEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceTestConfig.class)
public class OperationDaoTest {

    final private Integer ACCOUNT_ID_1 = 10;
    final private Integer ACCOUNT_ID_2 = 11;
    final private Integer ACCOUNT_ID_3 = 12;
    final private Integer CATEGORY_ID_1 = 20;
    final private Integer CATEGORY_ID_2 = 21;
    final private LocalDate DAY_0 = LocalDate.of(2017, 3, 4);
    final private LocalDate DAY_1 = LocalDate.of(2017, 3, 5);
    final private LocalDate DAY_2 = LocalDate.of(2017, 3, 6);
    final private LocalDate DAY_3 = LocalDate.of(2017, 3, 7);
    final private List<OperationEntity> orig = Arrays.asList(
            new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_1, DAY_1, new BigDecimal("0.1"), "Note 1"),
            new OperationEntity(2, ACCOUNT_ID_1, CATEGORY_ID_1, DAY_2, new BigDecimal("0.2"), "Note 2"),
            new OperationEntity(3, ACCOUNT_ID_1, CATEGORY_ID_2, DAY_1, new BigDecimal("0.4"), "Note 3"),
            new OperationEntity(4, ACCOUNT_ID_1, CATEGORY_ID_2, DAY_2, new BigDecimal("0.8"), "Note 4"),
            new OperationEntity(5, ACCOUNT_ID_2, CATEGORY_ID_1, DAY_1, new BigDecimal("1.6"), "Note 5"),
            new OperationEntity(6, ACCOUNT_ID_2, CATEGORY_ID_1, DAY_2, new BigDecimal("3.2"), "Note 6"),
            new OperationEntity(7, ACCOUNT_ID_2, CATEGORY_ID_2, DAY_1, new BigDecimal("6.4"), "Note 7"),
            new OperationEntity(8, ACCOUNT_ID_2, CATEGORY_ID_2, DAY_2, new BigDecimal("12.8"), "Note 8"),
            new OperationEntity(9, ACCOUNT_ID_3, CATEGORY_ID_1, DAY_1, new BigDecimal("25.6"), "Note 9"),
            new OperationEntity(10, ACCOUNT_ID_3, CATEGORY_ID_1, DAY_2, new BigDecimal("51.2"), "Note 10"),
            new OperationEntity(11, ACCOUNT_ID_3, CATEGORY_ID_2, DAY_1, new BigDecimal("102.4"), "Note 11"),
            new OperationEntity(12, ACCOUNT_ID_3, CATEGORY_ID_2, DAY_2, new BigDecimal("204.8"), "Note 12")
    );

    @Autowired
    private IOperationDao operationDao;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Before
    public void setUp() throws Exception {
        String sql = "INSERT INTO operations(id, account_id, category_id, day, amount, note) " +
                "VALUES(:id, :account_id, :category_id, :day, :amount, :note)";
        for (OperationEntity entity : orig) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", entity.getId());
            params.put("account_id", entity.getAccountId());
            params.put("category_id", entity.getCategoryId());
            params.put("day", entity.getDay());
            params.put("amount", entity.getAmount());
            params.put("note", entity.getNote());
            namedParameterJdbcTemplate.update(sql, params);
        }
    }

    @After
    public void tearDown() throws Exception {
        namedParameterJdbcTemplate.update("DELETE FROM operations", Collections.emptyMap());
    }

    @Test
    public void testGetEntityList_All() throws Exception {
        List<OperationEntity> actual = operationDao.getList();
        assertOperationEntityListEquals(orig, actual);
    }

    @Test
    public void testGetEntityList_AllEmpty() throws Exception {
        namedParameterJdbcTemplate.update("DELETE FROM operations", Collections.emptyMap());
        List<OperationEntity> expected = Collections.emptyList();
        List<OperationEntity> actual = operationDao.getList();
        assertOperationEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntity_Ids() throws Exception {
        Set<Integer> ids = Sets.newSet(2, 7);
        List<OperationEntity> expected = Arrays.asList(orig.get(1), orig.get(6));
        List<OperationEntity> actual = operationDao.getList(ids);
        assertOperationEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntity_IdsEmpty() throws Exception {
        Set<Integer> ids = Sets.newSet(0, -1);
        List<OperationEntity> expected = Collections.emptyList();
        List<OperationEntity> actual = operationDao.getList(ids);
        assertOperationEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntity_Ok() throws Exception {
        OperationEntity expected = orig.get(0);
        OperationEntity actual = operationDao.get(1);
        assertOperationEntityEquals(expected, actual);
    }

    @Test
    public void testGetEntity_NotFound() throws Exception {
        OperationEntity entity = operationDao.get(0);
        assertNull(entity);
    }

    @Test
    public void testInsertEntity_Ok() throws Exception {
        OperationEntity entity = new OperationEntity(null, ACCOUNT_ID_2, CATEGORY_ID_2, DAY_2, new BigDecimal("409.6"),
                "Note 13");
        int affectedRows = operationDao.insert(entity);
        assertEquals(1, affectedRows);
        List<OperationEntity> expected = new ArrayList<>(orig);
        expected.add(entity);
        List<OperationEntity> actual = operationDao.getList();
        assertOperationEntityListEquals(expected, actual);
    }

    @Test
    public void testUpdateEntity_Ok() throws Exception {
        OperationEntity entity = new OperationEntity(1, ACCOUNT_ID_2, CATEGORY_ID_2, DAY_2, new BigDecimal("409.6"),
                "Note 13");
        OperationEntity expectedEntity = new OperationEntity(1, ACCOUNT_ID_1, CATEGORY_ID_1, DAY_1,
                new BigDecimal("409.6"), "Note 13");
        int affectedRows = operationDao.update(entity);
        assertEquals(1, affectedRows);
        List<OperationEntity> expected = new ArrayList<>();
        expected.add(expectedEntity);
        expected.addAll(orig.subList(1, 12));
        List<OperationEntity> actual = operationDao.getList();
        assertOperationEntityListEquals(expected, actual);
    }

    @Test
    public void testUpdateEntity_NotFound() throws Exception {
        OperationEntity entity = new OperationEntity(0, ACCOUNT_ID_2, CATEGORY_ID_2, DAY_2, new BigDecimal("409.6"),
                "Note 13");
        int affectedRows = operationDao.update(entity);
        assertEquals(0, affectedRows);
        List<OperationEntity> actual = operationDao.getList();
        assertOperationEntityListEquals(orig, actual);
    }

    @Test
    public void testDeleteEntity_Ok() throws Exception {
        int affectedRows = operationDao.delete(1);
        assertEquals(1, affectedRows);
        List<OperationEntity> expected = orig.subList(1, 12);
        List<OperationEntity> actual = operationDao.getList();
        assertOperationEntityListEquals(expected, actual);
    }

    @Test
    public void testDeleteEntity_NotFound() throws Exception {
        int affectedRows = operationDao.delete(0);
        assertEquals(0, affectedRows);
        List<OperationEntity> actual = operationDao.getList();
        assertOperationEntityListEquals(orig, actual);
    }

    @Test
    public void testGetEntityList_AccountIdsForInterval() throws Exception {
        Set<Integer> accountIds = Sets.newSet(ACCOUNT_ID_1, ACCOUNT_ID_2);
        List<OperationEntity> expected = orig.subList(0, 8);
        List<OperationEntity> actual = operationDao.getEntityList(accountIds, DAY_0, DAY_3);
        assertOperationEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_AccountIdsForIntervalEmpty() throws Exception {
        Set<Integer> accountIds = Collections.emptySet();
        List<OperationEntity> expected = Collections.emptyList();
        List<OperationEntity> actual = operationDao.getEntityList(accountIds, DAY_0, DAY_3);
        assertOperationEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_AccountIdsForDay() throws Exception {
        Set<Integer> accountIds = Sets.newSet(ACCOUNT_ID_1, ACCOUNT_ID_2);
        List<OperationEntity> expected = Arrays.asList(orig.get(0), orig.get(2), orig.get(4), orig.get(6));
        List<OperationEntity> actual = operationDao.getEntityList(accountIds, DAY_1);
        assertOperationEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_AccountIdsForDayEmpty() throws Exception {
        Set<Integer> accountIds = Collections.emptySet();
        List<OperationEntity> expected = Collections.emptyList();
        List<OperationEntity> actual = operationDao.getEntityList(accountIds, DAY_1);
        assertOperationEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_AccountIdsForDayCategory() throws Exception {
        Set<Integer> accountIds = Sets.newSet(ACCOUNT_ID_1, ACCOUNT_ID_2);
        List<OperationEntity> expected = Arrays.asList(orig.get(0), orig.get(4));
        List<OperationEntity> actual = operationDao.getEntityList(accountIds, DAY_1, CATEGORY_ID_1);
        assertOperationEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_AccountIdsEmptyForDayCategoryEmpty() throws Exception {
        Set<Integer> accountIds = Collections.emptySet();
        List<OperationEntity> expected = Collections.emptyList();
        List<OperationEntity> actual = operationDao.getEntityList(accountIds, DAY_1, CATEGORY_ID_1);
        assertOperationEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_AccountIdsForDayCategoryEmpty() throws Exception {
        Set<Integer> accountIds = Sets.newSet(ACCOUNT_ID_1, ACCOUNT_ID_2);
        List<OperationEntity> expected = Collections.emptyList();
        List<OperationEntity> actual = operationDao.getEntityList(accountIds, DAY_0, CATEGORY_ID_1);
        assertOperationEntityListEquals(expected, actual);
    }

}
