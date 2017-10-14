package org.kirillgaidai.income.rest.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.kirillgaidai.income.rest.dto.IGenericUpdateRestDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class AccountUpdateRestDto extends AccountCreateRestDto implements IGenericUpdateRestDto {

    @JsonProperty
    @NotNull
    @Min(1)
    private Integer id;

    public AccountUpdateRestDto() {
    }

    public AccountUpdateRestDto(Integer id, Integer currencyId, String sort, String title) {
        super(currencyId, sort, title);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
