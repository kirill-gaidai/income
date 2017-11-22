package org.kirillgaidai.income.dao.impl.currencydao;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.kirillgaidai.income.dao.config.PersistenceTestConfig;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceTestConfig.class)
public abstract class CurrencyDaoBaseTest {

    final protected List<CurrencyEntity> orig = Arrays.asList(
            new CurrencyEntity(3, "cc1", "currency1", 4),
            new CurrencyEntity(2, "cc2", "currency2", 2),
            new CurrencyEntity(1, "cc3", "currency3", 0)
    );

    @Autowired
    protected ICurrencyDao currencyDao;

    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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

}
