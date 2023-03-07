package com.alibou.security.repository;

import com.alibou.security.model.Order;
import com.alibou.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByUser(User user);
    Page<Order> findAllByUser(User user, Pageable pageable);
}
