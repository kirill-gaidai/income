package org.kirillgaidai.income.dao;

import org.kirillgaidai.income.entity.CategoryEntity;

import java.util.List;

public interface CategoryDao {

    List<CategoryEntity> getCategoryList();

    CategoryEntity getCategoryById(final Integer id);

    int insertCategory(final CategoryEntity categoryEntity);

    int updateCategory(final CategoryEntity categoryEntity);

    int deleteCategory(final Integer id);

}
