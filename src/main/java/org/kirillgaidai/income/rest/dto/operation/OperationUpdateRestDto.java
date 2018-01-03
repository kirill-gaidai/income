package org.kirillgaidai.income.rest.dto.operation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.kirillgaidai.income.rest.dto.ISerialUpdateRestDto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class OperationUpdateRestDto implements ISerialUpdateRestDto {

    @JsonProperty
    @NotNull
    @Min(1)
    private Integer id;
    @JsonProperty
    @NotNull
    @Min(1)
    private Integer categoryId;
    @JsonProperty
    @NotNull
    @Digits(integer = 10, fraction = 4)
    private BigDecimal amount;
    @JsonProperty
    @NotNull
    @Size(max = 250)
    private String note;

    public OperationUpdateRestDto() {
    }

    public OperationUpdateRestDto(Integer id, Integer categoryId, BigDecimal amount, String note) {
        this.id = id;
        this.categoryId = categoryId;
        this.amount = amount;
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

}
