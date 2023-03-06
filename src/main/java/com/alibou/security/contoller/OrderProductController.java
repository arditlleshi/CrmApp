package com.alibou.security.contoller;

import com.alibou.security.dto.OrderProductDto;
import com.alibou.security.dto.OrderProductResponseDto;
import com.alibou.security.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/order-product")
@RequiredArgsConstructor
public class OrderProductController {
    private final OrderProductService orderProductService;
    @PostMapping("/create")
    public ResponseEntity<OrderProductResponseDto> create(@RequestBody OrderProductDto orderProductDto) {
        return ResponseEntity.ok(orderProductService.create(orderProductDto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderProductResponseDto> findById(@PathVariable("id") Integer id){
        Optional<OrderProductResponseDto> orderProductResponseDto = orderProductService.findById(id);
        return orderProductResponseDto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<List<OrderProductResponseDto>> findAll(){
        List<OrderProductResponseDto> orderProductResponseDtoList = orderProductService.findAll();
        return ResponseEntity.ok(orderProductResponseDtoList);
    }
}
