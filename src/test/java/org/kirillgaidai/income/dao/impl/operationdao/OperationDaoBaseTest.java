package org.kirillgaidai.income.dao.impl.operationdao;

import org.junit.After;
import org.junit.Before;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.dao.impl.DaoBaseTest;
import org.kirillgaidai.income.dao.intf.IOperationDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link org.kirillgaidai.income.dao.impl.OperationDao} base test
 *
 * @author Kirill Gaidai
 */
public abstract class OperationDaoBaseTest extends DaoBaseTest {

    final protected Integer ACCOUNT_ID_1 = 10;
    final protected Integer ACCOUNT_ID_2 = 11;
    final protected Integer ACCOUNT_ID_3 = 12;
    final protected Integer ACCOUNT_ID_4 = 13;
    final protected Integer CATEGORY_ID_1 = 20;
    final protected Integer CATEGORY_ID_2 = 21;
    final protected Integer CATEGORY_ID_3 = 22;
    final protected LocalDate DAY_0 = LocalDate.of(2017, 3, 4);
    final protected LocalDate DAY_1 = LocalDate.of(2017, 3, 5);
    final protected LocalDate DAY_2 = LocalDate.of(2017, 3, 6);
    final protected LocalDate DAY_3 = LocalDate.of(2017, 3, 7);
    final protected List<OperationEntity> orig = Collections.unmodifiableList(Arrays.asList(
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
    ));

    @Autowired
    protected IOperationDao operationDao;

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

}
