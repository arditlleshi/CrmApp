package com.crm.security.contoller.admin;

import com.crm.security.dto.OrderProductDto;
import com.crm.security.dto.OrderProductResponseDto;
import com.crm.security.service.OrderProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/order-product")
@Component("adminOrderProductController")
public class OrderProductController {
    private final OrderProductService orderProductService;
    @PostMapping
    public ResponseEntity<OrderProductResponseDto> create(@RequestBody OrderProductDto orderProductDto) {
        return ResponseEntity.ok(orderProductService.create(orderProductDto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderProductResponseDto> findById(@PathVariable("id") Integer id){
        OrderProductResponseDto orderProductResponseDto = orderProductService.findById(id);
        return ResponseEntity.ok(orderProductResponseDto);
    }
    @GetMapping
    public ResponseEntity<List<OrderProductResponseDto>> findAll(){
        List<OrderProductResponseDto> orderProductResponseDtoList = orderProductService.findAll();
        return ResponseEntity.ok(orderProductResponseDtoList);
    }
    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<OrderProductResponseDto>> findAll(@PathVariable Integer pageNumber, @PathVariable Integer pageSize){
        Page<OrderProductResponseDto> orderProductResponseDtoList = orderProductService.findAll(pageNumber, pageSize);
        return ResponseEntity.ok(orderProductResponseDtoList);
    }
}
