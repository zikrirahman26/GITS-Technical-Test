package com.programming.service;

import com.programming.dto.ProductRequest;
import com.programming.dto.ProductResponse;

import java.util.List;

public interface ProductService {

    void addProduct(ProductRequest productRequest);

    ProductResponse getProduct(Long id);

    List<ProductResponse> getAllProducts();

    void updateProduct(ProductRequest productRequest, Long id);

    void deleteProduct(Long id);
}
