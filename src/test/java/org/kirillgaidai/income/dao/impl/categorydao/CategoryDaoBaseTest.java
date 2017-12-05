package org.kirillgaidai.income.dao.impl.categorydao;

import org.junit.After;
import org.junit.Before;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.dao.impl.DaoBaseTest;
import org.kirillgaidai.income.dao.intf.ICategoryDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        String sql = "INSERT INTO categories(id, sort, title) VALUES(:id, :sort, :title)";
        for (CategoryEntity entity : orig) {
            Map<String, Object> params = new HashMap<>();
            params.put("id", entity.getId());
            params.put("sort", entity.getSort());
            params.put("title", entity.getTitle());
            namedParameterJdbcTemplate.update(sql, params);
        }
    }

    @After
    public void tearDown() throws Exception {
        namedParameterJdbcTemplate.update("DELETE FROM categories", Collections.emptyMap());
    }

}
