package com.crm.security.service;

import com.crm.security.dto.OrderDto;
import com.crm.security.dto.OrderResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

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
