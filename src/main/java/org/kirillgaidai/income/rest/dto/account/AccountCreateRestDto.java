package org.kirillgaidai.income.rest.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.kirillgaidai.income.rest.dto.IGenericCreateRestDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AccountCreateRestDto implements IGenericCreateRestDto {

    @JsonProperty
    @NotNull
    @Min(1)
    private Integer currencyId;
    @JsonProperty
    @NotNull
    @Size(min = 1, max = 10)
    private String sort;
    @JsonProperty
    @NotNull
    @Size(min = 1, max = 250)
    private String title;

    public AccountCreateRestDto() {
    }

    public AccountCreateRestDto(Integer currencyId, String sort, String title) {
        this.currencyId = currencyId;
        this.sort = sort;
        this.title = title;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
