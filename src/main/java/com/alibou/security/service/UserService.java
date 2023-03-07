package com.alibou.security.service;

import com.alibou.security.dto.AuthenticationResponseDto;
import com.alibou.security.dto.LoginRequestDto;
import com.alibou.security.dto.UserRegisterDto;
import com.alibou.security.dto.UserResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    UserResponseDto create(UserRegisterDto userRegisterDto);
    UserResponseDto findById(Integer id);
    List<UserResponseDto> findAll();
    Page<UserResponseDto> findAll(Integer pageNumber, Integer pageSize);
    UserResponseDto update(Integer id, UserResponseDto userResponseDto);
    AuthenticationResponseDto authenticate(LoginRequestDto request);
    String deleteById(Integer id);
    List<UserResponseDto> search(String query);
}