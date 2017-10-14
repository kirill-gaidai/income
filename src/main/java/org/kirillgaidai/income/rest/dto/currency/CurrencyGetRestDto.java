package org.kirillgaidai.income.rest.dto.currency;

import org.kirillgaidai.income.rest.dto.IGenericGetRestDto;

public class CurrencyGetRestDto extends CurrencyUpdateRestDto implements IGenericGetRestDto {

    public CurrencyGetRestDto() {
    }

    public CurrencyGetRestDto(Integer id, String code, String title, Integer accuracy) {
        super(id, code, title, accuracy);
    }

}
