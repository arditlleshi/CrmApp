package com.alibou.security.service;

import com.alibou.security.dto.OrderDto;
import com.alibou.security.dto.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    OrderResponseDto create(OrderDto orderDto);
    OrderResponseDto findById(Integer id);
    List<OrderResponseDto> findAll();
    Page<OrderResponseDto> findAll(Integer pageNumber, Integer pageSize);
    OrderResponseDto create(OrderDto orderDto, UserDetails userDetails) throws IllegalAccessException;
    OrderResponseDto findById(Integer id, UserDetails userDetails) throws IllegalAccessException;
    List<OrderResponseDto> findAll(UserDetails userDetails);
    Page<OrderResponseDto> findAll(Integer pageNumber, Integer pageSize, UserDetails userDetails);
}
