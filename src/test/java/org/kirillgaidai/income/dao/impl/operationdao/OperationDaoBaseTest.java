package org.kirillgaidai.income.dao.impl.operationdao;

import org.junit.Before;
import org.kirillgaidai.income.dao.entity.OperationEntity;
import org.kirillgaidai.income.dao.impl.DaoBaseTest;
import org.kirillgaidai.income.dao.intf.IOperationDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        orig.forEach(this::insertOperationEntity);
    }

}
