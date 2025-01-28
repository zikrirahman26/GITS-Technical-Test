package com.programming.controller;

import com.programming.dto.ProductRequest;
import com.programming.dto.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.programming.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody ProductRequest productRequest) {
        productService.addProduct(productRequest);
        return new ResponseEntity<>("Successfully Added", HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> updateProduct(@RequestBody ProductRequest productRequest, @PathVariable Long id) {
        productService.updateProduct(productRequest, id);
        return new ResponseEntity<>("Successfully Updated", HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse productResponse = productService.getProduct(id);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> productsResponses = productService.getAllProducts();
        return new ResponseEntity<>(productsResponses, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>("Successfully Deleted", HttpStatus.OK);
    }
}
