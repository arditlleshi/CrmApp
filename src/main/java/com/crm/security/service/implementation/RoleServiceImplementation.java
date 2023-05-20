package com.crm.security.service.implementation;

import com.crm.security.model.Role;
import com.crm.security.repository.RoleRepository;
import com.crm.security.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImplementation implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role create(Role role){
        return roleRepository.save(role);
    }
}
