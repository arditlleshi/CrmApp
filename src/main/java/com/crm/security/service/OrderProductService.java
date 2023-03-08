package com.crm.security.service;

import com.crm.security.dto.OrderProductDto;
import com.crm.security.dto.OrderProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface OrderProductService {
    OrderProductResponseDto create(OrderProductDto orderProductDto);
    OrderProductResponseDto findById(Integer id);
    List<OrderProductResponseDto> findAll();
    Page<OrderProductResponseDto> findAll(Integer pageNumber, Integer pageSize);
    OrderProductResponseDto create(OrderProductDto orderProductDto, UserDetails userDetails) throws IllegalAccessException;
    OrderProductResponseDto findById(Integer id, UserDetails userDetails) throws IllegalAccessException;
    List<OrderProductResponseDto> findAll(UserDetails userDetails);
    Page<OrderProductResponseDto> findAll(Integer pageNumber, Integer pageSize, UserDetails userDetails);
}
