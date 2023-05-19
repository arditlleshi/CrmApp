package com.crm.security.contoller;

import com.crm.security.dto.AuthenticationResponseDto;
import com.crm.security.dto.LoginRequestDto;
import com.crm.security.dto.UserRegisterDto;
import com.crm.security.dto.UserRegisterResponseDto;
import com.crm.security.exception.EmailAlreadyExistsException;
import com.crm.security.exception.UserNotFoundException;
import com.crm.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crm-app")
public class CreateAuthenticateUser {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<UserRegisterResponseDto> register(@RequestBody @Valid UserRegisterDto request) throws EmailAlreadyExistsException{
        return new ResponseEntity<>(userService.register(request), CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody @Valid LoginRequestDto request) throws UserNotFoundException{
        return new ResponseEntity<>(userService.authenticate(request), OK);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, UserNotFoundException{
        userService.refreshToken(request, response);
    }
}