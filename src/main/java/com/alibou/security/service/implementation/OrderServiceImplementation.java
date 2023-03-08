package com.alibou.security.service.implementation;

import com.alibou.security.dto.OrderDto;
import com.alibou.security.dto.OrderResponseDto;
import com.alibou.security.model.Client;
import com.alibou.security.model.Order;
import com.alibou.security.model.User;
import com.alibou.security.repository.ClientRepository;
import com.alibou.security.repository.OrderRepository;
import com.alibou.security.repository.UserRepository;
import com.alibou.security.service.OrderService;
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
    private final ClientRepository clientRepository;
    private final ModelMapper mapper;
    @Override
    public OrderResponseDto create(OrderDto orderDto) {
        Order order = new Order();
        order.setAmount(0.0);
        order.setUser(userRepository.findById(orderDto.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("User not found with id: " + orderDto.getUserId())));
        order.setClient(clientRepository.findById(orderDto.getClientId()).orElseThrow(
                () -> new EntityNotFoundException("Client not found with id: " + orderDto.getClientId())));
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
    public OrderResponseDto create(OrderDto orderDto, UserDetails userDetails) throws IllegalAccessException {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new EntityNotFoundException("User not found with email: " + userDetails.getUsername())
        );
        Client client = clientRepository.findById(orderDto.getClientId()).orElseThrow(
                () -> new EntityNotFoundException("Client not found with id: " + orderDto.getClientId())
        );
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
    public OrderResponseDto findById(Integer id, UserDetails userDetails) throws IllegalAccessException {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new EntityNotFoundException("User not found with email: " + userDetails.getUsername())
        );
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Order not found with id: " + id)
        );
        if (!order.getUser().equals(user)){
            throw new IllegalAccessException("You can't view this order!");
        }
        return convertToResponseDto(order);
    }

    @Override
    public List<OrderResponseDto> findAll(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new EntityNotFoundException("User not found with email: " + userDetails.getUsername())
        );
        List<Order> orders = orderRepository.findAllByUser(user);
        return convertToResponseDto(orders);
    }

    @Override
    public Page<OrderResponseDto> findAll(Integer pageNumber, Integer pageSize, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new EntityNotFoundException("User not found with email: " + userDetails.getUsername())
        );
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
