package com.crm.security.service;

import com.crm.security.dto.AuthenticationResponseDto;
import com.crm.security.dto.LoginRequestDto;
import com.crm.security.dto.UserRegisterDto;
import com.crm.security.dto.UserResponseDto;
import com.crm.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

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
    User findUserByEmailOrThrowException(UserDetails userDetails);
}