package org.kirillgaidai.income.service.impl.currencyservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.impl.CurrencyDao;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.service.converter.CurrencyConverter;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.CurrencyDto;
import org.kirillgaidai.income.service.exception.IncomeServiceCurrencyNotFoundException;
import org.kirillgaidai.income.service.impl.CurrencyService;
import org.kirillgaidai.income.service.intf.ICurrencyService;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertCurrencyDtoEquals;
import static org.kirillgaidai.income.service.utils.ServiceTestUtils.assertCurrencyDtoListEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class CurrencyServiceTest {

    final private ICurrencyDao currencyDao = mock(CurrencyDao.class);
    final private IGenericConverter<CurrencyEntity, CurrencyDto> currencyConverter = mock(CurrencyConverter.class);
    final private ICurrencyService currencyService = new CurrencyService(currencyDao, currencyConverter);

    @Test
    public void testGetDtoList_AllOk() throws Exception {
        List<CurrencyEntity> currencyEntityList = Arrays.asList(
                new CurrencyEntity(1, "01", "category1", 0),
                new CurrencyEntity(2, "02", "category2", 2),
                new CurrencyEntity(3, "03", "category3", 4)
        );
        List<CurrencyDto> expected = Arrays.asList(
                new CurrencyDto(1, "01", "category1", 0),
                new CurrencyDto(2, "02", "category2", 2),
                new CurrencyDto(3, "03", "category3", 4)
        );

        doReturn(currencyEntityList).when(currencyDao).getList();
        for (int index = 0; index < currencyEntityList.size(); index++) {
            doReturn(expected.get(index)).when(currencyConverter).convertToDto(currencyEntityList.get(index));
        }

        List<CurrencyDto> actual = currencyService.getList();

        assertCurrencyDtoListEquals(expected, actual);
        verify(currencyDao).getList();
        for (CurrencyEntity currencyEntity : currencyEntityList) {
            verify(currencyConverter).convertToDto(currencyEntity);
        }
        verifyNoMoreInteractions(currencyDao, currencyConverter);
    }

    @Test
    public void testGetDtoList_AllEmpty() throws Exception {
        List<CurrencyEntity> currencyEntityList = Collections.emptyList();
        List<CurrencyDto> expected = Collections.emptyList();
        doReturn(currencyEntityList).when(currencyDao).getList();
        List<CurrencyDto> actual = currencyService.getList();
        assertCurrencyDtoListEquals(expected, actual);
        verify(currencyDao).getList();
        verifyNoMoreInteractions(currencyDao, currencyConverter);
    }

    @Test
    public void testGetDtoList_IdsOk() throws Exception {
        Set<Integer> categoryIds = Sets.newSet(1, 2);
        List<CurrencyEntity> currencyEntityList = Arrays.asList(
                new CurrencyEntity(1, "01", "category1", 0),
                new CurrencyEntity(2, "02", "category2", 2)
        );
        List<CurrencyDto> expected = Arrays.asList(
                new CurrencyDto(1, "01", "category1", 0),
                new CurrencyDto(2, "02", "category2", 2)
        );

        doReturn(currencyEntityList).when(currencyDao).getList(categoryIds);
        for (int index = 0; index < currencyEntityList.size(); index++) {
            doReturn(expected.get(index)).when(currencyConverter).convertToDto(currencyEntityList.get(index));
        }

        List<CurrencyDto> actual = currencyService.getList(categoryIds);

        assertCurrencyDtoListEquals(expected, actual);
        verify(currencyDao).getList(categoryIds);
        for (CurrencyEntity currencyEntity : currencyEntityList) {
            verify(currencyConverter).convertToDto(currencyEntity);
        }
        verifyNoMoreInteractions(currencyDao, currencyConverter);
    }

    @Test
    public void testGetDtoList_IdsEmpty() throws Exception {
        Set<Integer> categoryIds = Sets.newSet(1, 2);
        List<CurrencyEntity> currencyEntityList = Collections.emptyList();
        List<CurrencyDto> expected = Collections.emptyList();
        doReturn(currencyEntityList).when(currencyDao).getList(categoryIds);
        List<CurrencyDto> actual = currencyService.getList(categoryIds);
        assertCurrencyDtoListEquals(expected, actual);
        verify(currencyDao).getList(categoryIds);
        verifyNoMoreInteractions(currencyDao, currencyConverter);
    }

}
