package com.alibou.security.contoller;

import com.alibou.security.dto.AuthenticationResponseDto;
import com.alibou.security.dto.LoginRequestDto;
import com.alibou.security.dto.UserRegisterDto;
import com.alibou.security.dto.UserResponseDto;
import com.alibou.security.model.User;
import com.alibou.security.service.UserService;
import com.alibou.security.service.implementation.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRegisterDto request){
        return ResponseEntity.ok(userService.create(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody LoginRequestDto request){
        return ResponseEntity.ok(userService.authenticate(request));
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable("id") Integer id){
        Optional<UserResponseDto> userResponseDto = userService.findById(id);
        return userResponseDto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
//    @GetMapping
//    public ResponseEntity<List<UserResponseDto>> findAll(){
//        return userService.findAll().get();
//    }
}
