package com.alibou.security.contoller.user;

import com.alibou.security.dto.OrderProductDto;
import com.alibou.security.dto.OrderProductResponseDto;
import com.alibou.security.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/operator/order-product")
@RequiredArgsConstructor
public class OrderProductController {
    private final OrderProductService orderProductService;
    @PostMapping
    public ResponseEntity<OrderProductResponseDto> create(@RequestBody OrderProductDto orderProductDto, @AuthenticationPrincipal UserDetails userDetails) throws IllegalAccessException {
        return ResponseEntity.ok(orderProductService.create(orderProductDto, userDetails));
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderProductResponseDto> findById(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) throws IllegalAccessException {
        Optional<OrderProductResponseDto> orderProductResponseDto = orderProductService.findById(id, userDetails);
        return orderProductResponseDto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<List<OrderProductResponseDto>> findAll(@AuthenticationPrincipal UserDetails userDetails){
        List<OrderProductResponseDto> orderProductResponseDtoList = orderProductService.findAll(userDetails);
        return ResponseEntity.ok(orderProductResponseDtoList);
    }
}
