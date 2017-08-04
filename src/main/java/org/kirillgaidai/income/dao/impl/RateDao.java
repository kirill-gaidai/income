package org.kirillgaidai.income.dao.impl;

import org.kirillgaidai.income.dao.entity.RateEntity;
import org.kirillgaidai.income.dao.exception.IncomeDuplicateDaoException;
import org.kirillgaidai.income.dao.intf.IRateDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RateDao implements IRateDao {

    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    final private RowMapper<RateEntity> rateEntityRowMapper;

    @Autowired
    public RateDao(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate,
            RowMapper<RateEntity> rateEntityRowMapper) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.rateEntityRowMapper = rateEntityRowMapper;
    }

    @Override
    public List<RateEntity> getEntityList() {
        String sql = "SELECT currency_id_from, currency_id_to, day, value FROM rates ORDER BY id";
        return namedParameterJdbcTemplate.query(sql, rateEntityRowMapper);
    }

    @Override
    public List<RateEntity> getEntityList(
            Integer currencyIdFrom, Integer currencyIdTo, LocalDate firstDay, LocalDate lastDay) {
        String sql = "SELECT currency_id_from, currency_id_to, day, value FROM rates " +
                "WHERE (currency_id_from = :currency_id_from) AND (currency_id_to = :currency_id_to) " +
                "AND (day BETWEEN :first_day AND :last_day) ORDER BY day";
        Map<String, Object> params = new HashMap<>();
        params.put("currency_id_from", currencyIdFrom);
        params.put("currency_id_to", currencyIdTo);
        params.put("first_day", Date.valueOf(firstDay));
        params.put("last_day", Date.valueOf(lastDay));
        return namedParameterJdbcTemplate.query(sql, params, rateEntityRowMapper);
    }

    @Override
    public RateEntity getEntity(Integer currencyIdFrom, Integer currencyIdTo, LocalDate day) {
        String sql = "SELECT currency_id_from, currency_id_to, day, value FROM rates " +
                "WHERE (currency_id_from = :currency_id_from) AND (currency_id_to = :currency_id_to) " +
                "AND (((:day IS NULL) AND (day IS NULL)) OR ((:day IS NOT NULL) AND (day = :day)))";
        Map<String, Object> params = new HashMap<>();
        params.put("currency_id_from", currencyIdFrom);
        params.put("currency_id_to", currencyIdTo);
        params.put("day", day == null ? null : Date.valueOf(day));
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, params, rateEntityRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int insertEntity(RateEntity entity) {
        Map<String, Object> params = new HashMap<>();
        params.put("currency_id_from", entity.getCurrencyIdFrom());
        params.put("currency_id_to", entity.getCurrencyIdTo());
        LocalDate day = entity.getDay();
        params.put("day", day == null ? null : Date.valueOf(day));
        params.put("value", entity.getValue());

        String sql = "SELECT COUNT(*) FROM rates WHERE (currency_id_from = :currency_id_from) " +
                "AND (currency_id_to = :currency_id_to)";
        int rowCount = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);

        if (entity.getDay() == null && rowCount != 0) {
            // Can not insert rate without date in case it already exists
            throw new IncomeDuplicateDaoException("Rate entity already exists");
        } else if (entity.getDay() != null) {
            sql = "SELECT COUNT(*) FROM rates WHERE (currency_id_from = :currency_id_from) " +
                    "AND (currency_id_to = :currency_id_to) AND (day = :day)";
            rowCount = namedParameterJdbcTemplate.queryForObject(sql, params, Integer.class);
            if (rowCount != 0) {
                // Can not specify two rates for one day
                throw new IncomeDuplicateDaoException("Rate entity already exists");
            }
        }

        sql = "INSERT INTO rates(currency_id_from, currency_id_to, day, value) " +
                "VALUES(:currency_id_from, :currency_id_to, :day, :value)";
        return namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public int updateEntity(RateEntity entity) {
        String sql = "UPDATE rates SET value = :value " +
                "WHERE (currency_id_from = :currency_id_from) AND (currency_id_to = :currency_id_to) " +
                "AND (((:day IS NULL) AND (day IS NULL)) OR ((:day IS NOT NULL) AND (day = :day)))";
        Map<String, Object> params = new HashMap<>();
        params.put("currency_id_from", entity.getCurrencyIdFrom());
        params.put("currency_id_to", entity.getCurrencyIdTo());
        LocalDate day = entity.getDay();
        params.put("day", day == null ? null : Date.valueOf(day));
        params.put("value", entity.getValue());
        return namedParameterJdbcTemplate.update(sql, params);
    }

    @Override
    public int deleteEntity(Integer currencyIdFrom, Integer currencyIdTo, LocalDate day) {
        String sql = "DELETE FROM rates " +
                "WHERE (currency_id_from = :currency_id_from) AND (currency_id_to = :currency_id_to) " +
                "AND (((:day IS NULL) AND (day IS NULL)) OR ((:day IS NOT NULL) AND (day = :day)))";
        Map<String, Object> params = new HashMap<>();
        params.put("currency_id_from", currencyIdFrom);
        params.put("currency_id_to", currencyIdTo);
        params.put("day", day == null ? null : Date.valueOf(day));
        return namedParameterJdbcTemplate.update(sql, params);
    }

}
