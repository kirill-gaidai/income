package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.entity.ISerialEntity;
import org.kirillgaidai.income.dao.intf.ISerialDao;
import org.kirillgaidai.income.dao.util.DaoHelper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public abstract class SerialDao<T extends ISerialEntity> extends GenericDao<T> implements ISerialDao<T> {

    public SerialDao(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate, DaoHelper daoHelper, RowMapper<T> rowMapper) {
        super(namedParameterJdbcTemplate, daoHelper, rowMapper);
    }

}
