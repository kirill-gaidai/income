package org.kirillgaidai.income.rest.dto.login;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;

public class LoginResponseRestDto {

    @JsonProperty
    @NotEmpty
    @Size(min = 36, max = 36)
    private String token;
    @JsonProperty
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private ZonedDateTime expires;

    public LoginResponseRestDto() {
    }

    public LoginResponseRestDto(String token, ZonedDateTime expires) {
        this.token = token;
        this.expires = expires;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ZonedDateTime getExpires() {
        return expires;
    }

    public void setExpires(ZonedDateTime expires) {
        this.expires = expires;
    }

}
