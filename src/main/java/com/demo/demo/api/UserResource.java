package com.demo.demo.api;

import java.util.List;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.demo.domain.AppUser;
import com.demo.demo.domain.Authority;
import com.demo.demo.dto.SignUpRequestDto;
import com.demo.demo.mapper.Mapper;
import com.demo.demo.service.AuthorityService;
import com.demo.demo.service.UserService;

@RestController
public class UserResource {

    private static final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final UserService userService;
    private final AuthorityService authorityService;
    private final Mapper<SignUpRequestDto, AppUser> signUpRequestMapper;


    public UserResource(UserService userService, AuthorityService authorityService, Mapper<SignUpRequestDto, AppUser> signUpRequestMapper) {
        this.userService = userService;
        this.authorityService = authorityService;
        this.signUpRequestMapper = signUpRequestMapper;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<Void> signUp(@RequestBody @Valid SignUpRequestDto signUpRequestDto) {
        AppUser appUser = signUpRequestMapper.map(signUpRequestDto);
        userService.saveUser(appUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/user")
    public ResponseEntity<AppUser> createUser(@RequestBody AppUser user) {
        log.info("Create user: {}", user);
        AppUser appUser = userService.saveUser(user);
        return ResponseEntity.ok(appUser);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<AppUser> deleteUser(@RequestParam String username) {
        log.info("delete user {}", username);
        AppUser appUser = userService.getUserByUsername(username);
        userService.deleteUser(username);
        return ResponseEntity.ok(appUser);
    }

    @GetMapping("/user")
    public ResponseEntity<AppUser> getUser(@RequestParam String username) {
        log.info("get user {}", username);
        AppUser appUser = userService.getUserByUsername(username);
        return ResponseEntity.ok(appUser);
    }

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>> getUsers() {
        log.info("get users");
        List<AppUser> appUsers = userService.getAllUsers();
        return ResponseEntity.ok(appUsers);
    }

    @PostMapping("/user/authority")
    public ResponseEntity<AppUser> addAuthorityToUser(@RequestParam String username, @RequestParam String authorityName) {
        log.info("add authority to user {}", username);
        AppUser appUser = userService.getUserByUsername(username);
        Authority authority = authorityService.getAuthority(authorityName);
        appUser.getAuthorities().add(authority);
        return ResponseEntity.ok(appUser);
    }

    @DeleteMapping("user/authority")
    public ResponseEntity<AppUser> removeAuthorityFromUser(@RequestParam String username, @RequestParam String authorityName) {
        log.info("remove authority from user {}", username);
        AppUser appUser = userService.getUserByUsername(username);
        Authority authority = authorityService.getAuthority(authorityName);
        appUser.getAuthorities().remove(authority);
        return ResponseEntity.ok(appUser);
    }

}
