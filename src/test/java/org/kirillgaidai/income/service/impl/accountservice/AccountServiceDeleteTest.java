package org.kirillgaidai.income.service.impl.accountservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.AccountEntity;
import org.kirillgaidai.income.service.exception.IncomeServiceDependentOnException;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticDeleteException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class AccountServiceDeleteTest extends AccountServiceBaseTest {

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

        doReturn(null).when(accountDao).get(id);

        try {
            service.delete(id);
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Account with id %d not found", id), e.getMessage());
        }

        verify(accountDao).get(id);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test dependent operations found
     *
     * @throws Exception exception
     */
    @Test
    public void testDependentOperationsFound() throws Exception {
        Integer id = 1;

        AccountEntity entity = new AccountEntity(id, 2, "01", "title");

        doReturn(entity).when(accountDao).get(id);
        doReturn(1).when(operationDao).getCountByAccountId(id);
        doReturn(0).when(balanceDao).getCountByAccountId(id);

        try {
            service.delete(id);
        } catch (IncomeServiceDependentOnException e) {
            assertEquals(String.format("Operations dependent on account with id %d found", id), e.getMessage());
        }

        verify(accountDao).get(id);
        verify(operationDao).getCountByAccountId(id);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test dependent balances found
     *
     * @throws Exception exception
     */
    @Test
    public void testDependentBalancesFound() throws Exception {
        Integer id = 1;

        AccountEntity entity = new AccountEntity(id, 2, "01", "title");

        doReturn(entity).when(accountDao).get(id);
        doReturn(0).when(operationDao).getCountByAccountId(id);
        doReturn(1).when(balanceDao).getCountByAccountId(id);

        try {
            service.delete(id);
        } catch (IncomeServiceDependentOnException e) {
            assertEquals(String.format("Balances dependent on account with id %d found", id), e.getMessage());
        }

        verify(accountDao).get(id);
        verify(operationDao).getCountByAccountId(id);
        verify(balanceDao).getCountByAccountId(id);

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

        AccountEntity entity = new AccountEntity(id, 2, "01", "title");

        doReturn(entity).when(accountDao).get(id);
        doReturn(0).when(operationDao).getCountByAccountId(id);
        doReturn(0).when(balanceDao).getCountByAccountId(id);
        doReturn(0).when(accountDao).delete(entity);

        try {
            service.delete(id);
        } catch (IncomeServiceOptimisticDeleteException e) {
            assertEquals(String.format("Account with id %d delete failure", id), e.getMessage());
        }

        verify(accountDao).get(id);
        verify(operationDao).getCountByAccountId(id);
        verify(balanceDao).getCountByAccountId(id);
        verify(accountDao).delete(entity);

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

        AccountEntity entity = new AccountEntity(id, 2, "01", "title");

        doReturn(entity).when(accountDao).get(id);
        doReturn(0).when(operationDao).getCountByAccountId(id);
        doReturn(0).when(balanceDao).getCountByAccountId(id);
        doReturn(1).when(accountDao).delete(entity);

        service.delete(id);

        verify(accountDao).get(id);
        verify(operationDao).getCountByAccountId(id);
        verify(balanceDao).getCountByAccountId(id);
        verify(accountDao).delete(entity);

        verifyNoMoreDaoInteractions();
    }


}
