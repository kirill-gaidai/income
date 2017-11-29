package org.kirillgaidai.income.service.impl.currencyservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.service.exception.IncomeServiceDependentOnException;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticDeleteException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class CurrencyServiceDeleteTest extends CurrencyServiceBaseTest {

    /**
     * Test id is null
     *
     * @throws Exception exception
     */
    @Test
    public void testNull() throws Exception {
        try {
            service.delete(null);
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

        doReturn(null).when(currencyDao).get(id);

        try {
            service.delete(id);
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Currency with id %d not found", id), e.getMessage());
        }

        verify(currencyDao).get(id);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test dependent accounts found
     *
     * @throws Exception exception
     */
    @Test
    public void testDependentAccountsFound() throws Exception {
        Integer id = 1;

        CurrencyEntity entity = new CurrencyEntity(id, "01", "title", 2);

        doReturn(entity).when(currencyDao).get(id);
        doReturn(1).when(accountDao).getCountByCurrencyId(id);

        try {
            service.delete(id);
        } catch (IncomeServiceDependentOnException e) {
            assertEquals(String.format("Accounts dependent on currency with id %d found", id), e.getMessage());
        }

        verify(currencyDao).get(id);
        verify(accountDao).getCountByCurrencyId(id);

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

        CurrencyEntity entity = new CurrencyEntity(id, "01", "title", 2);

        doReturn(entity).when(currencyDao).get(id);
        doReturn(0).when(accountDao).getCountByCurrencyId(id);
        doReturn(0).when(currencyDao).delete(entity);

        try {
            service.delete(id);
        } catch (IncomeServiceOptimisticDeleteException e) {
            assertEquals(String.format("Currency with id %d delete failure", id), e.getMessage());
        }

        verify(currencyDao).get(id);
        verify(accountDao).getCountByCurrencyId(id);
        verify(currencyDao).delete(entity);

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

        CurrencyEntity entity = new CurrencyEntity(id, "01", "title", 2);

        doReturn(entity).when(currencyDao).get(id);
        doReturn(0).when(accountDao).getCountByCurrencyId(id);
        doReturn(1).when(currencyDao).delete(entity);

        service.delete(id);

        verify(currencyDao).get(id);
        verify(accountDao).getCountByCurrencyId(id);
        verify(currencyDao).delete(entity);

        verifyNoMoreDaoInteractions();
    }

}
