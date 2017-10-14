package org.kirillgaidai.income.rest.dto.operation;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.kirillgaidai.income.rest.dto.IGenericUpdateRestDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public class OperationUpdateRestDto extends OperationCreateRestDto implements IGenericUpdateRestDto {

    @JsonProperty
    @NotNull
    @Min(1)
    private Integer id;

    public OperationUpdateRestDto() {
    }

    public OperationUpdateRestDto(
            Integer id, Integer accountId, Integer categoryId, LocalDate day, BigDecimal amount, String note) {
        super(accountId, categoryId, day, amount, note);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
