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
     * @param entity new entity
     * @return number of inserted rows
     */
    int insert(T entity);

    /**
     * Updates entity with specified in DB
     *
     * @param entity entity
     * @return number of updated rows
     */
    int update(T entity);

    /**
     * Updates entity with specified if it matches old entity
     *
     * @param newEntity new entity
     * @param oldEntity old entity
     * @return number of updated rows
     */
    int update(T newEntity, T oldEntity);

    /**
     * Deletes entity if it matches specified entity
     *
     * @param entity old entity
     * @return number of deleted rows
     */
    int delete(T entity);

}
