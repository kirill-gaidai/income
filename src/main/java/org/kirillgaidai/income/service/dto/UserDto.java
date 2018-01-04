package org.kirillgaidai.income.service.dto;

import java.time.LocalDateTime;

public class UserDto implements ISerialDto {

    private Integer id;
    private String login;
    private String password;
    private Boolean admin;
    private Boolean blocked;
    private String token;
    private LocalDateTime expires;

    public UserDto() {
    }

    public UserDto(
            Integer id, String login, String password, Boolean admin, Boolean blocked,
            String token, LocalDateTime expires) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.admin = admin;
        this.blocked = blocked;
        this.token = token;
        this.expires = expires;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
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

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getBlocked() {
        return blocked;
    }

    public void setBlocked(Boolean blocked) {
        this.blocked = blocked;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }

}
