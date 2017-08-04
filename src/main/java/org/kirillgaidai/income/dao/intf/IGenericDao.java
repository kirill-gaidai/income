package org.kirillgaidai.income.dao.intf;

import org.kirillgaidai.income.dao.entity.IGenericEntity;

import java.util.List;
import java.util.Set;

/**
 * Defines basic operations for DAO with entities implementing {@link IGenericEntity}
 *
 * @param <T> - generic entity class
 */
public interface IGenericDao<T extends IGenericEntity> {

    /**
     * Returns all entities from DB table
     *
     * @return entity list
     */
    List<T> getEntityList();

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
     * Creates new entity in DB and sets its id
     *
     * @param entity - new entity
     * @return number of inserted rows
     */
    int insertEntity(T entity);

    /**
     * Updates entity with specified in DB
     *
     * @param entity - entity
     * @return number of updated rows
     */
    int updateEntity(T entity);

    /**
     * Deletes entity from DB by its id
     *
     * @param id - entity id
     * @return number of deleted rows
     */
    int deleteEntity(Integer id);

}
