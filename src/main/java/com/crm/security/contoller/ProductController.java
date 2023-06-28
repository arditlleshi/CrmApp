package com.crm.security.contoller;

import com.crm.security.dto.ProductDto;
import com.crm.security.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/crm-app/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.create(productDto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> findById(@PathVariable("id") Integer id) {
        ProductDto productDto = productService.findById(id);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> findAll() {
        List<ProductDto> productDtoList = productService.findAll();
        return ResponseEntity.ok(productDtoList);
    }

    @GetMapping("/{pageNumber}/{pageSize}")
    public ResponseEntity<Page<ProductDto>> findAll(@PathVariable Integer pageNumber, @PathVariable Integer pageSize) {
        Page<ProductDto> productDtoList = productService.findAll(pageNumber, pageSize);
        return ResponseEntity.ok(productDtoList);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDto>> search(@RequestParam("q") String query, @RequestParam("n") Integer pageNumber, @RequestParam("s") Integer pageSize) {
        return ResponseEntity.ok(productService.search(query, pageNumber, pageSize));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable("id") Integer id, @RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.update(id, productDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(productService.deleteById(id));
    }
}