package org.kirillgaidai.income.rest.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.kirillgaidai.income.rest.dto.IGenericGetRestDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AccountGetRestDto extends AccountUpdateRestDto implements IGenericGetRestDto {

    @JsonProperty
    @NotNull
    @Size(min = 3, max = 3)
    private String currencyCode;
    @JsonProperty
    @NotNull
    @Size(min = 1, max = 250)
    private String currencyTitle;

    public AccountGetRestDto() {
    }

    public AccountGetRestDto(
            Integer id, Integer currencyId, String currencyCode, String currencyTitle, String sort, String title) {
        super(id, currencyId, sort, title);
        this.currencyCode = currencyCode;
        this.currencyTitle = currencyTitle;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyTitle() {
        return currencyTitle;
    }

    public void setCurrencyTitle(String currencyTitle) {
        this.currencyTitle = currencyTitle;
    }

}
