package com.crm.security.service;

import com.crm.security.dto.*;
import com.crm.security.exception.EmailAlreadyExistsException;
import com.crm.security.exception.UserNotFoundException;
import com.crm.security.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.List;

public interface UserService {

    UserRegisterResponseDto register(UserRegisterDto userRegisterDto) throws EmailAlreadyExistsException;

    UserResponseDto findById(Integer id) throws UserNotFoundException;

    List<UserResponseDto> findAll();

    Page<UserResponseDto> findAll(Integer pageNumber, Integer pageSize);

    UserResponseDto update(Integer id, UserUpdateDto userUpdateDto) throws UserNotFoundException, EmailAlreadyExistsException;

    AuthenticationResponseDto authenticate(LoginRequestDto request) throws UserNotFoundException;

    String deleteById(Integer id);

    List<UserResponseDto> search(String query);

    User findUserByEmailOrThrowException(UserDetails userDetails) throws UserNotFoundException;

    boolean isUserAdmin(User user);

    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, UserNotFoundException;

    AuthenticationResponseDto changePassword(ChangePasswordDto request, UserDetails userDetails) throws Exception;
}