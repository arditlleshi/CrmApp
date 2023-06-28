package com.crm.security;

import com.crm.security.model.Role;
import com.crm.security.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.crm.security.enums.RoleName.ROLE_ADMIN;
import static com.crm.security.enums.RoleName.ROLE_OPERATOR;

@SpringBootApplication
@RequiredArgsConstructor
public class Application implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Role roleAdmin = new Role();
        roleAdmin.setName(ROLE_ADMIN);
        Role roleOperator = new Role();
        roleOperator.setName(ROLE_OPERATOR);
        if (roleRepository.findAll().isEmpty()) {
            roleRepository.save(roleAdmin);
            roleRepository.save(roleOperator);
        }
    }
}