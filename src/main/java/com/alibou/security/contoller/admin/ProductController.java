package com.alibou.security.contoller.admin;

import com.alibou.security.dto.ProductDto;
import com.alibou.security.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.create(productDto));
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable("id") Integer id){
        Optional<ProductDto> productDto = productService.findById(id);
        return productDto.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<List<ProductDto>> findAll(){
        List<ProductDto> productDtoList = productService.findAll();
        return ResponseEntity.ok(productDtoList);
    }
    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<ProductDto>> findAll(@PathVariable Integer pageNumber, @PathVariable Integer pageSize){
        Page<ProductDto> productDtoList = productService.findAll(pageNumber, pageSize);
        return ResponseEntity.ok(productDtoList);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable("id") Integer id, @RequestBody ProductDto productDto){
        return ResponseEntity.ok(productService.update(id, productDto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Integer id){
        return ResponseEntity.ok(productService.deleteById(id));
    }
}