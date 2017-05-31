package org.kirillgaidai.income.dao.intf;

import org.kirillgaidai.income.dao.entity.IGenericEntity;

import java.util.List;
import java.util.Set;

public interface IGenericDao<T extends IGenericEntity> {

    List<T> getEntityList();

    List<T> getEntityList(Set<Integer> ids);

    T getEntity(Integer id);

    int insertEntity(T entity);

    int updateEntity(T entity);

    int deleteEntity(Integer id);

}
