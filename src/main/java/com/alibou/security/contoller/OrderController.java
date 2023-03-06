package com.alibou.security.contoller;

import com.alibou.security.dto.OrderDto;
import com.alibou.security.dto.OrderResponseDto;
import com.alibou.security.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    @PostMapping("/create")
    public ResponseEntity<OrderResponseDto> create(@RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(orderService.create(orderDto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> findById(@PathVariable("id") Integer id){
        Optional<OrderResponseDto> orderResponseDto = orderService.findById(id);
        return orderResponseDto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> findAll(){
        List<OrderResponseDto> orderResponseDtoList = orderService.findAll();
        return ResponseEntity.ok(orderResponseDtoList);
    }
}
