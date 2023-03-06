package com.alibou.security.contoller.admin;

import com.alibou.security.dto.AuthenticationResponseDto;
import com.alibou.security.dto.LoginRequestDto;
import com.alibou.security.dto.UserRegisterDto;
import com.alibou.security.dto.UserResponseDto;
import com.alibou.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping
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
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll(){
        List<UserResponseDto> userResponseDtoList = userService.findAll();
        return ResponseEntity.ok(userResponseDtoList);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable("id") Integer id, @RequestBody UserResponseDto userResponseDto){
        return ResponseEntity.ok(userService.update(id, userResponseDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(userService.deleteById(id));
    }
}
