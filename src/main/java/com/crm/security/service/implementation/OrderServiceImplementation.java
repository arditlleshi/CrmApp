package com.crm.security.service.implementation;

import com.crm.security.dto.OrderDto;
import com.crm.security.dto.OrderResponseDto;
import com.crm.security.exception.ClientNotFoundException;
import com.crm.security.exception.UserNotFoundException;
import com.crm.security.model.Client;
import com.crm.security.model.Order;
import com.crm.security.model.User;
import com.crm.security.repository.OrderRepository;
import com.crm.security.repository.UserRepository;
import com.crm.security.service.ClientService;
import com.crm.security.service.OrderService;
import com.crm.security.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImplementation implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;
    private final UserService userService;
    private final ClientService clientService;
    @Override
    public OrderResponseDto create(OrderDto orderDto) throws UserNotFoundException, ClientNotFoundException {
        Order order = new Order();
        order.setAmount(0.0);
        order.setUser(userRepository.findById(orderDto.getUserId()).orElseThrow(
                () -> new UserNotFoundException("User not found with id: " + orderDto.getUserId())));
        order.setClient(clientService.findClientByIdOrThrowException(orderDto.getClientId()));
        orderRepository.save(order);
        return convertToResponseDto(order);
    }

    @Override
    public OrderResponseDto findById(Integer id) {
        return orderRepository.findById(id)
                .map(this::convertToResponseDto).orElseThrow(() ->
                        new IllegalStateException("Order not found with id: " + id));
    }

    @Override
    public List<OrderResponseDto> findAll() {
        List<Order> orders = orderRepository.findAll();
        return convertToResponseDto(orders);
    }

    @Override
    public Page<OrderResponseDto> findAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Order> orders = orderRepository.findAll(pageable);
        return convertToResponseDto(orders);
    }

    @Override
    public OrderResponseDto create(OrderDto orderDto, UserDetails userDetails) throws IllegalAccessException, UserNotFoundException, ClientNotFoundException {
        User user = userService.findUserByEmailOrThrowException(userDetails);
        Client client = clientService.findClientByIdOrThrowException(orderDto.getClientId());
        Order order = new Order();
        if (!client.getUser().equals(user)){
            throw new IllegalAccessException("You can't assign this client to the order!");
        }
        order.setUser(user);
        order.setClient(client);
        order.setAmount(0.0);
        orderRepository.save(order);
        return convertToResponseDto(order);
    }

    @Override
    public OrderResponseDto findById(Integer id, UserDetails userDetails) throws IllegalAccessException, UserNotFoundException {
        User user = userService.findUserByEmailOrThrowException(userDetails);
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Order not found with id: " + id)
        );
        if (!order.getUser().equals(user)){
            throw new IllegalAccessException("You can't view this order!");
        }
        return convertToResponseDto(order);
    }

    @Override
    public List<OrderResponseDto> findAll(UserDetails userDetails) throws UserNotFoundException {
        User user = userService.findUserByEmailOrThrowException(userDetails);
        List<Order> orders = orderRepository.findAllByUser(user);
        return convertToResponseDto(orders);
    }

    @Override
    public Page<OrderResponseDto> findAll(Integer pageNumber, Integer pageSize, UserDetails userDetails) throws UserNotFoundException {
        User user = userService.findUserByEmailOrThrowException(userDetails);
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Order> orders = orderRepository.findAllByUser(user, pageable);
        return convertToResponseDto(orders);
    }

    private OrderResponseDto convertToResponseDto(Order order){
        return mapper.map(order, OrderResponseDto.class);
    }
    private List<OrderResponseDto> convertToResponseDto(List<Order> orders){
        List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();
        for (Order order : orders){
            orderResponseDtoList.add(convertToResponseDto(order));
        }
        return orderResponseDtoList;
    }
    private Page<OrderResponseDto> convertToResponseDto(Page<Order> orders){
        List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();
        for (Order order : orders){
            orderResponseDtoList.add(convertToResponseDto(order));
        }
        return new PageImpl<>(orderResponseDtoList, orders.getPageable(), orders.getTotalElements());
    }
}
