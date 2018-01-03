package org.kirillgaidai.income.dao.impl.userdao;

import org.junit.Before;
import org.kirillgaidai.income.dao.entity.UserEntity;
import org.kirillgaidai.income.dao.impl.DaoBaseTest;
import org.kirillgaidai.income.dao.intf.IUserDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class UserDaoBaseTest extends DaoBaseTest {

    final protected List<UserEntity> orig = Collections.unmodifiableList(Arrays.asList(
            new UserEntity(1, "admin", "s3cr3t!", true, false, "token1", LocalDateTime.of(2018, 1, 2, 8, 45, 30)),
            new UserEntity(2, "blocked", "pass", false, true, "token2", LocalDateTime.of(2018, 1, 2, 9, 45, 30)),
            new UserEntity(3, "user", "password", false, false, "token3", LocalDateTime.of(2018, 1, 2, 10, 45, 30))
    ));

    @Autowired
    protected IUserDao userDao;

    @Before
    public void setUp() throws Exception {
        orig.forEach(this::insertUserEntity);
    }

}
