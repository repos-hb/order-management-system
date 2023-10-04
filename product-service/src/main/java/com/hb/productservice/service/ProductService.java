package com.hb.productservice.service;

import com.hb.productservice.dto.ProductRequest;
import com.hb.productservice.dto.ProductResponse;
import com.hb.productservice.model.Product;
import com.hb.productservice.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void create(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        log.info("Saving {}", product);
        productRepository.save(product);
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(this::mapProductsToProductResponse).collect(Collectors.toList());
    }

    private ProductResponse mapProductsToProductResponse(Product product) {
        return ProductResponse.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .id(product.getId())
                .build();
    }
}
