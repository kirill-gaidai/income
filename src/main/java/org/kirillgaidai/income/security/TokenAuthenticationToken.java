package org.kirillgaidai.income.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.security.Principal;

public class TokenAuthenticationToken extends AbstractAuthenticationToken {

    private final Principal principal;

    public TokenAuthenticationToken(String token, boolean isAuthenticated) {
        super(null);
        setAuthenticated(isAuthenticated);
        this.principal = () -> token;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

}
