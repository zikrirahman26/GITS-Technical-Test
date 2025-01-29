package com.programming.service.impl;

import com.programming.dto.ProductRequest;
import com.programming.dto.ProductResponse;
import com.programming.entity.Product;
import com.programming.repository.ProductRepository;
import com.programming.service.ProductService;
import com.programming.service.ValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ValidationService validationService;

    public ProductServiceImpl(ProductRepository productRepository, ValidationService validationService) {
        this.productRepository = productRepository;
        this.validationService = validationService;
    }

    @Override
    public void addProduct(ProductRequest productRequest) {

        validationService.validate(productRequest);

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setSkuCode(generateSkuCode());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setDescription(productRequest.getDescription());
        productRepository.save(product);
    }

    @Override
    public ProductResponse getProduct(Long id) {
        Product optionalProduct = productRepository.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return productResponse(optionalProduct);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> productList = productRepository.findAll();
        return productList.stream().map(this::productResponse).collect(Collectors.toList());
    }

    @Override
    public void updateProduct(ProductRequest productRequest, Long id) {

        validationService.validate(productRequest);

        Product product = productRepository.findById(id).orElseThrow(()
                -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        product.setCategory(productRequest.getCategory());
        product.setDescription(productRequest.getDescription());
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        if (productRepository.findById(id).isPresent()) {
            productRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found");
        }
    }

    public ProductResponse productResponse(Product product) {
        return ProductResponse.builder()
                .name(product.getName())
                .skuCode(product.getSkuCode())
                .imageUrl(product.getImageUrl())
                .price(product.getPrice())
                .category(product.getCategory())
                .description(product.getDescription())
                .build();
    }

    public String generateSkuCode() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString().replace("-", "").toUpperCase().substring(0, 8);
    }
}
