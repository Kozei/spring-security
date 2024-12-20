package com.demo.demo.api;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.demo.domain.AppUser;
import com.demo.demo.dto.AuthResponseDto;
import com.demo.demo.dto.RegisterDto;
import com.demo.demo.mapper.Mapper;
import com.demo.demo.security.service.UserPrincipal;
import com.demo.demo.service.UserService;
import com.demo.demo.util.JwtUtil;

@RestController
public class UserResource {

    private static final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final UserService userService;
    private final Mapper<RegisterDto, AppUser> signUpRequestMapper;
    private final JwtUtil jwtUtil;

    public UserResource(UserService userService, Mapper<RegisterDto, AppUser> signUpRequestMapper, JwtUtil jwtUtil) {
        this.userService = userService;
        this.signUpRequestMapper = signUpRequestMapper;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/public/register")
    public ResponseEntity<AuthResponseDto> signUp(@RequestBody @Valid RegisterDto registerDto) {
        var appUser = signUpRequestMapper.map(registerDto);
        userService.saveUser(appUser);

        var userPrincipal = new UserPrincipal(appUser);
        var token = jwtUtil.generateToken(userPrincipal);
        var authResponseDto = new AuthResponseDto(token);

        return ResponseEntity.ok(authResponseDto);
    }

    @PostMapping(value = "/public/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<AuthResponseDto> login(@RequestParam String username) {
        log.info("Login user: {}", username);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userPrincipal = (UserDetails) authentication.getPrincipal();
        var token = jwtUtil.generateToken(userPrincipal);
        var authResponseDto = new AuthResponseDto(token);

        return ResponseEntity.ok(authResponseDto);
    }

    @GetMapping("/private/resource")
    public String getResource() {
        return "Accessing protected resource";
    }

    @GetMapping("/private/resource2")
    public String getResource2() {
        return "Accessing protected resource2";
    }
}
