package com.crm.security.service;

import com.crm.security.dto.OrderDto;
import com.crm.security.dto.OrderResponseDto;
import com.crm.security.exception.ClientNotFoundException;
import com.crm.security.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface OrderService {

    OrderResponseDto create(OrderDto orderDto, UserDetails userDetails) throws IllegalAccessException, UserNotFoundException, ClientNotFoundException;

    OrderResponseDto findById(Integer id, UserDetails userDetails) throws IllegalAccessException, UserNotFoundException;

    List<OrderResponseDto> findAll(UserDetails userDetails) throws UserNotFoundException;

    Page<OrderResponseDto> findAll(Integer pageNumber, Integer pageSize, UserDetails userDetails) throws UserNotFoundException;
}
