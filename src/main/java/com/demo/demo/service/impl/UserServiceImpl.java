package com.demo.demo.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.demo.domain.AppUser;
import com.demo.demo.domain.Authority;
import com.demo.demo.repository.AuthorityRepository;
import com.demo.demo.repository.UserRepository;
import com.demo.demo.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser saveUser(AppUser user) {
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(String username) {
        AppUser appUser = userRepository.findByUsername(username);
        userRepository.delete(appUser);
    }

    @Override
    public AppUser getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<AppUser> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public AppUser addUserAuthority(AppUser user, String authorityName) {
        AppUser appUser = userRepository.findByUsername(user.getUsername());
        Authority authority = authorityRepository.findByAuthorityName(authorityName);
        appUser.getAuthorities().add(authority);
        return appUser;
    }

    @Override
    public AppUser deleteUserAuthority(String username, String authorityName) {
        AppUser appUser = userRepository.findByUsername(username);
        Authority authority = authorityRepository.findByAuthorityName(authorityName);
        appUser.getAuthorities().remove(authority);
        return appUser;
    }
}
