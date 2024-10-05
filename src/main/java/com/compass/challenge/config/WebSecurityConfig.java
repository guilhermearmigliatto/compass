package com.compass.challenge.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	protected void configure(HttpSecurity http) throws Exception {

		http.httpBasic()
				.and()
				.authorizeRequests()
				.anyRequest().permitAll()  // Permitir todas as requisições
				.and()
				// Disable frameOptions to allow h2-console
				.headers().frameOptions().disable()
				.and()
				// Disable CSRF (Cross-Site Request Forgery)
				.csrf().disable();
	}
	
}
