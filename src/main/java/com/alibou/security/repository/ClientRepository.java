package com.alibou.security.repository;

import com.alibou.security.model.Client;
import com.alibou.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer>, JpaSpecificationExecutor<Client> {
    Optional<Client> findByEmail(String email);
    List<Client> findAllByUser(User user);
    Page<Client> findAllByUser(User user, Pageable pageable);
}