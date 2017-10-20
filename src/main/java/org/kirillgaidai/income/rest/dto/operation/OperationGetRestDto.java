package org.kirillgaidai.income.rest.dto.operation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.kirillgaidai.income.rest.dto.ISerialGetRestDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public class OperationGetRestDto extends OperationUpdateRestDto implements ISerialGetRestDto {

    @JsonProperty
    @NotNull
    @Size(min = 1, max = 250)
    private String accountTitle;
    @JsonProperty
    @NotNull
    @Size(min = 1, max = 250)
    private String categoryTitle;

    public OperationGetRestDto() {
    }

    public OperationGetRestDto(
            Integer id, Integer accountId, String accountTitle, Integer categoryId, String categoryTitle,
            LocalDate day, BigDecimal amount, String note) {
        super(id, accountId, categoryId, day, amount, note);
        this.accountTitle = accountTitle;
        this.categoryTitle = categoryTitle;
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
