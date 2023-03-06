package com.alibou.security.contoller;

import com.alibou.security.dto.OrderProductDto;
import com.alibou.security.dto.OrderProductResponseDto;
import com.alibou.security.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order-product")
@RequiredArgsConstructor
public class OrderProductController {
    private final OrderProductService orderProductService;
    @PostMapping("/create")
    public ResponseEntity<OrderProductResponseDto> create(@RequestBody OrderProductDto orderProductDto) {
        return ResponseEntity.ok(orderProductService.create(orderProductDto));
    }
}
