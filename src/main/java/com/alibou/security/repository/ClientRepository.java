package com.alibou.security.repository;

import com.alibou.security.model.Client;
import com.alibou.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    Optional<Client> findByEmail(String email);
    Optional<Client> findByUser(User user);
    List<Client> findAllByUser(User user);
}
