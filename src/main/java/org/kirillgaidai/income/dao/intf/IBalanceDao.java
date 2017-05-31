package org.kirillgaidai.income.dao.intf;

import org.kirillgaidai.income.dao.entity.BalanceEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface IBalanceDao {

    List<BalanceEntity> getEntityList();

    List<BalanceEntity> getEntityList(Integer accountId, LocalDate firstDay, LocalDate lastDay);

    List<BalanceEntity> getEntityList(Set<Integer> accountIds, LocalDate firstDay, LocalDate lastDay);

    BalanceEntity getEntity(Integer accountId, LocalDate day);

    int insertEntity(BalanceEntity entity);

    int updateEntity(BalanceEntity entity);

    int deleteEntity(Integer accountId, LocalDate day);

}
