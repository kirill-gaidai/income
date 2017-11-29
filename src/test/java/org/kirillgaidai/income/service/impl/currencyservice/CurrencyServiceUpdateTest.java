package org.kirillgaidai.income.service.impl.currencyservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.service.dto.CurrencyDto;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticUpdateException;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class CurrencyServiceUpdateTest extends CurrencyServiceBaseTest {

    /**
     * Test null
     *
     * @throws Exception exception
     */
    @Test
    public void testNull() throws Exception {
        try {
            service.update(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Dto is null", e.getMessage());
        }

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test id is null
     *
     * @throws Exception exception
     */
    @Test
    public void testIdIsNull() throws Exception {
        CurrencyDto dto = new CurrencyDto(null, "01", "title", 2);

        try {
            service.update(dto);
        } catch (IllegalArgumentException e) {
            assertEquals("Id is null", e.getMessage());
        }

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test not found
     *
     * @throws Exception exception
     */
    @Test
    public void testNotFound() throws Exception {
        Integer id = 1;

        CurrencyDto dto = new CurrencyDto(id, "01", "title", 2);

        doReturn(null).when(currencyDao).get(id);

        try {
            service.update(dto);
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Currency with id %d not found", id), e.getMessage());
        }

        verify(currencyDao).get(id);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test failure
     *
     * @throws Exception exception
     */
    @Test
    public void testFailure() throws Exception {
        Integer id = 1;

        CurrencyDto dto = new CurrencyDto(id, "02", "newTitle", 3);
        CurrencyEntity oldEntity = new CurrencyEntity(id, "01", "oldTitle", 2);

        doReturn(oldEntity).when(currencyDao).get(id);
        doReturn(0).when(currencyDao).update(any(CurrencyEntity.class), eq(oldEntity));

        try {
            service.update(dto);
        } catch (IncomeServiceOptimisticUpdateException e) {
            assertEquals(String.format("Currency with id %d update failure", id), e.getMessage());
        }

        ArgumentCaptor<CurrencyEntity> argumentCaptor = ArgumentCaptor.forClass(CurrencyEntity.class);

        verify(currencyDao).get(id);
        verify(currencyDao).update(argumentCaptor.capture(), eq(oldEntity));

        CurrencyEntity expectedEntity = new CurrencyEntity(id, "02", "newTitle", 3);
        CurrencyEntity actualEntity = argumentCaptor.getValue();
        assertEntityEquals(expectedEntity, actualEntity);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Integer id = 1;

        CurrencyDto dto = new CurrencyDto(id, "02", "newTitle", 3);
        CurrencyEntity oldEntity = new CurrencyEntity(id, "01", "oldTitle", 2);

        doReturn(oldEntity).when(currencyDao).get(id);
        doReturn(1).when(currencyDao).update(any(CurrencyEntity.class), eq(oldEntity));

        CurrencyDto expectedDto = new CurrencyDto(id, "02", "newTitle", 3);
        CurrencyDto actualDto = service.update(dto);
        assertEntityEquals(expectedDto, actualDto);

        ArgumentCaptor<CurrencyEntity> argumentCaptor = ArgumentCaptor.forClass(CurrencyEntity.class);

        verify(currencyDao).get(id);
        verify(currencyDao).update(argumentCaptor.capture(), eq(oldEntity));

        CurrencyEntity expectedEntity = new CurrencyEntity(id, "02", "newTitle", 3);
        CurrencyEntity actualEntity = argumentCaptor.getValue();
        assertEntityEquals(expectedEntity, actualEntity);

        verifyNoMoreDaoInteractions();
    }

}
