package com.demo.demo;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.demo.demo.domain.AppUser;
import com.demo.demo.domain.Authority;
import com.demo.demo.service.AuthorityService;
import com.demo.demo.service.UserService;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


	@Bean
	CommandLineRunner run(UserService userService, AuthorityService authorityService) {
		return args -> {

			AppUser appUser1 = userService.saveUser(new AppUser(null,"George","123",new ArrayList<>()));
			AppUser appUser2 = userService.saveUser(new AppUser(null,"Nick","1234",new ArrayList<>()));
			AppUser appUser3 = userService.saveUser(new AppUser(null,"Natalie","12345",new ArrayList<>()));

			Authority authority1 = authorityService.saveAuthority(new Authority(null, "ROLE_USER",null));
			Authority authority2 = authorityService.saveAuthority(new Authority(null, "ROLE_ADMIN",null));
			Authority authority3 = authorityService.saveAuthority(new Authority(null, "ROLE_MANAGER",null));

			userService.addUserAuthority(appUser1, authority1.getAuthorityName());
			userService.addUserAuthority(appUser2, authority2.getAuthorityName());
			userService.addUserAuthority(appUser3, authority3.getAuthorityName());

		};
	}

}
