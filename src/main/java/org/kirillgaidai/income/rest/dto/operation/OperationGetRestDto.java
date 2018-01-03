package org.kirillgaidai.income.rest.dto.operation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.kirillgaidai.income.rest.dto.ISerialGetRestDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public class OperationGetRestDto extends OperationCreateRestDto implements ISerialGetRestDto {

    @JsonProperty
    @NotNull
    @Min(1)
    private Integer id;
    @JsonProperty
    @NotEmpty
    @Size(min = 1, max = 250)
    private String accountTitle;
    @JsonProperty
    @NotEmpty
    @Size(min = 1, max = 250)
    private String categoryTitle;

    public OperationGetRestDto() {
    }

    public OperationGetRestDto(
            Integer id, Integer accountId, String accountTitle, Integer categoryId, String categoryTitle,
            LocalDate day, BigDecimal amount, String note) {
        super(accountId, categoryId, day, amount, note);
        this.id = id;
        this.accountTitle = accountTitle;
        this.categoryTitle = categoryTitle;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountTitle() {
        return accountTitle;
    }

    public void setAccountTitle(String accountTitle) {
        this.accountTitle = accountTitle;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

}
