package org.kirillgaidai.income.dao.intf;

import org.kirillgaidai.income.dao.entity.RateEntity;

import java.time.LocalDate;
import java.util.List;

public interface IRateDao {

    List<RateEntity> getEntityList();

    List<RateEntity> getEntityList(Integer currencyIdFrom, Integer currencyIdTo, LocalDate firstDay, LocalDate lastDay);

    RateEntity getEntity(Integer currencyIdFrom, Integer currencyIdTo, LocalDate day);

    int insertEntity(RateEntity entity);

    int updateEntity(RateEntity entity);

    int deleteEntity(Integer currencyIdFrom, Integer currencyIdTo, LocalDate day);

}
