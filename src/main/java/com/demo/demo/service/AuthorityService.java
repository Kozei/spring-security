package com.demo.demo.service;

import java.util.List;

import com.demo.demo.domain.Authority;

public interface AuthorityService {

    Authority saveAuthority(Authority authority);
    void deleteAuthority(String authorityName);

    Authority getAuthority(String authorityName);
    List<Authority> getAuthorities();
}
