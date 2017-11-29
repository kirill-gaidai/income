package org.kirillgaidai.income.service.impl.currencyservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.service.dto.CurrencyDto;
import org.mockito.internal.util.collections.Sets;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class CurrencyServiceGetListByIdsTest extends CurrencyServiceBaseTest {

    /**
     * Test empty
     *
     * @throws Exception exception
     */
    @Test
    public void testEmpty() throws Exception {
        Set<Integer> categoryIds = Sets.newSet(1, 2);

        List<CurrencyEntity> entityList = Collections.emptyList();

        doReturn(entityList).when(currencyDao).getList(categoryIds);

        List<CurrencyDto> expected = Collections.emptyList();
        List<CurrencyDto> actual = service.getList(categoryIds);
        assertEntityListEquals(expected, actual);

        verify(currencyDao).getList(categoryIds);

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        Set<Integer> categoryIds = Sets.newSet(1, 2);

        List<CurrencyEntity> entityList = Arrays.asList(
                new CurrencyEntity(1, "01", "category1", 0),
                new CurrencyEntity(2, "02", "category2", 2)
        );

        doReturn(entityList).when(currencyDao).getList(categoryIds);

        List<CurrencyDto> expected = Arrays.asList(
                new CurrencyDto(1, "01", "category1", 0),
                new CurrencyDto(2, "02", "category2", 2)
        );
        List<CurrencyDto> actual = service.getList(categoryIds);
        assertEntityListEquals(expected, actual);

        verify(currencyDao).getList(categoryIds);

        verifyNoMoreDaoInteractions();
    }

}
