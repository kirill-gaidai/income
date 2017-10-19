package org.kirillgaidai.income.service.intf;

import org.kirillgaidai.income.service.dto.ISerialDto;

import java.util.List;
import java.util.Set;

public interface ISerialService<T extends ISerialDto> extends IGenericService<T> {

    List<T> getList(Set<Integer> ids);

    T get(Integer id);

    void delete(Integer id);

}
