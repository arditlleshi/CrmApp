package com.alibou.security.service;

import com.alibou.security.dto.AuthenticationResponseDto;
import com.alibou.security.dto.LoginRequestDto;
import com.alibou.security.dto.UserRegisterDto;
import com.alibou.security.dto.UserResponseDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponseDto create(UserRegisterDto userRegisterDto);
    Optional<UserResponseDto> findById(Integer id);
    List<UserResponseDto> findAll();
    UserResponseDto update(UserRegisterDto userRegisterDto);
    AuthenticationResponseDto authenticate(LoginRequestDto request);
}
