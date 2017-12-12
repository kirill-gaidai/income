package org.kirillgaidai.income.rest.dto.currency;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.kirillgaidai.income.rest.dto.IGenericCreateRestDto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CurrencyCreateRestDto implements IGenericCreateRestDto {

    @JsonProperty
    @NotEmpty
    @Size(min = 3, max = 3)
    private String code;
    @JsonProperty
    @NotEmpty
    @Size(min = 1, max = 250)
    private String title;
    @JsonProperty
    @NotNull
    @Min(0)
    @Max(4)
    private Integer accuracy;

    public CurrencyCreateRestDto() {
    }

    public CurrencyCreateRestDto(String code, String title, Integer accuracy) {
        this.code = code;
        this.title = title;
        this.accuracy = accuracy;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(Integer accuracy) {
        this.accuracy = accuracy;
    }

}
