package org.kirillgaidai.income.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public TokenAuthenticationProcessingFilter(String... defaultFilterProcessesUrls) {
        super(new OrRequestMatcher(Stream.of(defaultFilterProcessesUrls)
                .map(AntPathRequestMatcher::new).collect(Collectors.toList())));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        return getAuthenticationManager().authenticate(new TokenAuthenticationToken(request.getHeader("Token"), false));
    }

}
