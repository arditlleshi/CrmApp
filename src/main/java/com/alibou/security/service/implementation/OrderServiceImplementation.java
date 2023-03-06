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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                () -> new UsernameNotFoundException("User not found with id: " + orderDto.getUserId())));
        order.setClient(clientRepository.findById(orderDto.getClientId()).orElseThrow(
                () -> new UsernameNotFoundException("Client not found with id: " + orderDto.getClientId())));
        orderRepository.save(order);
        return convertToResponseDto(order);
    }

    @Override
    public Optional<OrderResponseDto> findById(Integer id) {
        return Optional.ofNullable(orderRepository.findById(id)
                .map(this::convertToResponseDto).orElseThrow(() ->
                        new IllegalStateException("Order not found with id: " + id)));
    }

    @Override
    public List<OrderResponseDto> findAll() {
        List<Order> orders = orderRepository.findAll();
        return convertToResponseDto(orders);
    }

    @Override
    public OrderResponseDto create(OrderDto orderDto, UserDetails userDetails) throws IllegalAccessException {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Client client = clientRepository.findById(orderDto.getClientId()).orElseThrow();
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
    public Optional<OrderResponseDto> findById(Integer id, UserDetails userDetails) throws IllegalAccessException {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Order not found with id: " + id)
        );
        if (!order.getUser().equals(user)){
            throw new IllegalAccessException("You can't view this order!");
        }
        return Optional.of(convertToResponseDto(order));
    }

    @Override
    public List<OrderResponseDto> findAll(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        List<Order> orders = orderRepository.findAllByUser(user);
        return convertToResponseDto(orders);
    }

    private OrderResponseDto convertToResponseDto(Order order){
        return mapper.map(order, OrderResponseDto.class);
    }
    private List<OrderResponseDto> convertToResponseDto(List<Order> orders){
        List<OrderResponseDto> orderResponseDtoList = new ArrayList<>();
        for (Order order : orders){
            OrderResponseDto orderResponseDto = new OrderResponseDto();
            orderResponseDto.setId(order.getId());
            orderResponseDto.setAmount(order.getAmount());
            orderResponseDto.setUserId(order.getUser().getId());
            orderResponseDto.setClientId(order.getClient().getId());
            orderResponseDtoList.add(orderResponseDto);
        }
        return orderResponseDtoList;
    }
}
