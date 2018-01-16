package org.kirillgaidai.income.config;

import org.kirillgaidai.income.security.TokenAuthenticationFailureHandler;
import org.kirillgaidai.income.security.TokenAuthenticationProcessingFilter;
import org.kirillgaidai.income.security.TokenAuthenticationProvider;
import org.kirillgaidai.income.security.TokenAuthenticationSuccessHandler;
import org.kirillgaidai.income.service.intf.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;

@EnableWebSecurity
@Configuration
@ComponentScan("org.kirillgaidai.income.security")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private IUserService userService;

    @Autowired
    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.requiresChannel().anyRequest().requiresSecure();
        http.authorizeRequests().antMatchers("/", "/favicon.ico", "/login", "/css/**", "/js/**")
                .permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilterBefore(tokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
        http.formLogin().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(tokenAuthenticationProvider());
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    @Bean
    public AuthenticationProvider tokenAuthenticationProvider() {
        return new TokenAuthenticationProvider(userService);
    }

    @Bean
    public Filter tokenAuthenticationProcessingFilter() throws Exception {
        TokenAuthenticationProcessingFilter filter = new TokenAuthenticationProcessingFilter("/rest/**");
        filter.setAuthenticationSuccessHandler(new TokenAuthenticationSuccessHandler());
        filter.setAuthenticationFailureHandler(new TokenAuthenticationFailureHandler());
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

}
