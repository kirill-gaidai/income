package org.kirillgaidai.income.dao;

import org.kirillgaidai.income.entity.BalanceEntity;

import java.util.Date;

public interface BalanceDao {

    BalanceEntity getEntityByIds(final Integer accountId, final Date day);

    BalanceEntity getPreviousEntityByIds(Integer accountId, Date day);

    int insertEntity(final BalanceEntity balanceEntity);

    int updateEntity(final BalanceEntity balanceEntity);

    int deleteEntity(final Integer accountId, final Date day);

}
