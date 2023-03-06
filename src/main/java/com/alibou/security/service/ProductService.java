package com.alibou.security.service;

import com.alibou.security.dto.ProductDto;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductDto create(ProductDto productDto);
    Optional<ProductDto> findById(Integer id);
    List<ProductDto> findAll();
    ProductDto update(Integer id, ProductDto productDto);
    String deleteById(Integer id);
}
