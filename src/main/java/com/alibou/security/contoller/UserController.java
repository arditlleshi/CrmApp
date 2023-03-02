package com.alibou.security.contoller;

import com.alibou.security.dto.AuthenticationResponseDto;
import com.alibou.security.dto.LoginRequestDto;
import com.alibou.security.dto.UserRegisterDto;
import com.alibou.security.dto.UserResponseDto;
import com.alibou.security.service.implementation.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final UserServiceImplementation userServiceImplementation;
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRegisterDto request){
        return ResponseEntity.ok(userServiceImplementation.create(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody LoginRequestDto request){
        return ResponseEntity.ok(userServiceImplementation.authenticate(request));
    }
}
