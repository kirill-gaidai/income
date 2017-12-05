package org.kirillgaidai.income.dao.impl.accountdao;

import org.junit.After;
import org.junit.Before;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.impl.DaoBaseTest;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link org.kirillgaidai.income.dao.impl.AccountDao} base test
 *
 * @author Kirill Gaidai
 */
public abstract class AccountDaoBaseTest extends DaoBaseTest {

    final protected List<AccountEntity> orig = Arrays.asList(
            new AccountEntity(3, 5, "01", "account1"),
            new AccountEntity(2, 6, "02", "account2"),
            new AccountEntity(1, 7, "03", "account3")
    );

    @Autowired
    protected IAccountDao accountDao;

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

}
