package com.demo.demo.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.demo.demo.domain.AppUser;
import com.demo.demo.dto.SignUpRequestDto;
import com.demo.demo.mapper.Mapper;

@Component
public class UserMapperImpl implements Mapper<SignUpRequestDto, AppUser> {

    private final ModelMapper modelMapper;

    public UserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public AppUser map(SignUpRequestDto signUpRequestDto) {
        return modelMapper.map(signUpRequestDto, AppUser.class);
    }

    @Override
    public SignUpRequestDto mapReverse(AppUser appUser) {
        return modelMapper.map(appUser, SignUpRequestDto.class);
    }
}
