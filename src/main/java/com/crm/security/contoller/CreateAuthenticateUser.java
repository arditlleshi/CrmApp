package com.crm.security.contoller;

import com.crm.security.dto.*;
import com.crm.security.exception.EmailAlreadyExistsException;
import com.crm.security.exception.UserNotFoundException;
import com.crm.security.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody @Valid LoginRequestDto request) throws UserNotFoundException{
        return new ResponseEntity<>(userService.authenticate(request), OK);
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<AuthenticationResponseDto> changePassword(@RequestBody @Valid ChangePasswordDto request, @AuthenticationPrincipal UserDetails userDetails) throws Exception{
        return new ResponseEntity<>(userService.changePassword(request, userDetails), OK);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, UserNotFoundException{
        userService.refreshToken(request, response);
    }
}