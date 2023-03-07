package com.alibou.security.service;

import com.alibou.security.dto.ProductDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductDto create(ProductDto productDto);
    Optional<ProductDto> findById(Integer id);
    List<ProductDto> findAll();
    Page<ProductDto> findAll(Integer pageNumber, Integer pageSize);
    ProductDto update(Integer id, ProductDto productDto);
    String deleteById(Integer id);
}
