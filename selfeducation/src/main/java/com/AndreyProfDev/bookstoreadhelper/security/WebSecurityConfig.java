package com.AndreyProfDev.bookstoreadhelper.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    private RESTAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private RESTAuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private RESTAuthenticationSuccessHandler authenticationSuccessHandler;

    @Value("${remember_me_token:bookremembermetoken}")
    private String rememberMeToken;

    private static final String DEF_USERS_BY_USERNAME_QUERY =
          "select username,password,enabled " +
                  "from users " +
                  "where username = ?";
    private static final String DEF_AUTHORITIES_BY_USERNAME_QUERY =
          "select username,authority " +
                  "from authorities " +
                  "where username = ?";

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Override
    public void configure(AuthenticationManagerBuilder auth)
          throws Exception {
    auth.jdbcAuthentication().dataSource(dataSource)
            .usersByUsernameQuery(DEF_USERS_BY_USERNAME_QUERY)
            .authoritiesByUsernameQuery(DEF_AUTHORITIES_BY_USERNAME_QUERY);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/rest/**").authenticated()
                .and().rememberMe()
                .tokenValiditySeconds(-1)
                .key(rememberMeToken);

        http
                .csrf()
                .disable();

        http
                .csrf()
                .disable();
        http
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint);
        http
                .formLogin()
                .successHandler(authenticationSuccessHandler);
        http
                .formLogin()
                .failureHandler(authenticationFailureHandler);
    }
}
