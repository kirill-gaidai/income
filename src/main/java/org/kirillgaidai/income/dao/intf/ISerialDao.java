package org.kirillgaidai.income.dao.intf;

import org.kirillgaidai.income.dao.entity.ISerialEntity;

import java.util.List;
import java.util.Set;

public interface ISerialDao<T extends ISerialEntity> extends IGenericDao<T> {

    /**
     * Returns entities with specified ids from DB
     *
     * @param ids - entity ids set
     * @return entity list
     */
    List<T> getEntityList(Set<Integer> ids);

    /**
     * Returns entity with specified id from DB
     *
     * @param id - entity id
     * @return entity
     */
    T getEntity(Integer id);

    /**
     * Deletes entity from DB by its id
     *
     * @param id - entity id
     * @return number of deleted rows
     */
    int deleteEntity(Integer id);

}
