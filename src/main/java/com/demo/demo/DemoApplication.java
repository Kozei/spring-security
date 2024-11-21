package com.demo.demo;

import java.util.Objects;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.web.context.WebApplicationContext;

import com.demo.demo.service.AuthorityService;
import com.demo.demo.service.UserService;

@SpringBootApplication
public class DemoApplication {

	private final PasswordEncoder passwordEncoder;

	public DemoApplication(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userService, AuthorityService authorityService) {
		return args -> {
			//TODO: populate the database with data. Figure out why the password encoder during user password match shows invalid password match when database is populated here.
			//TODO: It is probably due to the nature of singleton type beans. Do more research. Print the application context beans.
		};
	}

	@Bean
	CommandLineRunner logServletContainerFilters(WebApplicationContext context) {
		return args -> {
			Objects.requireNonNull(context.getServletContext())
					.getFilterRegistrations()
					.forEach((name, filterRegistration) -> System.out.println("Filter registered with servlet container: " + name));

		};
	}

	@Bean
	public ApplicationRunner logSpringSecurityFilters(FilterChainProxy filterChainProxy) {
		return args -> {
			filterChainProxy.getFilterChains()
					.forEach(securityFilterChain -> securityFilterChain.getFilters()
							.forEach(filter -> System.out.println("Filter in Spring Security chain: " + filter.getClass().getName())));
		};
	}
}
