package org.kirillgaidai.income.dao.intf;

import org.kirillgaidai.income.dao.entity.OperationEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface IOperationDao extends ISerialDao<OperationEntity> {

    List<OperationEntity> getList(Set<Integer> accountIds, LocalDate firstDay, LocalDate lastDay);

    List<OperationEntity> getList(Set<Integer> accountIds, LocalDate day);

    List<OperationEntity> getList(Set<Integer> accountIds, LocalDate day, Integer categoryId);

    int getCountByAccountId(Integer accountId);

    int getCountByCategoryId(Integer categoryId);

}
