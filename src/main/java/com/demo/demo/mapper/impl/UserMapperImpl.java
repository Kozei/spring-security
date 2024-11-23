package com.demo.demo.mapper.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.demo.demo.domain.AppUser;
import com.demo.demo.dto.RegisterDto;
import com.demo.demo.mapper.Mapper;

@Component
public class UserMapperImpl implements Mapper<RegisterDto, AppUser> {

    private final ModelMapper modelMapper;

    public UserMapperImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public AppUser map(RegisterDto registerDto) {
        return modelMapper.map(registerDto, AppUser.class);
    }

    @Override
    public RegisterDto mapReverse(AppUser appUser) {
        return modelMapper.map(appUser, RegisterDto.class);
    }
}
