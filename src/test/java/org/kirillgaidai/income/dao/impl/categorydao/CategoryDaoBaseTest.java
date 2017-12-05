package org.kirillgaidai.income.dao.impl.categorydao;

import org.junit.Before;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.impl.DaoBaseTest;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * {@link org.kirillgaidai.income.dao.impl.CategoryDao} base test
 *
 * @author Kirill Gaidai
 */
public abstract class CategoryDaoBaseTest extends DaoBaseTest {

    final protected List<CategoryEntity> orig = Collections.unmodifiableList(Arrays.asList(
            new CategoryEntity(3, "01", "category1"),
            new CategoryEntity(2, "02", "category2"),
            new CategoryEntity(1, "03", "category3")
    ));

    @Autowired
    protected ICategoryDao categoryDao;

    @Before
    public void setUp() throws Exception {
        orig.forEach(this::insertCategoryEntity);
    }

}
