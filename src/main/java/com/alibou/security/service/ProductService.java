package com.alibou.security.service;

import com.alibou.security.dto.ProductDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    ProductDto create(ProductDto productDto);
    ProductDto findById(Integer id);
    List<ProductDto> findAll();
    Page<ProductDto> findAll(Integer pageNumber, Integer pageSize);
    ProductDto update(Integer id, ProductDto productDto);
    String deleteById(Integer id);
    Page<ProductDto> search(String query, Integer pageNumber, Integer pageSize);
}
