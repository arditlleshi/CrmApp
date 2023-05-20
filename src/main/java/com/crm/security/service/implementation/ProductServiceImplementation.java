package com.crm.security.service.implementation;

import com.crm.security.dto.ProductDto;
import com.crm.security.model.Product;
import com.crm.security.repository.ProductRepository;
import com.crm.security.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImplementation implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper mapper;

    @Override
    public ProductDto create(ProductDto productDto){
        Product product = dtoToEntity(productDto);
        productRepository.save(product);
        return convertToResponseDto(product);
    }

    @Override
    public ProductDto findById(Integer id){
        return productRepository.findById(id)
                .map(this::convertToResponseDto).orElseThrow(() ->
                        new EntityNotFoundException("Product not found with id: " + id));
    }

    @Override
    public List<ProductDto> findAll(){
        List<Product> products = productRepository.findAll();
        return convertToResponseDto(products);
    }

    @Override
    public Page<ProductDto> findAll(Integer pageNumber, Integer pageSize){
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> products = productRepository.findAll(pageable);
        return convertToResponseDto(products);
    }

    @Override
    public Page<ProductDto> search(String query, Integer pageNumber, Integer pageSize){
        Specification<Product> specification = ((root, query1, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.like(root.get("name"), "%" + query + "%"),
                criteriaBuilder.like(root.get("unit"), "%" + query + "%")
                                                                                                     ));
        Pageable pageable = PageRequest.of(pageNumber - 1, pageSize);
        Page<Product> products = productRepository.findAll(specification, pageable);
        return convertToResponseDto(products);
    }

    @Override
    public ProductDto update(Integer id, ProductDto productDto){
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setName(productDto.getName());
            product.setPrice(productDto.getPrice());
            product.setUnit(productDto.getUnit());
            productRepository.save(product);
            return convertToResponseDto(product);
        } else {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
    }

    @Override
    public String deleteById(Integer id){
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return "Successfully deleted product with id: " + id;
        } else {
            return "Product not found with id: " + id;
        }
    }

    private Product dtoToEntity(ProductDto productDto){
        return mapper.map(productDto, Product.class);
    }

    private ProductDto convertToResponseDto(Product product){
        return mapper.map(product, ProductDto.class);
    }

    public List<ProductDto> convertToResponseDto(List<Product> products){
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product : products) {
            productDtoList.add(convertToResponseDto(product));
        }
        return productDtoList;
    }

    public Page<ProductDto> convertToResponseDto(Page<Product> products){
        List<ProductDto> productDtoList = new ArrayList<>();
        for (Product product : products) {
            productDtoList.add(convertToResponseDto(product));
        }
        return new PageImpl<>(productDtoList, products.getPageable(), products.getTotalElements());
    }
}
