package org.kirillgaidai.income.dao.intf;

import org.kirillgaidai.income.dao.entity.OperationEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface IOperationDao extends ISerialDao<OperationEntity> {

    List<OperationEntity> getList(
            Set<Integer> accountIds, Set<Integer> categoryIds, LocalDate firstDay, LocalDate lastDay);

    int getCountByAccountId(Integer accountId);

    int getCountByCategoryId(Integer categoryId);

}
