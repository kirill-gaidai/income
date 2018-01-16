package org.kirillgaidai.income.rest.dto.login;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class LoginRequestRestDto {

    @JsonProperty
    @NotEmpty
    @Size(min = 1, max = 10)
    private String login;
    @JsonProperty
    @NotEmpty
    @Size(min = 1, max = 250)
    private String password;

    public LoginRequestRestDto() {
    }

    public LoginRequestRestDto(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
