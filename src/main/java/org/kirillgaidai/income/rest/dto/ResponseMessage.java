package org.kirillgaidai.income.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ResponseMessage {

    @JsonProperty
    @NotNull
    @Size(max = 250)
    private String message;

    public ResponseMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
