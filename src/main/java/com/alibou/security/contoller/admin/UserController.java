package com.alibou.security.contoller.admin;

import com.alibou.security.dto.AuthenticationResponseDto;
import com.alibou.security.dto.LoginRequestDto;
import com.alibou.security.dto.UserRegisterDto;
import com.alibou.security.dto.UserResponseDto;
import com.alibou.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/all/create")
    public ResponseEntity<UserResponseDto> register(@RequestBody UserRegisterDto request){
        return new ResponseEntity<>(userService.create(request), CREATED);
    }
    @PostMapping("/all/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody LoginRequestDto request){
        return new ResponseEntity<>(userService.authenticate(request), OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable("id") Integer id){
        UserResponseDto userResponseDto = userService.findById(id);
        return new ResponseEntity<>(userResponseDto, OK);
    }
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll(){
        List<UserResponseDto> userResponseDtoList = userService.findAll();
        return new ResponseEntity<>(userResponseDtoList, OK);
    }
    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<UserResponseDto>> findAll(@PathVariable Integer pageNumber, @PathVariable Integer pageSize){
        Page<UserResponseDto> userResponseDtoPage = userService.findAll(pageNumber, pageSize);
        return new ResponseEntity<>(userResponseDtoPage, OK);
    }
    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> search(@RequestParam("q") String query){
        return new ResponseEntity<>(userService.search(query), OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable("id") Integer id, @RequestBody UserResponseDto userResponseDto){
        return new ResponseEntity<>(userService.update(id, userResponseDto), OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Integer id){
        return new ResponseEntity<>(userService.deleteById(id), OK);
    }
}
