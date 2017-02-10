package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.CategoryDao;
import org.kirillgaidai.income.entity.CategoryEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryDaoImpl implements CategoryDao {

    @Override
    public List<CategoryEntity> getCategoryList() {
        final List<CategoryEntity> result = new ArrayList<>();

        final CategoryEntity categoryEntity1 = new CategoryEntity();
        categoryEntity1.setId(1L);
        categoryEntity1.setTitle("Category1");
        result.add(categoryEntity1);

        final CategoryEntity categoryEntity2 = new CategoryEntity();
        categoryEntity2.setId(2L);
        categoryEntity2.setTitle("Category2");
        result.add(categoryEntity2);

        return result;
    }

    @Override
    public CategoryEntity getCategoryById(final Long id) {
        final CategoryEntity result = new CategoryEntity();
        result.setId(1L);
        result.setTitle("Category1");
        return result;
    }

}
