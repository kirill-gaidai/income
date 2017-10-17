package org.kirillgaidai.income.service.impl;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.dao.impl.CurrencyDao;
import org.kirillgaidai.income.dao.intf.ICurrencyDao;
import org.kirillgaidai.income.service.converter.CurrencyConverter;
import org.kirillgaidai.income.service.converter.IGenericConverter;
import org.kirillgaidai.income.service.dto.CurrencyDto;
import org.kirillgaidai.income.service.exception.IncomeServiceCurrencyNotFoundException;
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

        doReturn(currencyEntityList).when(currencyDao).getEntityList();
        for (int index = 0; index < currencyEntityList.size(); index++) {
            doReturn(expected.get(index)).when(currencyConverter).convertToDto(currencyEntityList.get(index));
        }

        List<CurrencyDto> actual = currencyService.getList();

        assertCurrencyDtoListEquals(expected, actual);
        verify(currencyDao).getEntityList();
        for (CurrencyEntity currencyEntity : currencyEntityList) {
            verify(currencyConverter).convertToDto(currencyEntity);
        }
        verifyNoMoreInteractions(currencyDao, currencyConverter);
    }

    @Test
    public void testGetDtoList_AllEmpty() throws Exception {
        List<CurrencyEntity> currencyEntityList = Collections.emptyList();
        List<CurrencyDto> expected = Collections.emptyList();
        doReturn(currencyEntityList).when(currencyDao).getEntityList();
        List<CurrencyDto> actual = currencyService.getList();
        assertCurrencyDtoListEquals(expected, actual);
        verify(currencyDao).getEntityList();
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

        doReturn(currencyEntityList).when(currencyDao).getEntityList(categoryIds);
        for (int index = 0; index < currencyEntityList.size(); index++) {
            doReturn(expected.get(index)).when(currencyConverter).convertToDto(currencyEntityList.get(index));
        }

        List<CurrencyDto> actual = currencyService.getList(categoryIds);

        assertCurrencyDtoListEquals(expected, actual);
        verify(currencyDao).getEntityList(categoryIds);
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
        doReturn(currencyEntityList).when(currencyDao).getEntityList(categoryIds);
        List<CurrencyDto> actual = currencyService.getList(categoryIds);
        assertCurrencyDtoListEquals(expected, actual);
        verify(currencyDao).getEntityList(categoryIds);
        verifyNoMoreInteractions(currencyDao, currencyConverter);
    }

    @Test
    public void testGetDto_Null() throws Exception {
        try {
            currencyService.get(null);
        } catch (IncomeServiceCurrencyNotFoundException e) {
            assertEquals("Currency not found", e.getMessage());
        }
        verifyNoMoreInteractions(currencyDao, currencyConverter);
    }

    @Test
    public void testGetDto_NotFound() throws Exception {
        try {
            currencyService.get(1);
        } catch (IncomeServiceCurrencyNotFoundException e) {
            assertEquals("Currency with id 1 not found", e.getMessage());
        }
        verify(currencyDao).getEntity(1);
        verifyNoMoreInteractions(currencyDao, currencyConverter);
    }

    @Test
    public void testGetDto_Ok() throws Exception {
        CurrencyEntity currencyEntity = new CurrencyEntity(1, "01", "category1", 2);
        CurrencyDto expected = new CurrencyDto(1, "01", "category1", 2);

        doReturn(currencyEntity).when(currencyDao).getEntity(1);
        doReturn(expected).when(currencyConverter).convertToDto(currencyEntity);

        CurrencyDto actual = currencyService.get(1);

        assertCurrencyDtoEquals(expected, actual);

        verify(currencyDao).getEntity(1);
        verify(currencyConverter).convertToDto(currencyEntity);
        verifyNoMoreInteractions(currencyDao, currencyConverter);
    }

    @Test
    public void testSaveDto_Null() throws Exception {
        try {
            currencyService.saveDto(null);
        } catch (IncomeServiceCurrencyNotFoundException e) {
            assertEquals("Currency not found", e.getMessage());
        }
        verifyNoMoreInteractions(currencyDao, currencyConverter);
    }

    @Test
    public void testSaveDto_Insert() throws Exception {
        CurrencyDto categoryDto = new CurrencyDto(null, "01", "category1", 2);
        CurrencyEntity currencyEntity = new CurrencyEntity(null, "01", "category1", 2);

        doReturn(currencyEntity).when(currencyConverter).convertToEntity(categoryDto);
        doReturn(1).when(currencyDao).insertEntity(currencyEntity);

        currencyService.saveDto(categoryDto);

        verify(currencyConverter).convertToEntity(categoryDto);
        verify(currencyDao).insertEntity(currencyEntity);
        verifyNoMoreInteractions(currencyDao, currencyConverter);
    }

    @Test
    public void testSaveDto_UpdateNotFound() throws Exception {
        CurrencyDto categoryDto = new CurrencyDto(1, "01", "category1", 2);
        CurrencyEntity currencyEntity = new CurrencyEntity(1, "01", "category1", 2);

        doReturn(currencyEntity).when(currencyConverter).convertToEntity(categoryDto);
        doReturn(0).when(currencyDao).updateEntity(currencyEntity);

        try {
            currencyService.saveDto(categoryDto);
        } catch (IncomeServiceCurrencyNotFoundException e) {
            assertEquals("Currency with id 1 not found", e.getMessage());
        }

        verify(currencyConverter).convertToEntity(categoryDto);
        verify(currencyDao).updateEntity(currencyEntity);
        verifyNoMoreInteractions(currencyDao, currencyConverter);
    }

    @Test
    public void testSaveDto_Update() throws Exception {
        CurrencyDto categoryDto = new CurrencyDto(1, "01", "category1", 2);
        CurrencyEntity currencyEntity = new CurrencyEntity(1, "01", "category1", 2);

        doReturn(currencyEntity).when(currencyConverter).convertToEntity(categoryDto);
        doReturn(1).when(currencyDao).updateEntity(currencyEntity);

        currencyService.saveDto(categoryDto);

        verify(currencyConverter).convertToEntity(categoryDto);
        verify(currencyDao).updateEntity(currencyEntity);
        verifyNoMoreInteractions(currencyDao, currencyConverter);
    }

    @Test
    public void testDeleteDto_Null() throws Exception {
        try {
            currencyService.deleteDto(null);
        } catch (IncomeServiceCurrencyNotFoundException e) {
            assertEquals("Currency not found", e.getMessage());
        }
        verifyNoMoreInteractions(currencyDao, currencyConverter);
    }

    @Test
    public void testDeleteDto_NotFound() throws Exception {
        doReturn(0).when(currencyDao).deleteEntity(1);
        try {
            currencyService.deleteDto(1);
        } catch (IncomeServiceCurrencyNotFoundException e) {
            assertEquals("Currency with id 1 not found", e.getMessage());
        }
        verify(currencyDao).deleteEntity(1);
        verifyNoMoreInteractions(currencyDao, currencyConverter);
    }

    @Test
    public void testDeleteDto_Ok() throws Exception {
        doReturn(1).when(currencyDao).deleteEntity(1);
        currencyService.deleteDto(1);
        verify(currencyDao).deleteEntity(1);
        verifyNoMoreInteractions(currencyDao, currencyConverter);
    }

}
