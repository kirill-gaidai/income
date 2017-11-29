package org.kirillgaidai.income.service.impl.currencyservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.service.dto.CurrencyDto;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticCreateException;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.kirillgaidai.income.utils.TestUtils.assertEntityEquals;
import static org.kirillgaidai.income.utils.TestUtils.getSerialEntityInsertAnswer;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class CurrencyServiceCreateTest extends CurrencyServiceBaseTest {

    /**
     * Test dto is null
     *
     * @throws Exception exception
     */
    @Test
    public void testNull() throws Exception {
        try {
            service.create(null);
        } catch (IllegalArgumentException e) {
            assertEquals("Dto is null", e.getMessage());
        }

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test failure
     *
     * @throws Exception exception
     */
    @Test
    public void testFailure() throws Exception {
        CurrencyDto dto = new CurrencyDto(null, "01", "title", 2);

        doReturn(0).when(currencyDao).insert(any(CurrencyEntity.class));

        try {
            service.create(dto);
        } catch (IncomeServiceOptimisticCreateException e) {
            assertEquals("Currency create failure", e.getMessage());
        }

        ArgumentCaptor<CurrencyEntity> argumentCaptor = ArgumentCaptor.forClass(CurrencyEntity.class);

        verify(currencyDao).insert(argumentCaptor.capture());

        CurrencyEntity expectedEntity = new CurrencyEntity(null, "01", "title", 2);
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

        CurrencyDto dto = new CurrencyDto(null, "01", "title", 2);

        doAnswer(getSerialEntityInsertAnswer(id)).when(currencyDao).insert(any(CurrencyEntity.class));

        CurrencyDto expected = new CurrencyDto(1, "01", "title", 2);
        CurrencyDto actual = service.create(dto);
        assertEntityEquals(expected, actual);

        ArgumentCaptor<CurrencyEntity> argumentCaptor = ArgumentCaptor.forClass(CurrencyEntity.class);

        verify(currencyDao).insert(argumentCaptor.capture());

        CurrencyEntity expectedEntity = new CurrencyEntity(1, "01", "title", 2);
        CurrencyEntity actualEntity = argumentCaptor.getValue();
        assertEntityEquals(expectedEntity, actualEntity);

        verifyNoMoreDaoInteractions();
    }

}
