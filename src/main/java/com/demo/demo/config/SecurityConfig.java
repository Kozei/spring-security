package com.demo.demo.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.demo.demo.security.filter.CustomAuthenticationFilter;
import com.demo.demo.security.manager.CustomAuthenticationManager;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class SecurityConfig {

    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter(CustomAuthenticationManager customAuthenticationManager) {
        return new CustomAuthenticationFilter(customAuthenticationManager);
    }

    @Bean
    public SecurityFilterChain customSecurityFilterChain(HttpSecurity http, CustomAuthenticationManager customAuthenticationManager) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                //.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAt(customAuthenticationFilter(customAuthenticationManager), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST,"/sign-up").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }

    /**
     * Prevent the CustomAuthenticationFilter to be registered with Servlet container.
     * The filter is already registered in Spring Security Chain.
     * @param filter
     * @return
     */
    @Bean
    public FilterRegistrationBean<CustomAuthenticationFilter> customAuthenticationFilterRegistration(CustomAuthenticationFilter filter) {
        FilterRegistrationBean<CustomAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}