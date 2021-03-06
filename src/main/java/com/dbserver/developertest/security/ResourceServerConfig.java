package com.dbserver.developertest.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.POST, "/v1/hungry-professional").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/restaurant").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/vote").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/vote").permitAll();

        http.headers().frameOptions().disable();
    }

}
