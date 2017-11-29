package org.kirillgaidai.income.service.impl.categoryservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CategoryEntity;
import org.kirillgaidai.income.service.exception.IncomeServiceDependentOnException;
import org.kirillgaidai.income.service.exception.IncomeServiceNotFoundException;
import org.kirillgaidai.income.service.exception.optimistic.IncomeServiceOptimisticDeleteException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class CategoryServiceDeleteTest extends CategoryServiceBaseTest {

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

        doReturn(null).when(categoryDao).get(id);

        try {
            service.delete(id);
        } catch (IncomeServiceNotFoundException e) {
            assertEquals(String.format("Category with id %d not found", id), e.getMessage());
        }

        verify(categoryDao).get(id);

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

        CategoryEntity entity = new CategoryEntity(id, "01", "title");

        doReturn(entity).when(categoryDao).get(id);
        doReturn(1).when(operationDao).getCountByCategoryId(id);

        try {
            service.delete(id);
        } catch (IncomeServiceDependentOnException e) {
            assertEquals(String.format("Operations dependent on category with id %d found", id), e.getMessage());
        }

        verify(categoryDao).get(id);
        verify(operationDao).getCountByCategoryId(id);

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

        CategoryEntity entity = new CategoryEntity(id, "01", "title");

        doReturn(entity).when(categoryDao).get(id);
        doReturn(0).when(operationDao).getCountByCategoryId(id);
        doReturn(0).when(categoryDao).delete(entity);

        try {
            service.delete(id);
        } catch (IncomeServiceOptimisticDeleteException e) {
            assertEquals(String.format("Category with id %d delete failure", id), e.getMessage());
        }

        verify(categoryDao).get(id);
        verify(operationDao).getCountByCategoryId(id);
        verify(categoryDao).delete(entity);

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

        CategoryEntity entity = new CategoryEntity(id, "01", "title");

        doReturn(entity).when(categoryDao).get(id);
        doReturn(0).when(operationDao).getCountByCategoryId(id);
        doReturn(1).when(categoryDao).delete(entity);

        service.delete(id);

        verify(categoryDao).get(id);
        verify(operationDao).getCountByCategoryId(id);
        verify(categoryDao).delete(entity);

        verifyNoMoreDaoInteractions();
    }

}
