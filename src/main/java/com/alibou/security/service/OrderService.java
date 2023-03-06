package com.alibou.security.service;

import com.alibou.security.dto.OrderDto;
import com.alibou.security.dto.OrderResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderResponseDto create(OrderDto orderDto);
    Optional<OrderResponseDto> findById(Integer id);
    List<OrderResponseDto> findAll();
    OrderResponseDto create(OrderDto orderDto, UserDetails userDetails) throws IllegalAccessException;
    Optional<OrderResponseDto> findById(Integer id, UserDetails userDetails) throws IllegalAccessException;
    List<OrderResponseDto> findAll(UserDetails userDetails);
}
