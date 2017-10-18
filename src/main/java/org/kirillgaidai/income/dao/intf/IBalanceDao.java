package org.kirillgaidai.income.dao.intf;

import org.kirillgaidai.income.dao.entity.BalanceEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface IBalanceDao extends IGenericDao<BalanceEntity> {

    List<BalanceEntity> getList(Set<Integer> accountIds, LocalDate lastDay);

    List<BalanceEntity> getList(Set<Integer> accountIds, LocalDate firstDay, LocalDate lastDay);

    BalanceEntity get(Integer accountId, LocalDate day);

    BalanceEntity getEntityBefore(Integer accountId, LocalDate day);

    BalanceEntity getEntityAfter(Integer accountId, LocalDate day);

    int delete(Integer accountId, LocalDate day);

}
