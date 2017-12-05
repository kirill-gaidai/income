package org.kirillgaidai.income.dao.impl.accountdao;

import org.junit.Before;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.dao.impl.DaoBaseTest;
import org.kirillgaidai.income.dao.intf.IAccountDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

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
        orig.forEach(this::insertAccountEntity);
    }

}
