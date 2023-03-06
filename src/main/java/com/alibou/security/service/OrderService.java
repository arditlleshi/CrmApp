package com.alibou.security.service;

import com.alibou.security.dto.OrderDto;
import com.alibou.security.dto.OrderResponseDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderResponseDto create(OrderDto orderDto);
    Optional<OrderResponseDto> findById(Integer id);
    List<OrderResponseDto> findAll();
}
