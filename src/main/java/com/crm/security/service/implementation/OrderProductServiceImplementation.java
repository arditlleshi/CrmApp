package com.crm.security.service.implementation;

import com.crm.security.dto.OrderProductDto;
import com.crm.security.dto.OrderProductResponseDto;
import com.crm.security.exception.UserNotFoundException;
import com.crm.security.model.Order;
import com.crm.security.model.OrderProduct;
import com.crm.security.model.Product;
import com.crm.security.model.User;
import com.crm.security.repository.OrderProductRepository;
import com.crm.security.repository.OrderRepository;
import com.crm.security.repository.ProductRepository;
import com.crm.security.repository.UserRepository;
import com.crm.security.service.OrderProductService;
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
public class OrderProductServiceImplementation implements OrderProductService {
    private final OrderProductRepository orderProductRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    public OrderProductResponseDto create(OrderProductDto orderProductDto) {
        OrderProduct orderProduct = new OrderProduct();
        Order order = orderRepository.findById(orderProductDto.getOrderId()).orElseThrow(
                () -> new EntityNotFoundException("Order not found with id: " + orderProductDto.getOrderId())
        );
        Product product = productRepository.findById(orderProductDto.getProductId()).orElseThrow(
                () -> new EntityNotFoundException("Product not found with id: " + orderProductDto.getProductId())
        );

        orderProduct.setOrder(order);
        Double orderAmount = (order.getAmount()) + (orderProductDto.getQuantity() * product.getPrice());
        order.setAmount(orderAmount);

        orderProduct.setProduct(product);
        orderProduct.setQuantity(orderProductDto.getQuantity());
        orderProduct.setAmount(orderProductDto.getQuantity() * product.getPrice());
        orderProductRepository.save(orderProduct);
        return convertToResponseDto(orderProduct);
    }

    @Override
    public OrderProductResponseDto findById(Integer id) {
        return orderProductRepository.findById(id)
                .map(this::convertToResponseDto).orElseThrow(() ->
                        new IllegalStateException("Order-Product not found with id: " + id));
    }

    @Override
    public List<OrderProductResponseDto> findAll() {
        List<OrderProduct> orderProducts = orderProductRepository.findAll();
        return convertToResponseDto(orderProducts);
    }

    @Override
    public Page<OrderProductResponseDto> findAll(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<OrderProduct> orderProducts = orderProductRepository.findAll(pageable);
        return convertToResponseDto(orderProducts);
    }

    @Override
    public OrderProductResponseDto create(OrderProductDto orderProductDto, UserDetails userDetails) throws IllegalAccessException {
        OrderProduct orderProduct = new OrderProduct();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new UserNotFoundException("User not found with email: " + userDetails.getUsername())
        );
        Order order = orderRepository.findById(orderProductDto.getOrderId()).orElseThrow(
                () -> new EntityNotFoundException("Order not found with id: " + orderProductDto.getOrderId())
        );
        Product product = productRepository.findById(orderProductDto.getProductId()).orElseThrow(
                () -> new EntityNotFoundException("Product not found with id: " + orderProductDto.getProductId())
        );
        if (!order.getUser().equals(user)){
            throw new IllegalAccessException("You can't add this order!");
        }
        orderProduct.setOrder(order);
        Double orderAmount = (order.getAmount()) + (orderProductDto.getQuantity() * product.getPrice());
        order.setAmount(orderAmount);

        orderProduct.setProduct(product);
        orderProduct.setQuantity(orderProductDto.getQuantity());
        orderProduct.setAmount(orderProductDto.getQuantity() * product.getPrice());
        orderProductRepository.save(orderProduct);
        return convertToResponseDto(orderProduct);
    }

    @Override
    public OrderProductResponseDto findById(Integer id, UserDetails userDetails) throws IllegalAccessException {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new UserNotFoundException("User not found with email: " + userDetails.getUsername())
        );
        OrderProduct orderProduct = orderProductRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Order not found with id: " + id)
        );
        if (!orderProduct.getOrder().getUser().equals(user)){
            throw new IllegalAccessException("You can't view this order!");
        }
        return convertToResponseDto(orderProduct);
    }

    @Override
    public List<OrderProductResponseDto> findAll(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new UserNotFoundException("User not found with email: " + userDetails.getUsername())
        );
        List<OrderProduct> orderProducts = orderProductRepository.findByUserId(user.getId());
        return convertToResponseDto(orderProducts);
    }

    @Override
    public Page<OrderProductResponseDto> findAll(Integer pageNumber, Integer pageSize, UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new UserNotFoundException("User not found with email: " + userDetails.getUsername())
        );
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<OrderProduct> orderProducts = orderProductRepository.findByUserId(user.getId(), pageable);
        return convertToResponseDto(orderProducts);
    }

    private OrderProductResponseDto convertToResponseDto(OrderProduct orderProduct){
        return mapper.map(orderProduct, OrderProductResponseDto.class);
    }
    private List<OrderProductResponseDto> convertToResponseDto(List<OrderProduct> orderProducts){
        List<OrderProductResponseDto> orderProductResponseDtoList = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts){
            orderProductResponseDtoList.add(convertToResponseDto(orderProduct));
        }
        return orderProductResponseDtoList;
    }
    private Page<OrderProductResponseDto> convertToResponseDto(Page<OrderProduct> orderProducts){
        List<OrderProductResponseDto> orderProductResponseDtoList = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts){
            orderProductResponseDtoList.add(convertToResponseDto(orderProduct));
        }
        return new PageImpl<>(orderProductResponseDtoList, orderProducts.getPageable(), orderProducts.getTotalElements());
    }
}