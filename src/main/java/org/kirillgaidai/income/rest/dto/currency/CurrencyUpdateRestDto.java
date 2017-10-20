package org.kirillgaidai.income.rest.dto.currency;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.kirillgaidai.income.rest.dto.ISerialUpdateRestDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CurrencyUpdateRestDto extends CurrencyCreateRestDto implements ISerialUpdateRestDto {

    @JsonProperty
    @NotNull
    @Min(1)
    private Integer id;

    public CurrencyUpdateRestDto() {
    }

    public CurrencyUpdateRestDto(Integer id, String code, String title, Integer accuracy) {
        super(code, title, accuracy);
        this.id = id;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

}
