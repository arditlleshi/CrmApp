package com.crm.security.contoller;

import com.crm.security.model.Role;
import com.crm.security.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crm-app/roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<Role> create(@RequestBody Role role){
        Role newRole = roleService.create(role);
        return new ResponseEntity<>(newRole, HttpStatus.CREATED);
    }
}
