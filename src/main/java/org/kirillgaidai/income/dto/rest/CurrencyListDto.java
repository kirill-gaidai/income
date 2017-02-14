package org.kirillgaidai.income.dto.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.kirillgaidai.income.dto.CurrencyDto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlRootElement(name = "currencies")
@XmlType(propOrder = {"currencyDtoList"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(value = {"currencies"})
public class CurrencyListDto {

    private List<CurrencyDto> currencyDtoList;

    @XmlElement(name = "currency")
    @JsonProperty(value = "currencies")
    public List<CurrencyDto> getCurrencyDtoList() {
        return currencyDtoList;
    }

    public void setCurrencyDtoList(List<CurrencyDto> currencyDtoList) {
        this.currencyDtoList = currencyDtoList;
    }

}
