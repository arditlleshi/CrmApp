package com.alibou.security.contoller.admin;

import com.alibou.security.dto.OrderDto;
import com.alibou.security.dto.OrderResponseDto;
import com.alibou.security.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@Component("adminOrderController")
public class OrderController {
    private final OrderService orderService;
    @PostMapping
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
    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<OrderResponseDto>> findAll(@PathVariable Integer pageNumber, @PathVariable Integer pageSize){
        Page<OrderResponseDto> orderResponseDtoList = orderService.findAll(pageNumber, pageSize);
        return ResponseEntity.ok(orderResponseDtoList);
    }
}
