package com.alibou.security.service.implementation;

import com.alibou.security.dto.OrderProductDto;
import com.alibou.security.dto.OrderProductResponseDto;
import com.alibou.security.model.Order;
import com.alibou.security.model.OrderProduct;
import com.alibou.security.model.Product;
import com.alibou.security.model.User;
import com.alibou.security.repository.OrderProductRepository;
import com.alibou.security.repository.OrderRepository;
import com.alibou.security.repository.ProductRepository;
import com.alibou.security.repository.UserRepository;
import com.alibou.security.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                () -> new IllegalStateException("Order not found with id: " + orderProductDto.getOrderId())
        );
        Product product = productRepository.findById(orderProductDto.getProductId()).orElseThrow(
                () -> new IllegalStateException("Product not found with id: " + orderProductDto.getProductId())
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
    public Optional<OrderProductResponseDto> findById(Integer id) {
        return Optional.ofNullable(orderProductRepository.findById(id)
                .map(this::convertToResponseDto).orElseThrow(() ->
                        new IllegalStateException("Order-Product not found with id: " + id)));
    }

    @Override
    public List<OrderProductResponseDto> findAll() {
        List<OrderProduct> orderProducts = orderProductRepository.findAll();
        return convertToResponseDto(orderProducts);
    }

    @Override
    public OrderProductResponseDto create(OrderProductDto orderProductDto, UserDetails userDetails) throws IllegalAccessException {
        OrderProduct orderProduct = new OrderProduct();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        Order order = orderRepository.findById(orderProductDto.getOrderId()).orElseThrow(
                () -> new IllegalStateException("Order not found with id: " + orderProductDto.getOrderId())
        );
        Product product = productRepository.findById(orderProductDto.getProductId()).orElseThrow(
                () -> new IllegalStateException("Product not found with id: " + orderProductDto.getProductId())
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
    public Optional<OrderProductResponseDto> findById(Integer id, UserDetails userDetails) throws IllegalAccessException {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        OrderProduct orderProduct = orderProductRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("Order not found with id: " + id)
        );
        if (!orderProduct.getOrder().getUser().equals(user)){
            throw new IllegalAccessException("You can't view this order!");
        }
        return Optional.ofNullable(convertToResponseDto(orderProduct));
    }

    @Override
    public List<OrderProductResponseDto> findAll(UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        List<OrderProduct> orderProducts = orderProductRepository.findByUserId(user.getId());
        return convertToResponseDto(orderProducts);
    }

    private OrderProductResponseDto convertToResponseDto(OrderProduct orderProduct){
        return mapper.map(orderProduct, OrderProductResponseDto.class);
    }
    private List<OrderProductResponseDto> convertToResponseDto(List<OrderProduct> orderProducts){
        List<OrderProductResponseDto> orderProductResponseDtoList = new ArrayList<>();
        for (OrderProduct orderProduct : orderProducts){
            OrderProductResponseDto orderProductResponseDto = new OrderProductResponseDto();
            orderProductResponseDto.setId(orderProduct.getId());
            orderProductResponseDto.setProductId(orderProduct.getProduct().getId());
            orderProductResponseDto.setOrderId(orderProduct.getOrder().getId());
            orderProductResponseDto.setQuantity(orderProduct.getQuantity());
            orderProductResponseDto.setAmount(orderProduct.getAmount());
            orderProductResponseDtoList.add(orderProductResponseDto);
        }
        return orderProductResponseDtoList;
    }
}