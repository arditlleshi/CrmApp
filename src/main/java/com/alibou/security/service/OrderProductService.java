package com.alibou.security.service;

import com.alibou.security.dto.OrderProductDto;
import com.alibou.security.dto.OrderProductResponseDto;

import java.util.List;
import java.util.Optional;

public interface OrderProductService {
    OrderProductResponseDto create(OrderProductDto orderProductDto);
    Optional<OrderProductResponseDto> findById(Integer id);
    List<OrderProductResponseDto> findAll();
}
