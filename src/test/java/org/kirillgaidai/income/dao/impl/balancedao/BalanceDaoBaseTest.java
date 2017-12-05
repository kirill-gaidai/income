package org.kirillgaidai.income.dao.impl.balancedao;

import org.junit.Before;
import org.kirillgaidai.income.dao.entity.BalanceEntity;
import org.kirillgaidai.income.dao.impl.DaoBaseTest;
import org.kirillgaidai.income.dao.intf.IBalanceDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * {@link org.kirillgaidai.income.dao.impl.BalanceDao} base test
 *
 * @author Kirill Gaidai
 */
public abstract class BalanceDaoBaseTest extends DaoBaseTest {

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

    @Before
    public void setUp() throws Exception {
        orig.forEach(this::insertBalanceEntity);
    }

}
