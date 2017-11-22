package org.kirillgaidai.income.dao.impl.ratedao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kirillgaidai.income.dao.config.PersistenceTestConfig;
import org.kirillgaidai.income.dao.entity.RateEntity;
import org.kirillgaidai.income.dao.intf.IRateDao;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertRateEntityEquals;
import static org.kirillgaidai.income.dao.utils.PersistenceTestUtils.assertRateEntityListEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceTestConfig.class)
public class RateDaoTest {

    final private Integer CURRENCY_ID_1 = 10;
    final private Integer CURRENCY_ID_2 = 11;
    final private Integer CURRENCY_ID_3 = 12;
    final private LocalDate DAY_0 = LocalDate.of(2017, 8, 1);
    final private LocalDate DAY_1 = LocalDate.of(2017, 8, 2);
    final private LocalDate DAY_2 = LocalDate.of(2017, 8, 3);
    final private LocalDate DAY_4 = LocalDate.of(2017, 8, 4);
    final private List<RateEntity> orig = Arrays.asList(
            new RateEntity(CURRENCY_ID_2, CURRENCY_ID_1, DAY_1, new BigDecimal("1.21")),
            new RateEntity(CURRENCY_ID_2, CURRENCY_ID_1, DAY_2, new BigDecimal("1.22")),
            new RateEntity(CURRENCY_ID_3, CURRENCY_ID_1, null, new BigDecimal("1.23"))
    );

    @Autowired
    private IRateDao rateDao;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Before
    public void setUp() throws Exception {
        String sql = "INSERT INTO rates(currency_id_from, currency_id_to, day, value) " +
                "VALUES(:currency_id_from, :currency_id_to, :day, :value)";
        for (RateEntity rateEntity : orig) {
            Map<String, Object> params = new HashMap<>();
            params.put("currency_id_from", rateEntity.getCurrencyIdFrom());
            params.put("currency_id_to", rateEntity.getCurrencyIdTo());
            params.put("day", rateEntity.getDay());
            params.put("value", rateEntity.getValue());
            namedParameterJdbcTemplate.update(sql, params);
        }
    }

    @After
    public void tearDown() throws Exception {
        namedParameterJdbcTemplate.update("DELETE FROM rates", Collections.emptyMap());
    }

    @Test
    public void testGetEntityList_All() throws Exception {
        List<RateEntity> actual = rateDao.getEntityList();
        assertRateEntityListEquals(orig, actual);
    }

    @Test
    public void testGetEntityList_AllEmpty() throws Exception {
        namedParameterJdbcTemplate.update("DELETE FROM rates", Collections.emptyMap());
        List<RateEntity> expected = Collections.emptyList();
        List<RateEntity> actual = rateDao.getEntityList();
        assertRateEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_DailyIntervalOk() throws Exception {
        List<RateEntity> expected = Arrays.asList(orig.get(0), orig.get(1));
        List<RateEntity> actual = rateDao.getEntityList(CURRENCY_ID_2, CURRENCY_ID_1, DAY_0, DAY_4);
        assertRateEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntityList_DailyIntervalEmpty() throws Exception {
        List<RateEntity> expected = Collections.emptyList();
        List<RateEntity> actual = rateDao.getEntityList(CURRENCY_ID_1, CURRENCY_ID_2, DAY_0, DAY_4);
        assertRateEntityListEquals(expected, actual);
    }

    @Test
    public void testGetEntity_ByCurrencyIdsWithDayOk() throws Exception {
        RateEntity expected = new RateEntity(CURRENCY_ID_2, CURRENCY_ID_1, DAY_1, new BigDecimal("1.21"));
        RateEntity actual = rateDao.getEntity(CURRENCY_ID_2, CURRENCY_ID_1, DAY_1);
        assertRateEntityEquals(expected, actual);
    }

    @Test
    public void testGetEntity_ByCurrencyIdsWithDayNotFound() throws Exception {
        RateEntity actual = rateDao.getEntity(CURRENCY_ID_1, CURRENCY_ID_2, DAY_1);
        assertNull(actual);
    }

    @Test
    public void testGetEntity_ByCurrencyIdsWithoutDayOk() throws Exception {
        RateEntity expected = new RateEntity(CURRENCY_ID_3, CURRENCY_ID_1, null, new BigDecimal("1.23"));
        RateEntity actual = rateDao.getEntity(CURRENCY_ID_3, CURRENCY_ID_1, null);
        assertRateEntityEquals(expected, actual);
    }

    @Test
    public void testGetEntity_ByCurrencyIdsWithoutDayNotFound() throws Exception {
        RateEntity actual = rateDao.getEntity(CURRENCY_ID_1, CURRENCY_ID_2, null);
        assertNull(actual);
    }

    @Test
    public void testInsertEntity_OkWithDay() throws Exception {
        RateEntity entity = new RateEntity(CURRENCY_ID_1, CURRENCY_ID_2, DAY_1, new BigDecimal("1.20"));
        List<RateEntity> expected = new ArrayList<>(orig);
        expected.add(entity);
        int affectedRows = rateDao.insertEntity(entity);
        assertEquals(1, affectedRows);
        List<RateEntity> actual = rateDao.getEntityList();
        assertRateEntityListEquals(expected, actual);
    }

    @Test
    public void testInsertEntity_OkWithoutDay() throws Exception {
        RateEntity entity = new RateEntity(CURRENCY_ID_1, CURRENCY_ID_2, null, new BigDecimal("1.20"));
        List<RateEntity> expected = new ArrayList<>(orig);
        expected.add(entity);
        int affectedRows = rateDao.insertEntity(entity);
        assertEquals(1, affectedRows);
        List<RateEntity> actual = rateDao.getEntityList();
        assertRateEntityListEquals(expected, actual);
    }

    @Test
    public void testInsertEntity_FailureWithDay() throws Exception {
        RateEntity entity = new RateEntity(CURRENCY_ID_2, CURRENCY_ID_1, DAY_1, new BigDecimal("1.20"));
        String message = null;
        try {
            rateDao.insertEntity(entity);
        } catch (RuntimeException e) {
            message = e.getMessage();
        }
        assertEquals("Rate entity already exists", message);
    }

    @Test
    public void testInsertEntity_FailureWithoutDay() throws Exception {
        RateEntity entity = new RateEntity(CURRENCY_ID_3, CURRENCY_ID_1, null, new BigDecimal("1.20"));
        String message = null;
        try {
            rateDao.insertEntity(entity);
        } catch (RuntimeException e) {
            message = e.getMessage();
        }
        assertEquals("Rate entity already exists", message);
    }

    @Test
    public void testUpdateEntity_OkWithDate() throws Exception {
        RateEntity entity = new RateEntity(CURRENCY_ID_2, CURRENCY_ID_1, DAY_1, new BigDecimal("1.20"));
        List<RateEntity> expected = new ArrayList<>(orig);
        expected.set(0, entity);
        int affectedRows = rateDao.updateEntity(entity);
        assertEquals(1, affectedRows);
        List<RateEntity> actual = rateDao.getEntityList();
        assertRateEntityListEquals(expected, actual);
    }

    @Test
    public void testUpdateEntity_OkWithoutDate() throws Exception {
        RateEntity entity = new RateEntity(CURRENCY_ID_3, CURRENCY_ID_1, null, new BigDecimal("1.20"));
        List<RateEntity> expected = new ArrayList<>(orig);
        expected.set(2, entity);
        int affectedRows = rateDao.updateEntity(entity);
        assertEquals(1, affectedRows);
        List<RateEntity> actual = rateDao.getEntityList();
        assertRateEntityListEquals(expected, actual);
    }

    @Test
    public void testUpdateEntity_NotFoundWithDate() throws Exception {
        RateEntity entity = new RateEntity(CURRENCY_ID_3, CURRENCY_ID_1, DAY_0, new BigDecimal("1.20"));
        int affectedRows = rateDao.updateEntity(entity);
        assertEquals(0, affectedRows);
        List<RateEntity> actual = rateDao.getEntityList();
        assertRateEntityListEquals(orig, actual);
    }

    @Test
    public void testUpdateEntity_NotFoundWithoutDate() throws Exception {
        RateEntity entity = new RateEntity(CURRENCY_ID_2, CURRENCY_ID_1, null, new BigDecimal("1.20"));
        int affectedRows = rateDao.updateEntity(entity);
        assertEquals(0, affectedRows);
        List<RateEntity> actual = rateDao.getEntityList();
        assertRateEntityListEquals(orig, actual);
    }

    @Test
    public void testDeleteEntity_OkWithDate() throws Exception {
        int affectedRows = rateDao.deleteEntity(CURRENCY_ID_2, CURRENCY_ID_1, DAY_1);
        assertEquals(1, affectedRows);
        List<RateEntity> expected = Arrays.asList(orig.get(1), orig.get(2));
        List<RateEntity> actual = rateDao.getEntityList();
        assertRateEntityListEquals(expected, actual);
    }

    @Test
    public void testDeleteEntity_OkWithoutDate() throws Exception {
        int affectedRows = rateDao.deleteEntity(CURRENCY_ID_3, CURRENCY_ID_1, null);
        assertEquals(1, affectedRows);
        List<RateEntity> expected = Arrays.asList(orig.get(0), orig.get(1));
        List<RateEntity> actual = rateDao.getEntityList();
        assertRateEntityListEquals(expected, actual);
    }

    @Test
    public void testDeleteEntity_NotFoundWithDate() throws Exception {
        int affectedRows = rateDao.deleteEntity(CURRENCY_ID_3, CURRENCY_ID_1, DAY_1);
        assertEquals(0, affectedRows);
        List<RateEntity> actual = rateDao.getEntityList();
        assertRateEntityListEquals(orig, actual);
    }

    @Test
    public void testDeleteEntity_NotFoundWithoutDate() throws Exception {
        int affectedRows = rateDao.deleteEntity(CURRENCY_ID_2, CURRENCY_ID_1, null);
        assertEquals(0, affectedRows);
        List<RateEntity> actual = rateDao.getEntityList();
        assertRateEntityListEquals(orig, actual);
    }

}
