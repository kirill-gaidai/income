package org.kirillgaidai.income.dao.intf;

import org.kirillgaidai.income.dao.entity.OperationEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface IOperationDao extends IGenericDao<OperationEntity> {

    List<OperationEntity> getEntityList(Set<Integer> accountIds, LocalDate firstDay, LocalDate lastDay);

    List<OperationEntity> getEntityList(Set<Integer> accountIds, LocalDate day);

    List<OperationEntity> getEntityList(Set<Integer> accountIds, LocalDate day, Integer categoryId);

}
