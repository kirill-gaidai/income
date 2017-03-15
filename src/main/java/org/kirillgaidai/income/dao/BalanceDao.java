package org.kirillgaidai.income.dao;

import org.kirillgaidai.income.entity.BalanceEntity;

import java.util.Date;

public interface BalanceDao {

    BalanceEntity getEntityBeforeDay(final int accountId, final Date day);

    BalanceEntity getEntityAfterDay(final int accountId, final Date day);

    int insertEntity(final BalanceEntity entity);

    int updateEntity(final BalanceEntity entity);

    int deleteEntity(final int accountId, final Date day);

}
