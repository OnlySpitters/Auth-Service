package com.service.auth.security.config;

import com.service.auth.security.AuthTokenFilter;
import com.service.auth.security.CustomUserDetailsService;
import com.service.auth.security.RestAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true
)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomUserDetailsService customUserDetailsService;
    private final AuthTokenFilter authTokenFilter;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
                .userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
        http = http.cors().and().csrf().disable();

        // Set session management to stateless
        http = http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and();

        // for h2 ui be able to load in frame
        http = http.headers().frameOptions().disable().and();

        // basic form login disabled
        http.formLogin().disable().httpBasic().disable();

        // Set unauthorized requests exception handler
        http = http.exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and();

        // Set permissions on endpoints
        http.authorizeRequests()
                .antMatchers("/",
                        "/error",
                        "/favicon.ico",
                        "/**/*.pdf",
                        "/**/*.png",
                        "/**/*.gif",
                        "/**/*.svg",
                        "/**/*.jpg",
                        "/**/*.jpeg",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js")
                .permitAll()
                // auth and h2
                .antMatchers("/v1/auth/**", "/h2/**")
                .permitAll()
                // swagger
                .antMatchers("/webjars", "/swagger-ui/**", "/api/v2/api-docs", "/v2/api-docs/**", "/swagger-resources/**")
                .permitAll()
                .anyRequest()
                .authenticated();

        // Add our custom Token based authentication filter
        http.addFilterBefore(
                authTokenFilter,
                UsernamePasswordAuthenticationFilter.class
        );
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}