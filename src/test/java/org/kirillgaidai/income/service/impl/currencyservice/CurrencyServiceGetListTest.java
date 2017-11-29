package org.kirillgaidai.income.service.impl.currencyservice;

import org.junit.Test;
import org.kirillgaidai.income.dao.entity.CurrencyEntity;
import org.kirillgaidai.income.service.dto.CurrencyDto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.kirillgaidai.income.utils.TestUtils.assertEntityListEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class CurrencyServiceGetListTest extends CurrencyServiceBaseTest {

    /**
     * Test empty
     *
     * @throws Exception exception
     */
    @Test
    public void testEmpty() throws Exception {
        List<CurrencyEntity> currencyEntityList = Collections.emptyList();

        doReturn(currencyEntityList).when(currencyDao).getList();

        List<CurrencyDto> expected = Collections.emptyList();
        List<CurrencyDto> actual = service.getList();
        assertEntityListEquals(expected, actual);

        verify(currencyDao).getList();

        verifyNoMoreDaoInteractions();
    }

    /**
     * Test successful
     *
     * @throws Exception exception
     */
    @Test
    public void testSuccessful() throws Exception {
        List<CurrencyEntity> entityList = Arrays.asList(
                new CurrencyEntity(1, "01", "category1", 0),
                new CurrencyEntity(2, "02", "category2", 2),
                new CurrencyEntity(3, "03", "category3", 4)
        );

        doReturn(entityList).when(currencyDao).getList();

        List<CurrencyDto> expected = Arrays.asList(
                new CurrencyDto(1, "01", "category1", 0),
                new CurrencyDto(2, "02", "category2", 2),
                new CurrencyDto(3, "03", "category3", 4)
        );
        List<CurrencyDto> actual = service.getList();
        assertEntityListEquals(expected, actual);

        verify(currencyDao).getList();

        verifyNoMoreDaoInteractions();
    }

}
