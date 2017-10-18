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
    List<T> getList();

    /**
     * Creates new entity in DB
     *
     * @param entity - new entity
     * @return number of inserted rows
     */
    int insert(T entity);

    /**
     * Updates entity with specified in DB
     *
     * @param entity - entity
     * @return number of updated rows
     */
    int update(T entity);

}
