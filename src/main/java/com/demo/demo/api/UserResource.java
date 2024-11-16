package com.demo.demo.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.demo.domain.AppUser;
import com.demo.demo.domain.Authority;
import com.demo.demo.service.AuthorityService;
import com.demo.demo.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class UserResource {

    private final UserService userService;
    private final AuthorityService authorityService;

    public UserResource(UserService userService, AuthorityService authorityService) {
        this.userService = userService;
        this.authorityService = authorityService;
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
