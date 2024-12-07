package com.demo.demo.security.config;

import static com.demo.demo.util.MessageConstants.PUBLIC_API;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.util.pattern.PathPatternParser;

import com.demo.demo.repository.UserRepository;
import com.demo.demo.security.filter.CapturePathFilter;
import com.demo.demo.security.authentication.AuthenticationFailureHandler;
import com.demo.demo.security.filter.JwtAuthenticationFilter;
import com.demo.demo.security.manager.RestAuthenticationManager;
import com.demo.demo.security.provider.RestAuthenticationProvider;
import com.demo.demo.security.service.JwtService;
import com.demo.demo.security.service.RestUserDetailsService;
import com.demo.demo.util.Resource;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain restSecurityFilterChain(HttpSecurity http,
                                                       RestAuthenticationManager restAuthenticationManager,
                                                       ObjectMapper objectMapper,
                                                       @Qualifier("pathPatternParser") PathPatternParser parser,
                                                       JwtService jwtService, RestUserDetailsService restUserDetailsService, RestAuthenticationProvider restAuthenticationProvider, Resource resource) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .exceptionHandling((exception) -> exception.authenticationEntryPoint(authenticationFailureHandler(objectMapper, parser)))
                .securityContext((securityContext) -> securityContext.requireExplicitSave(false))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterAt(jwtAuthenticationFilter(restAuthenticationManager, jwtService, restUserDetailsService,resource), UsernamePasswordAuthenticationFilter.class)
                .authenticationManager(restAuthenticationManager)
                .authenticationProvider(restAuthenticationProvider)
                .addFilterBefore(capturePathFilter(), JwtAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_API).permitAll()
                        .anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public RestUserDetailsService restUserDetailsService(UserRepository userRepository) {
        return new RestUserDetailsService(userRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestAuthenticationProvider restAuthenticationProvider(RestUserDetailsService restUserDetailsService,
                                                                 PasswordEncoder passwordEncoder) {
        return new RestAuthenticationProvider(restUserDetailsService, passwordEncoder);
    }

    @Bean
    public RestAuthenticationManager restAuthenticationManager(RestAuthenticationProvider restAuthenticationProvider) {
        return new RestAuthenticationManager(restAuthenticationProvider);
    }

    @Bean
    public Resource resource(@Qualifier("pathPatternParser")PathPatternParser parser) {
        return new Resource(parser);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(RestAuthenticationManager restAuthenticationManager,
                                                           JwtService jwtService,
                                                           RestUserDetailsService restUserDetailsService,
                                                           Resource resource) {
        return new JwtAuthenticationFilter(restAuthenticationManager, jwtService, restUserDetailsService, resource);
    }

    @Bean
    public CapturePathFilter capturePathFilter() {
        return new CapturePathFilter();
    }

    /**
     * Prevent the CustomAuthenticationFilter to be registered with Servlet container.
     * The filter is already registered in Spring Security Chain.
     * @param filter
     * @return
     */
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> customAuthenticationFilterRegistration(JwtAuthenticationFilter filter) {
        FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public AuthenticationEntryPoint authenticationFailureHandler(ObjectMapper objectMapper, @Qualifier("pathPatternParser") PathPatternParser parser) {
        return new AuthenticationFailureHandler(objectMapper, parser);
    }

}