package com.crm.security.contoller;

import com.crm.security.dto.UserResponseDto;
import com.crm.security.dto.UserUpdateDto;
import com.crm.security.exception.EmailAlreadyExistsException;
import com.crm.security.exception.UserNotFoundException;
import com.crm.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crm-app/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable("id") Integer id) throws UserNotFoundException {
        UserResponseDto userResponseDto = userService.findById(id);
        return new ResponseEntity<>(userResponseDto, OK);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAll() {
        List<UserResponseDto> userResponseDtoList = userService.findAll();
        return new ResponseEntity<>(userResponseDtoList, OK);
    }

    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<UserResponseDto>> findAll(@PathVariable Integer pageNumber, @PathVariable Integer pageSize) {
        Page<UserResponseDto> userResponseDtoPage = userService.findAll(pageNumber, pageSize);
        return new ResponseEntity<>(userResponseDtoPage, OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> search(@RequestParam("q") String query) {
        return new ResponseEntity<>(userService.search(query), OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> update(@PathVariable("id") Integer id, @RequestBody @Valid UserUpdateDto userUpdateDto) throws UserNotFoundException, EmailAlreadyExistsException {
        return new ResponseEntity<>(userService.update(id, userUpdateDto), OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(userService.deleteById(id), OK);
    }
}
