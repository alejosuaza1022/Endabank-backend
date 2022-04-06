package com.endava.endabank.security;

import com.endava.endabank.constants.Routes;
import com.endava.endabank.security.jwtAuthentication.JwtAuthenticationEntryPoint;
import com.endava.endabank.security.jwtAuthentication.JwtRequestFilter;
import com.endava.endabank.services.impl.UserAuthenticationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final JwtRequestFilter jwtRequestFilter;
    private final UserAuthenticationService userAuthentication;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public WebSecurityConfig(PasswordEncoder passwordEncoder, JwtRequestFilter jwtRequestFilter,
                             UserAuthenticationService userAuthentication, JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.passwordEncoder = passwordEncoder;
        this.jwtRequestFilter = jwtRequestFilter;
        this.userAuthentication = userAuthentication;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userAuthentication).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf().disable().cors().and()
                .authorizeRequests().antMatchers(HttpMethod.POST, Routes.API_ROUTE + Routes.LOGIN_ROUTE,
                        Routes.API_ROUTE + Routes.USERS_ROUTE).permitAll();
                httpSecurity.authorizeRequests().antMatchers(
                                HttpMethod.GET, Routes.API_ROUTE + Routes.USERS_ROUTE +
                                Routes.RESET_PASSWORD_ROUTE + "/**").permitAll();
        httpSecurity.authorizeRequests().antMatchers(
                HttpMethod.PUT, Routes.API_ROUTE +
                        Routes.USERS_ROUTE + Routes.RESET_PASSWORD_ROUTE + "/**").permitAll();
//        httpSecurity.authorizeRequests().anyRequest().authenticated().and()
//                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
//                .and().sessionManagement()
//                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
