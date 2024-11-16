package com.demo.demo.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.demo.domain.Authority;
import com.demo.demo.repository.AuthorityRepository;
import com.demo.demo.service.AuthorityService;

@Service
@Transactional
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    public AuthorityServiceImpl(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Override
    public Authority saveAuthority(Authority authority) {
        return authorityRepository.save(authority);
    }

    @Override
    public void deleteAuthority(String authorityName) {
        Authority authority = authorityRepository.findByAuthorityName(authorityName);
        authorityRepository.delete(authority);
    }

    @Override
    public Authority getAuthority(String authorityName) {
        return authorityRepository.findByAuthorityName(authorityName);
    }

    @Override
    public List<Authority> getAuthorities() {
        return authorityRepository.findAll();
    }

}
