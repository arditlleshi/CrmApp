package com.alibou.security.contoller.user;

import com.alibou.security.dto.OrderDto;
import com.alibou.security.dto.OrderResponseDto;
import com.alibou.security.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/operator/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping
    public ResponseEntity<OrderResponseDto> create(@RequestBody OrderDto orderDto, @AuthenticationPrincipal UserDetails userDetails) throws IllegalAccessException {
        return ResponseEntity.ok(orderService.create(orderDto, userDetails));
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> findById(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) throws IllegalAccessException {
        Optional<OrderResponseDto> orderResponseDto = orderService.findById(id, userDetails);
        return orderResponseDto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> findAll(@AuthenticationPrincipal UserDetails userDetails){
        List<OrderResponseDto> orderResponseDtoList = orderService.findAll(userDetails);
        return ResponseEntity.ok(orderResponseDtoList);
    }
}
