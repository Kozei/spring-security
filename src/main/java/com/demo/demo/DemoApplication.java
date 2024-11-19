package com.demo.demo;

import java.util.ArrayList;
import java.util.Objects;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.web.context.WebApplicationContext;

import com.demo.demo.domain.AppUser;
import com.demo.demo.domain.Authority;
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

			AppUser user1 = AppUser.builder()
					.username("George")
					.password(passwordEncoder.encode("1234567"))
					.email("george@gmail.com")
					.authorities(new ArrayList<>() {
					})
					.build();

			AppUser user2 = AppUser.builder()
					.username("Nick")
					.password(passwordEncoder.encode("12345678"))
					.email("nick@gmail.com")
					.authorities(new ArrayList<>() {
					})
					.build();

			AppUser user3 = AppUser.builder()
					.username("Natalie")
					.password(passwordEncoder.encode("123456789"))
					.email("natalie@gmail.com")
					.authorities(new ArrayList<>() {
					})
					.build();

			AppUser appUser1 = userService.saveUser(user1);
			AppUser appUser2 = userService.saveUser(user2);
			AppUser appUser3 = userService.saveUser(user3);

			Authority auth1 = Authority.builder()
					.authorityName("ROLE_USER")
					.build();

			Authority auth2 = Authority.builder()
					.authorityName("ROLE_ADMIN")
					.build();

			Authority auth3 = Authority.builder()
					.authorityName("ROLE_MANAGER")
					.build();

			Authority authority1 = authorityService.saveAuthority(auth1);
			Authority authority2 = authorityService.saveAuthority(auth2);
			Authority authority3 = authorityService.saveAuthority(auth3);

			userService.addUserAuthority(appUser1, authority1.getAuthorityName());
			userService.addUserAuthority(appUser2, authority2.getAuthorityName());
			userService.addUserAuthority(appUser3, authority3.getAuthorityName());

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
