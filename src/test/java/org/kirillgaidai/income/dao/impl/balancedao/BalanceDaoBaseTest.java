package org.kirillgaidai.income.dao.impl.balancedao;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.kirillgaidai.income.dao.config.PersistenceTestConfig;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link org.kirillgaidai.income.dao.impl.BalanceDao} base test
 *
 * @author Kirill Gaidai
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = PersistenceTestConfig.class)
public abstract class BalanceDaoBaseTest {

    final protected Integer ACCOUNT_ID_1 = 10;
    final protected Integer ACCOUNT_ID_2 = 11;
    final protected Integer ACCOUNT_ID_3 = 12;
    final protected LocalDate DAY_0 = LocalDate.of(2017, 3, 4);
    final protected LocalDate DAY_1 = LocalDate.of(2017, 3, 5);
    final protected LocalDate DAY_2 = LocalDate.of(2017, 3, 6);
    final protected LocalDate DAY_3 = LocalDate.of(2017, 3, 7);
    final protected List<BalanceEntity> orig = Collections.unmodifiableList(Arrays.asList(
            new BalanceEntity(ACCOUNT_ID_1, DAY_2, new BigDecimal("0.1"), true),
            new BalanceEntity(ACCOUNT_ID_1, DAY_1, new BigDecimal("0.2"), false),
            new BalanceEntity(ACCOUNT_ID_2, DAY_2, new BigDecimal("0.4"), true),
            new BalanceEntity(ACCOUNT_ID_2, DAY_1, new BigDecimal("0.8"), false),
            new BalanceEntity(ACCOUNT_ID_3, DAY_2, new BigDecimal("1.6"), true),
            new BalanceEntity(ACCOUNT_ID_3, DAY_1, new BigDecimal("3.2"), false)
    ));

    @Autowired
    protected IBalanceDao balanceDao;

    @Autowired
    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

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

}
