package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.BalanceDao;
import org.kirillgaidai.income.entity.BalanceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class BalanceDaoImpl implements BalanceDao {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public BalanceDaoImpl(final NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public BalanceEntity getEntityBeforeDay(final int accountId, final Date day) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BalanceEntity getEntityAfterDay(final int accountId, final Date day) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int insertEntity(final BalanceEntity entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int updateEntity(final BalanceEntity entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int deleteEntity(final int accountId, final Date day) {
        throw new UnsupportedOperationException();
    }

}
