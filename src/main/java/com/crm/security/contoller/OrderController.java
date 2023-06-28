package com.crm.security.contoller;

import com.crm.security.dto.OrderDto;
import com.crm.security.dto.OrderResponseDto;
import com.crm.security.exception.ClientNotFoundException;
import com.crm.security.exception.UserNotFoundException;
import com.crm.security.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crm-app/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> create(@RequestBody OrderDto orderDto, @AuthenticationPrincipal UserDetails userDetails) throws IllegalAccessException, UserNotFoundException, ClientNotFoundException {
        return ResponseEntity.ok(orderService.create(orderDto, userDetails));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> findById(@PathVariable("id") Integer id, @AuthenticationPrincipal UserDetails userDetails) throws IllegalAccessException, UserNotFoundException {
        OrderResponseDto orderResponseDto = orderService.findById(id, userDetails);
        return ResponseEntity.ok(orderResponseDto);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> findAll(@AuthenticationPrincipal UserDetails userDetails) throws UserNotFoundException {
        List<OrderResponseDto> orderResponseDtoList = orderService.findAll(userDetails);
        return ResponseEntity.ok(orderResponseDtoList);
    }

    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<OrderResponseDto>> findAll(@PathVariable Integer pageNumber, @PathVariable Integer pageSize, @AuthenticationPrincipal UserDetails userDetails) throws UserNotFoundException {
        Page<OrderResponseDto> orderResponseDtoList = orderService.findAll(pageNumber, pageSize, userDetails);
        return ResponseEntity.ok(orderResponseDtoList);
    }
}
