package com.crm.security.service.implementation;

import com.crm.security.model.Role;
import com.crm.security.repository.RoleRepository;
import com.crm.security.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImplementation implements RoleService {
    
    private final RoleRepository roleRepository;
    
    @Transactional
    @Override
    public Role create(Role role){
        return roleRepository.save(role);
    }
}
