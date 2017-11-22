package org.kirillgaidai.income.service.intf;

import org.kirillgaidai.income.service.dto.IGenericDto;

import java.util.List;

/**
 * Service generic interface for {@link org.kirillgaidai.income.dao.entity.IGenericEntity}
 *
 * @param <T> dto class
 * @author Kirill Gaidai
 */
public interface IGenericService<T extends IGenericDto> {

    /**
     * Gets list of dto
     *
     * @return list of dto
     */
    List<T> getList();

    /**
     * Creates dto. Returns created dto
     *
     * @param dto dto
     * @return created dto
     */
    T create(T dto);

    /**
     * Updates dto. Returns updated dto
     *
     * @param dto dto
     * @return updated dto
     */
    T update(T dto);

    /**
     * Creates or updates dto. Returns created or updated dto
     *
     * @param dto dto
     * @return created or updated dto
     */
    T save(T dto);

}
