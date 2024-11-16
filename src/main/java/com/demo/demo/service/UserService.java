package com.demo.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.demo.demo.domain.AppUser;

@Service
public interface UserService {

    AppUser saveUser(AppUser user);
    void deleteUser(String username);

    AppUser getUserByUsername(String username);
    List<AppUser> getAllUsers();

    AppUser addUserAuthority(AppUser user, String authority);
    AppUser deleteUserAuthority(String username, String authority);
}
