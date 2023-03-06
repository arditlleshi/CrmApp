package com.alibou.security.repository;

import com.alibou.security.model.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, Integer> {
    @Query("SELECT op FROM order_products op INNER JOIN orders o ON op.order.id = o.id WHERE o.user.id = :userId")
    List<OrderProduct> findByUserId(@Param("userId") Integer userId);
}
