package org.kirillgaidai.income.security;

import org.kirillgaidai.income.service.intf.IUserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class TokenAuthenticationProvider implements AuthenticationProvider {

    final private IUserService userService;

    public TokenAuthenticationProvider(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = authentication.getName();
        if (token == null) {
            throw new BadCredentialsException("Missing token");
        }
        if (!userService.isLoggedIn(token)) {
            throw new BadCredentialsException("Invalid token");
        }
        return new TokenAuthenticationToken(token, true);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(TokenAuthenticationToken.class);
    }

}
