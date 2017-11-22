package org.kirillgaidai.income.service.intf;

import org.kirillgaidai.income.service.dto.ISerialDto;

import java.util.List;
import java.util.Set;

/**
 * Generic interface for {@link org.kirillgaidai.income.dao.entity.ISerialEntity}
 *
 * @param <T> dto class
 * @author Kirill Gaidai
 */
public interface ISerialService<T extends ISerialDto> extends IGenericService<T> {

    /**
     * Gets dto list by ids
     *
     * @param ids ids
     * @return dto list
     */
    List<T> getList(Set<Integer> ids);

    /**
     * Gets dto by id
     *
     * @param id id
     * @return dto
     */
    T get(Integer id);

    /**
     * Deletes dto by id
     *
     * @param id id
     */
    void delete(Integer id);

}
