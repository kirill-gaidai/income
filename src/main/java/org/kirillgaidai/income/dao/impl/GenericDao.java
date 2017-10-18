package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.entity.IGenericEntity;
import org.kirillgaidai.income.dao.intf.IGenericDao;
import org.kirillgaidai.income.dao.util.DaoHelper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public abstract class GenericDao<T extends IGenericEntity> implements IGenericDao<T> {

    final protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    final protected DaoHelper daoHelper;
    final protected RowMapper<T> rowMapper;

    public GenericDao(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate, DaoHelper daoHelper, RowMapper<T> rowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.daoHelper = daoHelper;
        this.rowMapper = rowMapper;
    }

}
