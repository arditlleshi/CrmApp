package com.crm.security.repository;

import com.crm.security.model.Order;
import com.crm.security.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByUser(User user);

    Page<Order> findAllByUser(User user, Pageable pageable);
}
