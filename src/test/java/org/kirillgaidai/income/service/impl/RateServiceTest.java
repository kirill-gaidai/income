package org.kirillgaidai.income.service.impl;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.entity.RateEntity;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.dao.intf.IRateDao;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.converter.RateConverter;
import org.kirillgaidai.income.service.dto.RateDto;
import org.kirillgaidai.income.service.intf.IRateService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertRateDtoEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertRateDtoListEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class RateServiceTest {

    final private IRateDao rateDao = mock(IRateDao.class);
    final private ICurrencyDao currencyDao = mock(ICurrencyDao.class);
    final private IGenericConverter<RateEntity, RateDto> rateConverter = mock(RateConverter.class);
    final private IRateService rateService = new RateService(rateDao, currencyDao, rateConverter);

    @Test
    public void testGetDtoList_FullList() throws Exception {
        Integer currencyIdFrom = 10;
        Integer currencyIdTo = 11;
        LocalDate firstDay = LocalDate.of(2017, 8, 1);
        LocalDate lastDay = LocalDate.of(2017, 8, 3);
        CurrencyEntity currencyEntityFrom = new CurrencyEntity(currencyIdFrom, "cc1", "currency1", 2);
        CurrencyEntity currencyEntityTo = new CurrencyEntity(currencyIdTo, "cc2", "currency2", 2);
        List<RateEntity> rateEntityList = Arrays.asList(
                new RateEntity(currencyIdFrom, currencyIdTo, firstDay, new BigDecimal("1.23")),
                new RateEntity(currencyIdFrom, currencyIdTo, LocalDate.of(2017, 8, 2), new BigDecimal("1.24")),
                new RateEntity(currencyIdFrom, currencyIdTo, lastDay, new BigDecimal("1.25"))
        );
        List<RateDto> rateDtoList = Arrays.asList(
                new RateDto(currencyIdFrom, null, null, currencyIdTo, null, null,
                        firstDay, new BigDecimal("1.23")),
                new RateDto(currencyIdFrom, null, null, currencyIdTo, null, null,
                        LocalDate.of(2017, 8, 2), new BigDecimal("1.24")),
                new RateDto(currencyIdFrom, null, null, currencyIdTo, null, null,
                        lastDay, new BigDecimal("1.25"))
        );

        doReturn(currencyEntityFrom).when(currencyDao).getEntity(currencyIdFrom);
        doReturn(currencyEntityTo).when(currencyDao).getEntity(currencyIdTo);
        doReturn(rateEntityList).when(rateDao).getEntityList(currencyIdFrom, currencyIdTo, firstDay, lastDay);
        for (int index = 0; index < rateEntityList.size(); index++) {
            doReturn(rateDtoList.get(index)).when(rateConverter).convertToDto(rateEntityList.get(index));
        }

        List<RateDto> expected = Arrays.asList(
                new RateDto(currencyIdFrom, "cc1", "currency1", currencyIdTo, "cc2", "currency2",
                        firstDay, new BigDecimal("1.23")),
                new RateDto(currencyIdFrom, "cc1", "currency1", currencyIdTo, "cc2", "currency2",
                        LocalDate.of(2017, 8, 2), new BigDecimal("1.24")),
                new RateDto(currencyIdFrom, "cc1", "currency1", currencyIdTo, "cc2", "currency2",
                        lastDay, new BigDecimal("1.25"))
        );
        List<RateDto> actual = rateService.getDtoList(currencyIdFrom, currencyIdTo, firstDay, lastDay);

        assertRateDtoListEquals(expected, actual);

        verify(currencyDao).getEntity(currencyIdFrom);
        verify(currencyDao).getEntity(currencyIdTo);
        verify(rateDao).getEntityList(currencyIdFrom, currencyIdTo, firstDay, lastDay);
        for (RateEntity aRateEntityList : rateEntityList) {
            verify(rateConverter).convertToDto(aRateEntityList);
        }
        verifyNoMoreInteractions(currencyDao, rateDao, rateConverter);
    }

    @Test
    public void testGetDtoList_EmptyList() throws Exception {
        Integer currencyIdFrom = 10;
        Integer currencyIdTo = 11;
        LocalDate firstDay = LocalDate.of(2017, 8, 1);
        LocalDate lastDay = LocalDate.of(2017, 8, 3);
        CurrencyEntity currencyEntityFrom = new CurrencyEntity(currencyIdFrom, "cc1", "currency1", 2);
        CurrencyEntity currencyEntityTo = new CurrencyEntity(currencyIdTo, "cc2", "currency2", 2);
        List<RateEntity> rateEntityList = Collections.emptyList();

        doReturn(currencyEntityFrom).when(currencyDao).getEntity(currencyIdFrom);
        doReturn(currencyEntityTo).when(currencyDao).getEntity(currencyIdTo);
        doReturn(rateEntityList).when(rateDao).getEntityList(currencyIdFrom, currencyIdTo, firstDay, lastDay);

        List<RateDto> expected = Arrays.asList(
                new RateDto(currencyIdFrom, "cc1", "currency1", currencyIdTo, "cc2", "currency2",
                        firstDay, BigDecimal.ZERO),
                new RateDto(currencyIdFrom, "cc1", "currency1", currencyIdTo, "cc2", "currency2",
                        LocalDate.of(2017, 8, 2), BigDecimal.ZERO),
                new RateDto(currencyIdFrom, "cc1", "currency1", currencyIdTo, "cc2", "currency2",
                        lastDay, BigDecimal.ZERO)
        );

        List<RateDto> actual = rateService.getDtoList(currencyIdFrom, currencyIdTo, firstDay, lastDay);

        assertRateDtoListEquals(expected, actual);

        verify(currencyDao).getEntity(currencyIdFrom);
        verify(currencyDao).getEntity(currencyIdTo);
        verify(rateDao).getEntityList(currencyIdFrom, currencyIdTo, firstDay, lastDay);
        verifyNoMoreInteractions(currencyDao, rateDao, rateConverter);
    }

    @Test
    public void testGetDtoList_FirstAbsent() throws Exception {
        Integer currencyIdFrom = 10;
        Integer currencyIdTo = 11;
        LocalDate firstDay = LocalDate.of(2017, 8, 1);
        LocalDate lastDay = LocalDate.of(2017, 8, 3);
        CurrencyEntity currencyEntityFrom = new CurrencyEntity(currencyIdFrom, "cc1", "currency1", 2);
        CurrencyEntity currencyEntityTo = new CurrencyEntity(currencyIdTo, "cc2", "currency2", 2);
        List<RateEntity> rateEntityList = Arrays.asList(
                new RateEntity(currencyIdFrom, currencyIdTo, LocalDate.of(2017, 8, 2), new BigDecimal("1.24")),
                new RateEntity(currencyIdFrom, currencyIdTo, lastDay, new BigDecimal("1.25"))
        );
        List<RateDto> rateDtoList = Arrays.asList(
                new RateDto(currencyIdFrom, null, null, currencyIdTo, null, null,
                        LocalDate.of(2017, 8, 2), new BigDecimal("1.24")),
                new RateDto(currencyIdFrom, null, null, currencyIdTo, null, null,
                        lastDay, new BigDecimal("1.25"))
        );

        doReturn(currencyEntityFrom).when(currencyDao).getEntity(currencyIdFrom);
        doReturn(currencyEntityTo).when(currencyDao).getEntity(currencyIdTo);
        doReturn(rateEntityList).when(rateDao).getEntityList(currencyIdFrom, currencyIdTo, firstDay, lastDay);
        for (int index = 0; index < rateEntityList.size(); index++) {
            doReturn(rateDtoList.get(index)).when(rateConverter).convertToDto(rateEntityList.get(index));
        }

        List<RateDto> expected = Arrays.asList(
                new RateDto(currencyIdFrom, "cc1", "currency1", currencyIdTo, "cc2", "currency2",
                        firstDay, BigDecimal.ZERO),
                new RateDto(currencyIdFrom, "cc1", "currency1", currencyIdTo, "cc2", "currency2",
                        LocalDate.of(2017, 8, 2), new BigDecimal("1.24")),
                new RateDto(currencyIdFrom, "cc1", "currency1", currencyIdTo, "cc2", "currency2",
                        lastDay, new BigDecimal("1.25"))
        );
        List<RateDto> actual = rateService.getDtoList(currencyIdFrom, currencyIdTo, firstDay, lastDay);

        assertRateDtoListEquals(expected, actual);

        verify(currencyDao).getEntity(currencyIdFrom);
        verify(currencyDao).getEntity(currencyIdTo);
        verify(rateDao).getEntityList(currencyIdFrom, currencyIdTo, firstDay, lastDay);
        for (RateEntity aRateEntityList : rateEntityList) {
            verify(rateConverter).convertToDto(aRateEntityList);
        }
        verifyNoMoreInteractions(currencyDao, rateDao, rateConverter);
    }

    @Test
    public void testGetDtoList_LastAbsent() throws Exception {
        Integer currencyIdFrom = 10;
        Integer currencyIdTo = 11;
        LocalDate firstDay = LocalDate.of(2017, 8, 1);
        LocalDate lastDay = LocalDate.of(2017, 8, 3);
        CurrencyEntity currencyEntityFrom = new CurrencyEntity(currencyIdFrom, "cc1", "currency1", 2);
        CurrencyEntity currencyEntityTo = new CurrencyEntity(currencyIdTo, "cc2", "currency2", 2);
        List<RateEntity> rateEntityList = Arrays.asList(
                new RateEntity(currencyIdFrom, currencyIdTo, firstDay, new BigDecimal("1.23")),
                new RateEntity(currencyIdFrom, currencyIdTo, LocalDate.of(2017, 8, 2), new BigDecimal("1.24"))
        );
        List<RateDto> rateDtoList = Arrays.asList(
                new RateDto(currencyIdFrom, null, null, currencyIdTo, null, null,
                        firstDay, new BigDecimal("1.23")),
                new RateDto(currencyIdFrom, null, null, currencyIdTo, null, null,
                        LocalDate.of(2017, 8, 2), new BigDecimal("1.24"))
        );

        doReturn(currencyEntityFrom).when(currencyDao).getEntity(currencyIdFrom);
        doReturn(currencyEntityTo).when(currencyDao).getEntity(currencyIdTo);
        doReturn(rateEntityList).when(rateDao).getEntityList(currencyIdFrom, currencyIdTo, firstDay, lastDay);
        for (int index = 0; index < rateEntityList.size(); index++) {
            doReturn(rateDtoList.get(index)).when(rateConverter).convertToDto(rateEntityList.get(index));
        }

        List<RateDto> expected = Arrays.asList(
                new RateDto(currencyIdFrom, "cc1", "currency1", currencyIdTo, "cc2", "currency2",
                        firstDay, new BigDecimal("1.23")),
                new RateDto(currencyIdFrom, "cc1", "currency1", currencyIdTo, "cc2", "currency2",
                        LocalDate.of(2017, 8, 2), new BigDecimal("1.24")),
                new RateDto(currencyIdFrom, "cc1", "currency1", currencyIdTo, "cc2", "currency2",
                        lastDay, BigDecimal.ZERO)
        );
        List<RateDto> actual = rateService.getDtoList(currencyIdFrom, currencyIdTo, firstDay, lastDay);

        assertRateDtoListEquals(expected, actual);

        verify(currencyDao).getEntity(currencyIdFrom);
        verify(currencyDao).getEntity(currencyIdTo);
        verify(rateDao).getEntityList(currencyIdFrom, currencyIdTo, firstDay, lastDay);
        for (RateEntity aRateEntityList : rateEntityList) {
            verify(rateConverter).convertToDto(aRateEntityList);
        }
        verifyNoMoreInteractions(currencyDao, rateDao, rateConverter);
    }

    @Test
    public void testGetDtoList_MiddleAbsent() throws Exception {
        Integer currencyIdFrom = 10;
        Integer currencyIdTo = 11;
        LocalDate firstDay = LocalDate.of(2017, 8, 1);
        LocalDate lastDay = LocalDate.of(2017, 8, 3);
        CurrencyEntity currencyEntityFrom = new CurrencyEntity(currencyIdFrom, "cc1", "currency1", 2);
        CurrencyEntity currencyEntityTo = new CurrencyEntity(currencyIdTo, "cc2", "currency2", 2);
        List<RateEntity> rateEntityList = Arrays.asList(
                new RateEntity(currencyIdFrom, currencyIdTo, firstDay, new BigDecimal("1.23")),
                new RateEntity(currencyIdFrom, currencyIdTo, lastDay, new BigDecimal("1.25"))
        );
        List<RateDto> rateDtoList = Arrays.asList(
                new RateDto(currencyIdFrom, null, null, currencyIdTo, null, null,
                        firstDay, new BigDecimal("1.23")),
                new RateDto(currencyIdFrom, null, null, currencyIdTo, null, null,
                        lastDay, new BigDecimal("1.25"))
        );

        doReturn(currencyEntityFrom).when(currencyDao).getEntity(currencyIdFrom);
        doReturn(currencyEntityTo).when(currencyDao).getEntity(currencyIdTo);
        doReturn(rateEntityList).when(rateDao).getEntityList(currencyIdFrom, currencyIdTo, firstDay, lastDay);
        for (int index = 0; index < rateEntityList.size(); index++) {
            doReturn(rateDtoList.get(index)).when(rateConverter).convertToDto(rateEntityList.get(index));
        }

        List<RateDto> expected = Arrays.asList(
                new RateDto(currencyIdFrom, "cc1", "currency1", currencyIdTo, "cc2", "currency2",
                        firstDay, new BigDecimal("1.23")),
                new RateDto(currencyIdFrom, "cc1", "currency1", currencyIdTo, "cc2", "currency2",
                        LocalDate.of(2017, 8, 2), BigDecimal.ZERO),
                new RateDto(currencyIdFrom, "cc1", "currency1", currencyIdTo, "cc2", "currency2",
                        lastDay, new BigDecimal("1.25"))
        );
        List<RateDto> actual = rateService.getDtoList(currencyIdFrom, currencyIdTo, firstDay, lastDay);

        assertRateDtoListEquals(expected, actual);

        verify(currencyDao).getEntity(currencyIdFrom);
        verify(currencyDao).getEntity(currencyIdTo);
        verify(rateDao).getEntityList(currencyIdFrom, currencyIdTo, firstDay, lastDay);
        for (RateEntity aRateEntityList : rateEntityList) {
            verify(rateConverter).convertToDto(aRateEntityList);
        }
        verifyNoMoreInteractions(currencyDao, rateDao, rateConverter);
    }


    @Test
    public void testGetDto_Exists() throws Exception {
        Integer currencyIdFrom = 10;
        Integer currencyIdTo = 11;
        LocalDate day = LocalDate.of(2017, 8, 1);
        BigDecimal value = new BigDecimal("1.23");
        CurrencyEntity currencyEntityFrom = new CurrencyEntity(currencyIdFrom, "cc1", "currency1", 2);
        CurrencyEntity currencyEntityTo = new CurrencyEntity(currencyIdTo, "cc2", "currency2", 2);
        RateEntity rateEntity = new RateEntity(currencyIdFrom, currencyIdTo, day, value);
        RateDto rateDto = new RateDto(currencyIdFrom, null, null, currencyIdTo, null, null, day, value);

        doReturn(currencyEntityFrom).when(currencyDao).getEntity(currencyIdFrom);
        doReturn(currencyEntityTo).when(currencyDao).getEntity(currencyIdTo);
        doReturn(rateEntity).when(rateDao).getEntity(currencyIdFrom, currencyIdTo, day);
        doReturn(rateDto).when(rateConverter).convertToDto(rateEntity);

        RateDto expected = new RateDto(currencyIdFrom, "cc1", "currency1",
                currencyIdTo, "cc2", "currency2", day, value);
        RateDto actual = rateService.getDto(currencyIdFrom, currencyIdTo, day);

        assertRateDtoEquals(expected, actual);

        verify(currencyDao).getEntity(currencyIdFrom);
        verify(currencyDao).getEntity(currencyIdTo);
        verify(rateDao).getEntity(currencyIdFrom, currencyIdTo, day);
        verify(rateConverter).convertToDto(rateEntity);
        verifyNoMoreInteractions(currencyDao, rateDao, rateConverter);
    }

    @Test
    public void testGetDto_NotExist() throws Exception {
        Integer currencyIdFrom = 10;
        Integer currencyIdTo = 11;
        LocalDate day = LocalDate.of(2017, 8, 1);
        CurrencyEntity currencyEntityFrom = new CurrencyEntity(currencyIdFrom, "cc1", "currency1", 2);
        CurrencyEntity currencyEntityTo = new CurrencyEntity(currencyIdTo, "cc2", "currency2", 2);

        doReturn(currencyEntityFrom).when(currencyDao).getEntity(currencyIdFrom);
        doReturn(currencyEntityTo).when(currencyDao).getEntity(currencyIdTo);

        RateDto expected = new RateDto(currencyIdFrom, "cc1", "currency1",
                currencyIdTo, "cc2", "currency2", day, BigDecimal.ZERO);
        RateDto actual = rateService.getDto(currencyIdFrom, currencyIdTo, day);

        assertRateDtoEquals(expected, actual);

        verify(currencyDao).getEntity(currencyIdFrom);
        verify(currencyDao).getEntity(currencyIdTo);
        verify(rateDao).getEntity(currencyIdFrom, currencyIdTo, day);
        verifyNoMoreInteractions(currencyDao, rateDao, rateConverter);
    }

}
