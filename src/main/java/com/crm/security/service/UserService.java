package com.crm.security.service;

import com.crm.security.dto.*;
import com.crm.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    UserRegisterResponseDto create(UserRegisterDto userRegisterDto);
    UserResponseDto findById(Integer id);
    List<UserResponseDto> findAll();
    Page<UserResponseDto> findAll(Integer pageNumber, Integer pageSize);
    UserResponseDto update(Integer id, UserUpdateDto userUpdateDto);
    AuthenticationResponseDto authenticate(LoginRequestDto request);
    String deleteById(Integer id);
    List<UserResponseDto> search(String query);
    User findUserByEmailOrThrowException(UserDetails userDetails);
}