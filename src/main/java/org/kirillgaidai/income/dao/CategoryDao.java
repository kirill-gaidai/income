package org.kirillgaidai.income.dao;

import org.kirillgaidai.income.entity.CategoryEntity;

import java.util.List;

public interface CategoryDao {

    List<CategoryEntity> getCategoryList();

    CategoryEntity getCategoryById(final Long id);

}
