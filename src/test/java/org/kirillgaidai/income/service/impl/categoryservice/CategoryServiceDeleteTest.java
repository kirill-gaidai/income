package org.kirillgaidai.income.service.impl.categoryservice;

import org.junit.Test;
import org.kirillgaidai.income.service.exception.IncomeServiceCategoryNotFoundException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class CategoryServiceDeleteTest extends CategoryServiceBaseTest {

    @Test
    public void testNull() throws Exception {
        try {
            service.delete(null);
        } catch (IllegalArgumentException e) {
            assertEquals("null", e.getMessage());
        }
        verifyNoMoreInteractions();
    }

    @Test
    public void testNotFound() throws Exception {
        doReturn(0).when(categoryDao).delete(1);
        try {
            service.delete(1);
        } catch (IncomeServiceCategoryNotFoundException e) {
            assertEquals("Category with id 1 not found", e.getMessage());
        }
        verify(categoryDao).delete(1);
        verifyNoMoreInteractions();
    }

    @Test
    public void testOk() throws Exception {
        doReturn(1).when(categoryDao).delete(1);
        service.delete(1);
        verify(categoryDao).delete(1);
        verifyNoMoreInteractions();
    }

}
