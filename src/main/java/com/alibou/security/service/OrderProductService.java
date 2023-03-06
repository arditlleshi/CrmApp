package com.alibou.security.service;

import com.alibou.security.dto.OrderProductDto;
import com.alibou.security.dto.OrderProductResponseDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface OrderProductService {
    OrderProductResponseDto create(OrderProductDto orderProductDto);
    Optional<OrderProductResponseDto> findById(Integer id);
    List<OrderProductResponseDto> findAll();
    OrderProductResponseDto create(OrderProductDto orderProductDto, UserDetails userDetails) throws IllegalAccessException;
    Optional<OrderProductResponseDto> findById(Integer id, UserDetails userDetails) throws IllegalAccessException;
    List<OrderProductResponseDto> findAll(UserDetails userDetails);
}
