package com.crm.security.contoller.operator;

import com.crm.security.dto.OrderProductDto;
import com.crm.security.dto.OrderProductResponseDto;
import com.crm.security.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/operator/order-product")
@Component("operatorOrderProductController")
public class OrderProductController {
    private final OrderProductService orderProductService;
    @PostMapping
    public ResponseEntity<OrderProductResponseDto> create(@RequestBody OrderProductDto orderProductDto, @AuthenticationPrincipal UserDetails userDetails) throws IllegalAccessException {
        return ResponseEntity.ok(orderProductService.create(orderProductDto, userDetails));
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderProductResponseDto> findById(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) throws IllegalAccessException {
        OrderProductResponseDto orderProductResponseDto = orderProductService.findById(id, userDetails);
        return ResponseEntity.ok(orderProductResponseDto);
    }
    @GetMapping
    public ResponseEntity<List<OrderProductResponseDto>> findAll(@AuthenticationPrincipal UserDetails userDetails){
        List<OrderProductResponseDto> orderProductResponseDtoList = orderProductService.findAll(userDetails);
        return ResponseEntity.ok(orderProductResponseDtoList);
    }
    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<OrderProductResponseDto>> findAll(@PathVariable Integer pageNumber, @PathVariable Integer pageSize, @AuthenticationPrincipal UserDetails userDetails){
        Page<OrderProductResponseDto> orderProductResponseDtoList = orderProductService.findAll(pageNumber, pageSize, userDetails);
        return ResponseEntity.ok(orderProductResponseDtoList);
    }
}
