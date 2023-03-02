package com.alibou.security.service.implementation;

import com.alibou.security.model.Role;
import com.alibou.security.repository.RoleRepository;
import com.alibou.security.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImplementation implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public Role create(Role role) {
        return roleRepository.save(role);
    }
}
