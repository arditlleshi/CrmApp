package com.crm.security.service;

import com.crm.security.dto.OrderProductDto;
import com.crm.security.dto.OrderProductResponseDto;
import com.crm.security.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface OrderProductService {
    OrderProductResponseDto create(OrderProductDto orderProductDto, UserDetails userDetails) throws IllegalAccessException, UserNotFoundException;
    OrderProductResponseDto findById(Integer id, UserDetails userDetails) throws IllegalAccessException, UserNotFoundException;
    List<OrderProductResponseDto> findAll(UserDetails userDetails) throws UserNotFoundException;
    Page<OrderProductResponseDto> findAll(Integer pageNumber, Integer pageSize, UserDetails userDetails) throws UserNotFoundException;
}